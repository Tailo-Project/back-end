package com.growith.tailo.search.feed.service.Impl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.growith.tailo.block.entity.BlockMember;
import com.growith.tailo.block.repository.BlockRepository;
import com.growith.tailo.feed.comment.repository.CommentRepository;
import com.growith.tailo.feed.likes.repository.PostLikeRepository;
import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.follow.repository.FollowRepository;
import com.growith.tailo.member.entity.Member;
import com.growith.tailo.member.repository.MemberRepository;
import com.growith.tailo.search.feed.docs.FeedDocument;
import com.growith.tailo.search.feed.dto.FeedSearchResponse;
import com.growith.tailo.search.feed.service.FeedSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedSearchServiceImpl implements FeedSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final FollowRepository followRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public Page<FeedSearchResponse> feedSearch(String keyword, Pageable pageable, Member member) {

        // 와일드 카드 설정
        String wildcardKeyword = "*" + keyword + "*";

        // 계정 아이디, 닉네임, 해시태그, 본문이 검색어에 포함되면 검색 결과에 포함되도록 한다.
        Query query = QueryBuilders.bool(b -> b
                .should(s -> s.wildcard(m -> m.field("content").value(wildcardKeyword)))
                .should(s -> s.wildcard(m -> m.field("hashtags").value(wildcardKeyword)))
                .should(s -> s.wildcard(m -> m.field("account_id").value(wildcardKeyword)))
                .should(s -> s.wildcard(m -> m.field("nickname").value(wildcardKeyword)))
        );

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query) // 검색 조건
                .withPageable(pageable) // 페이징 설정
                .withSort(s -> s.field(f -> f.field("updated_at").order(SortOrder.Desc))) // 최신순
                .build();

        // 실제로 ElasticSearch에 쿼리를 실행하여 결과 반환
        SearchHits<FeedDocument> searchHits = elasticsearchOperations.search(nativeQuery, FeedDocument.class);

        List<FeedDocument> feeds = searchHits.get().map(SearchHit::getContent).toList();
        List<String> accountIds = feeds.stream().map(FeedDocument::getAccountId).toList();

        // 팔로우, 차단 관계 조회
        List<Follow> follows = followRepository.findByFollowerAccountIdAndFollowingAccountIdIn(member.getAccountId(), accountIds);
        List<BlockMember> blocks = blockRepository.findByBlockerAccountIdAndBlockedAccountIdIn(member.getAccountId(), accountIds);

        List<String> blockIds = blocks.stream()
                .map(block -> block.getBlocked().getAccountId())
                .toList();

        List<String> followIds = follows.stream()
                .map(follow -> follow.getFollowing().getAccountId())
                .toList();

        // 차단한 사용자는 결과에서 제외, 팔로우 여부 표시
        List<FeedSearchResponse> responses = feeds.stream()
                .filter(m -> !blockIds.contains(m.getAccountId()))
                .map(m -> {

                    // 해시태그, 이미지 리스트화
                    List<String> hashtags = m.getHashtags() == null ?
                            new ArrayList<>() : Arrays.asList(m.getHashtags().split("&\\|&"));
                    List<String> imageUrls = m.getImageUrls() == null ?
                            new ArrayList<>() : Arrays.asList(m.getImageUrls().split("&\\|&"));

                    // TODO : 너무 많은 쿼리문이 나감 -> 조치 필요
                    // 댓글 수, 좋아요 수, 좋아요 여부
                    long commentCount = Long.parseLong(commentRepository.countByFeedPostId(m.getFeedPostId()));
                    long likesCount = Long.parseLong(postLikeRepository.countByFeedPostId(m.getFeedPostId()));
                    boolean isLiked = postLikeRepository.existsByMemberIdAndFeedPostId(member.getId(), m.getFeedPostId());

                    return FeedSearchResponse.builder()
                            .feedPostId(m.getFeedPostId())
                            .accountId(m.getAccountId())
                            .nickname(m.getNickname())
                            .profileImageUrl(m.getProfileImageUrl())
                            .content(m.getContent())
                            .hashtags(hashtags)
                            .imageUrls(imageUrls)
                            .createdAt(m.getCreatedAt())
                            .updatedAt(m.getUpdatedAt())
                            .isFollowing(followIds.contains(m.getAccountId()))
                            .likesCount(likesCount)
                            .commentsCount(commentCount)
                            .isLiked(isLiked)
                            .build();
                })
                .toList();

        if (responses.isEmpty()) {
            log.info("사용자 검색 결과 없음: keyword={}", keyword);
        } else {
            log.info("검색완료: keyword={}, 결과 수={}, 제외된 수={}",
                    keyword, responses.size(), responses.size() - feeds.size());
        }

        return new PageImpl<>(responses, pageable, searchHits.getTotalHits());
    }
}
