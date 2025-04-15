package com.growith.tailo.user.repository;

import com.growith.tailo.auth.oauth.entity.OAuthProvider;
import com.growith.tailo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);


    /** 이메일로 조회 (로컬/소셜 공통) */
    Optional<User> findByEmail(String email);
    /** 이메일 + 소셜 제공자 기반 조회 (소셜 로그인용) */
    Optional<User> findByEmailAndProvider(String email, OAuthProvider provider);
    /** providerId로 조회 (소셜 로그인 고유 식별자) */
    Optional<User> findByProviderAndProviderId(OAuthProvider provider, String providerId);

    /** 이메일 존재 여부 확인 */
    boolean existsByEmail(String email);
    /** 닉네임 중복 확인 */
    boolean existsByNickname(String nickname);
}
