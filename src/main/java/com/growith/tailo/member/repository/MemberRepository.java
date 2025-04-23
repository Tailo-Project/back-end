package com.growith.tailo.member.repository;

import com.growith.tailo.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<Member,Long>, MemberDSLRepository{

    Optional<Member> findByAccountId(String accountId);

    boolean existsByAccountId(String accountId);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(@NotBlank(message = "이메일은 필수 입니다.") String email);
}
