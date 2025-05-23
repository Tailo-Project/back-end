package com.growith.tailo.follow.service;

import com.growith.tailo.common.exception.ResourceAlreadyExistException;
import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.follow.dto.response.FollowMeResponse;
import com.growith.tailo.follow.dto.response.MyFollowResponse;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.follow.repository.FollowRepository;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.notification.enums.NotificationType;
import com.growith.tailo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public String follow(Member member, String accountId) {
        Member target = findTarget(accountId);
        if (member.getAccountId().equals(accountId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인은 팔로우할 수 없습니다.");
        }
        if (followRepository.existsByFollowerAndFollowing(member, target)) {
            throw new ResourceAlreadyExistException("이미 팔로우 된 상태입니다.");
        }
        Follow follow = Follow.builder()
                .follower(member)
                .following(target)
                .build();
        followRepository.save(follow);

        // TODO: MQ 도입 시 비동기로 변경 예정
        String notificationUrl = String.format("%s/api/member/profile/%s", baseUrl, member.getAccountId());
        notificationService.send(target, member, NotificationType.FOLLOW, notificationUrl);

        return "팔로우 성공";
    }

    @Transactional
    public String followCancel(Member member, String accountId) {

        Member target = findTarget(accountId);

        if (!followRepository.existsByFollowerAndFollowing(member, target)) {
            throw new ResourceAlreadyExistException("팔로우 상태가 아닙니다.");

        }

        followRepository.deleteByFollowerAndFollowing(member, target);

        return "팔로우 취소 성공";

    }

    public Page<MyFollowResponse> getFollowList(String accountId, Pageable pageable) {
        Member member = findTarget(accountId);

        return followRepository.countFollowByFollower(member) == 0 ?
                Page.empty(pageable) : followRepository.findAllMyFollow(member, pageable);

    }

    public Page<FollowMeResponse> getTargetList(String accountId, Pageable pageable) {
        Member member = findTarget(accountId);

        return followRepository.countFollowByFollowing(member) == 0 ?
                Page.empty(pageable) : followRepository.findAllFollowMe(member, pageable);

    }

    private Member findTarget(String accountId) {
        log.info("팔로우 조회:{}", accountId);
        Member target = memberRepository.findByAccountId(accountId).orElseThrow(
                () -> new ResourceNotFoundException(" 사용자를 찾을 수 없습니다. ")
        );
        return target;
    }
}
