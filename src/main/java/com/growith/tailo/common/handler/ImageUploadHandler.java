package com.growith.tailo.common.handler;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.growith.tailo.common.exception.image.ExceededImageNumberException;
import com.growith.tailo.common.exception.image.FailUploadImageException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImageUploadHandler {

    private static final int MAX_IMAGE_COUNT = 4;

    @Resource
    private Cloudinary cloudinary;

    @Value("${spring.profiles.active}")
    private String profile;

    public List<String> uploadMultiImages(List<MultipartFile> files) {
        if (files.size() > MAX_IMAGE_COUNT) {
            throw new ExceededImageNumberException("이미지 갯수를 초과하였습니다.");
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String filename = file.getOriginalFilename();
            if (filename == null || !isAllowedExtension(filename)) {
                throw new FailUploadImageException("jpg, png, webp 형식의 이미지 파일만 업로드할 수 있습니다.");
            }

            String uploadUrl;
            if ("prod".equals(profile)) {
                uploadUrl = uploadToS3(file);
            } else {
                uploadUrl = uploadToCloudinary(file);
            }
            imageUrls.add(uploadUrl);
        }

        return imageUrls;
    }

    public String uploadSingleImages(MultipartFile file) {

        String filename = file.getOriginalFilename();
        if (filename == null || !isAllowedExtension(filename)) {
            throw new FailUploadImageException("jpg, png, webp 형식의 이미지 파일만 업로드할 수 있습니다.");
        }

        String uploadUrl;
        if ("prod".equals(profile)) {
            uploadUrl = uploadToS3(file);
        } else {
            uploadUrl = uploadToCloudinary(file);
        }

        return uploadUrl;
    }

    public void deleteImage(String deletedImageUrl) {
        if ("prod".equals(profile)) {
            // TODO : D3 연동하게 된다면 추가
        } else {
            deleteToCloudinary(deletedImageUrl);
        }
    }

    private boolean isAllowedExtension(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".webp");
    }

    private String uploadToS3(MultipartFile file) {
        // TODO : S3 연동 시 사용
        return null;
    }

    private String uploadToCloudinary(MultipartFile file) {

        HashMap<Object, Object> options = new HashMap<>();
        options.put("folder", "tailo");
        String publicId = "";

        try {
            Map uploadFile = cloudinary.uploader().upload(file.getBytes(), options);
            publicId = (String) uploadFile.get("public_id");
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailUploadImageException("파일 저장 중 실패하였습니다.");
        }

        return cloudinary.url().secure(true).generate(publicId);
    }

    private void deleteToCloudinary(String deletedImageUrl) {
        String publicId = extractPublicId(deletedImageUrl);

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailUploadImageException("파일 삭제 중 실패하였습니다.");
        }
    }

    private String extractPublicId(String deletedImageUrl) {
        try {
            String afterUpload = deletedImageUrl.split("/upload/")[1];
            String withoutVersion = afterUpload.replaceFirst("^v\\d+/", "");
            String publicId = withoutVersion.replaceAll("\\.[^.]+$", "");
            return publicId;
        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 URL에서 Cloudinary Public ID 추출 실패" + deletedImageUrl + e.getMessage());
        }
    }
}
