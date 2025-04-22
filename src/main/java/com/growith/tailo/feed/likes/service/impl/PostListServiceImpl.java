package com.growith.tailo.feed.likes.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.repository.FeedPostRepository;
import com.growith.tailo.feed.likes.entity.PostLike;
import com.growith.tailo.feed.likes.repository.PostLikeRepository;
import com.growith.tailo.feed.likes.service.PostListService;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostListServiceImpl implements PostListService {

    private final MemberRepository memberRepository;
    private final FeedPostRepository feedPostRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional
    public String likeFeedPost(Long feedId, Member member) {

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

            return "좋아요 성공";
        }

    }

    @Override
    public String countLikes(Long feedId, Member member) {
        return postLikeRepository.countByFeedPostId(feedId);
    }
}
