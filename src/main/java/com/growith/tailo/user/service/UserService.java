package com.growith.tailo.user.service;


import com.growith.tailo.auth.oauth.entity.OAuthProvider;
import com.growith.tailo.user.dto.LoginRequest;
import com.growith.tailo.user.dto.UserRequest;
import com.growith.tailo.user.dto.UserResponse;
import com.growith.tailo.user.entity.User;
import com.growith.tailo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** 로컬 회원가입 */
    public UserResponse signup(UserRequest requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail()))
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        if (userRepository.existsByNickname(requestDto.getNickName()))
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = requestDto.toEntity(encryptedPassword);
        return new UserResponse(userRepository.save(user));
    }

    /** 로컬 로그인 */
    public UserResponse login(LoginRequest requestDto) {
        User user = userRepository.findByEmailAndProvider(requestDto.getEmail(), OAuthProvider.LOCAL)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        return new UserResponse(user);
    }

    /** ID로 사용자 조회 */
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));
        return new UserResponse(user);
    }

}

