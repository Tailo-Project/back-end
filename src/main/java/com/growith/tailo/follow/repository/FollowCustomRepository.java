package com.growith.tailo.follow.repository;

import com.growith.tailo.follow.dto.response.FollowMeResponse;
import com.growith.tailo.follow.dto.response.MyFollowResponse;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowCustomRepository {
    Page<MyFollowResponse> findAllMyFollow(Member member, Pageable pageable);
    Page<FollowMeResponse> findAllFollowMe(Member member, Pageable pageable);
}
