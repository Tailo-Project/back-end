package com.growith.tailo.common.exception.image;

import com.growith.tailo.common.exception.BadRequestException;

public class FailUploadImageException extends BadRequestException {
    public FailUploadImageException(String message) {
        super(message);
    }

}
