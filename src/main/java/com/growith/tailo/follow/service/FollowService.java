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

    /**
     * Creates a follow relationship between the given member and the target member identified by accountId.
     *
     * Throws an exception if a member attempts to follow themselves or if the follow relationship already exists.
     * Sends a follow notification to the target member upon successful creation.
     *
     * @param member the member initiating the follow
     * @param accountId the account ID of the member to be followed
     * @return a success message indicating the follow was successful
     * @throws ResponseStatusException if a member tries to follow themselves
     * @throws ResourceAlreadyExistException if the follow relationship already exists
     */
    @Transactional
    public String follow(Member member, String accountId){
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

    /**
     * Cancels an existing follow relationship between the given member and the target member identified by accountId.
     *
     * @param member the member initiating the unfollow action
     * @param accountId the account ID of the member to unfollow
     * @return a success message indicating the follow has been canceled
     * @throws ResourceAlreadyExistException if the member is not currently following the target
     */
    @Transactional

    public String followCancel(Member member, String accountId) {
  
        Member target = findTarget(accountId);

        if (!followRepository.existsByFollowerAndFollowing(member, target)) {
            throw new ResourceAlreadyExistException("팔로우 상태가 아닙니다.");

        }

        followRepository.deleteByFollowerAndFollowing(member,target);

        return "팔로우 취소 성공";
    }

    /**
     * Retrieves a paginated list of members that the specified user is following.
     *
     * @param accountId the account ID of the member whose followings are to be listed
     * @param pageable pagination information
     * @return a page of members the user is following, wrapped in {@link MyFollowResponse}; returns an empty page if the user follows no one
     */
    public Page<MyFollowResponse> getFollowList(String accountId, Pageable pageable) {
        Member member = findTarget(accountId);

        return followRepository.countFollowByFollower(member) == 0 ?
                Page.empty(pageable) : followRepository.findAllMyFollow(member, pageable);

    }

    /**
     * Retrieves a paginated list of followers for the specified member.
     *
     * @param accountId the account ID of the member whose followers are to be listed
     * @param pageable pagination information
     * @return a page of followers wrapped in {@link FollowMeResponse}; empty if the member has no followers
     */
    public Page<FollowMeResponse> getTargetList(String accountId, Pageable pageable) {
        Member member = findTarget(accountId);

        return followRepository.countFollowByFollowing(member) == 0 ?
                Page.empty(pageable) : followRepository.findAllFollowMe(member, pageable);

    }

    /**
     * Retrieves a member by account ID or throws an exception if not found.
     *
     * @param accountId the unique account identifier of the member to retrieve
     * @return the member associated with the given account ID
     * @throws ResourceNotFoundException if no member exists with the specified account ID
     */
    private Member findTarget(String accountId) {
        log.info("팔로우 조회:{}", accountId);
        Member target = memberRepository.findByAccountId(accountId).orElseThrow(
                () -> new ResourceNotFoundException(" 사용자를 찾을 수 없습니다. ")
        );
        return target;
    }
}
