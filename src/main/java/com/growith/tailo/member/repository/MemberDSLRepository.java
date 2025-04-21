package com.growith.tailo.member.repository;

import com.growith.tailo.member.dto.response.MemberProfileResponse;
import com.growith.tailo.member.entity.Member;

import java.util.Optional;

public interface MemberDSLRepository {
    MemberProfileResponse findByMemberProfile(String accountId);
}
