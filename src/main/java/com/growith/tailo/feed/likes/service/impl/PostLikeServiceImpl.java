package com.growith.tailo.feed.likes.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.repository.FeedPostRepository;
import com.growith.tailo.feed.likes.dto.response.LikedFeedIdsResponse;
import com.growith.tailo.feed.likes.entity.PostLike;
import com.growith.tailo.feed.likes.repository.PostLikeRepository;
import com.growith.tailo.feed.likes.service.PostLikeService;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.notification.enums.NotificationType;
import com.growith.tailo.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final RedissonClient redissonClient;
    private final MemberRepository memberRepository;
    private final FeedPostRepository feedPostRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationService notificationService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public String likeFeedPost(Long feedId, Member member) {

        String lockKey = "LOCK:feed:" + feedId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean available = lock.tryLock(5, 3, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("좋아요 락 획득 실패: 다른 사용자가 처리 중입니다.");
            }

            if (member == null || !memberRepository.existsByAccountId(member.getAccountId())) {
                throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
            }

            FeedPost feedPost = feedPostRepository.findById(feedId)
                    .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 피드입니다."));

            boolean isAlreadyLiked = postLikeRepository.existsByMemberAndFeedPost(member, feedPost);

            if (isAlreadyLiked) {
                postLikeRepository.deleteByMemberAndFeedPost(member, feedPost);
                return "좋아요 취소";

            } else {

                PostLike postLike = PostLike.builder()
                        .member(member)
                        .feedPost(feedPost)
                        .build();

                postLikeRepository.save(postLike);

                // TODO : MQ 도입 시 비동기로 변경 예정
                String notificationUrl = String.format("%s/api/feed/%s", baseUrl, feedId);
                notificationService.send(feedPost.getAuthor(), member, NotificationType.LIKE, notificationUrl);

                return "좋아요 성공";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 대기 중 인터럽트 발생", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Override
    public String countLikes(Long feedId, Member member) {

        if (member == null || !memberRepository.existsById(member.getId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        return postLikeRepository.countByFeedPostId(feedId);
    }

    @Override
    public LikedFeedIdsResponse getLikedFeedIds(Member member) {

        if (member == null || !memberRepository.existsById(member.getId())) {
            throw new ResourceNotFoundException("해당 회원이 존재하지 않습니다.");
        }

        List<Long> likedFeedIds = postLikeRepository.findFeedIdsByMemberId(member.getId());

        return new LikedFeedIdsResponse(likedFeedIds);
    }
}
