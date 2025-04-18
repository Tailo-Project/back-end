package com.growith.tailo.feed.hashtag.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.FeedPostHashtag;
import com.growith.tailo.feed.feed.repository.FeedPostHashtagRepository;
import com.growith.tailo.feed.hashtag.dto.HashtagDto;
import com.growith.tailo.feed.hashtag.entity.Hashtag;
import com.growith.tailo.feed.hashtag.repository.HashtagRepository;
import com.growith.tailo.feed.hashtag.service.HashtagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final FeedPostHashtagRepository feedPostHashtagRepository;

    // 피드 게시물에 작성한 해시 연결
    @Override
    @Transactional
    public void linkHashtagsToPost(List<HashtagDto> hashtagList, FeedPost feedPost) {
        for (HashtagDto dto : hashtagList) {
            Hashtag hashtag = hashtagRepository.findByHashtag(dto.hashtagName())
                    .orElseGet(() -> hashtagRepository.save(
                            Hashtag.builder().
                                    hashtag(dto.hashtagName())
                                    .count(0)
                                    .build()
                    ));

            // 해시 카운트 증가
            hashtag.increaseCount();

            FeedPostHashtag linkHashtag = FeedPostHashtag.builder()
                    .feedPost(feedPost)
                    .hashtag(hashtag)
                    .build();

            feedPostHashtagRepository.save(linkHashtag);
        }
    }

    // 해시 업데이트
    @Override
    public void updateHashtagHandler(List<HashtagDto> updatedHashtags, FeedPost feedPost) {

        List<HashtagDto> currentHashtags = feedPostHashtagRepository.findAllByFeedPost(feedPost);

        Set<String> updatedHashtagNames = new HashSet<>();
        for (HashtagDto dto : updatedHashtags) {
            updatedHashtagNames.add(dto.hashtagName());
        }

        Set<String> currentHashtagNames = new HashSet<>();
        for (HashtagDto dto : currentHashtags) {
            currentHashtagNames.add(dto.hashtagName());
        }

        Set<String> deleteHashtagNames = new HashSet<>(currentHashtagNames);
        deleteHashtagNames.removeAll(updatedHashtagNames);

        Set<String> registerHashtagNames = new HashSet<>(updatedHashtagNames);
        deleteHashtagNames.removeAll(currentHashtagNames);

        for (String deleteHashtagName : deleteHashtagNames) {
            deleteHashtag(deleteHashtagName, feedPost);
        }

        for (String registerHashtagName : registerHashtagNames) {
            registerHashtag(registerHashtagName, feedPost);
        }
    }

    // 해시 삭제
    private void deleteHashtag(String deleteHashtagName, FeedPost feedPost) {

        Hashtag hashtag = hashtagRepository.findByHashtag(deleteHashtagName)
                .orElseThrow(() -> new ResourceNotFoundException("해시태그를 찾을 수 없습니다."));

        // 해시 카운즈 차감
        hashtag.increaseCount();

        feedPostHashtagRepository.deleteByHashtagAndFeedPost(hashtag, feedPost);
    }

    // 해시 수정
    private void registerHashtag(String registerHashtagName, FeedPost feedPost) {
        Hashtag hashtag = hashtagRepository.findByHashtag(registerHashtagName)
                .orElseGet(() -> hashtagRepository.save(
                        Hashtag.builder().
                                hashtag(registerHashtagName)
                                .count(0)
                                .build()
                ));

        // 해시 카운트 증가
        hashtag.increaseCount();

        FeedPostHashtag linkHashtag = FeedPostHashtag.builder()
                .feedPost(feedPost)
                .hashtag(hashtag)
                .build();

        feedPostHashtagRepository.save(linkHashtag);
    }
}
