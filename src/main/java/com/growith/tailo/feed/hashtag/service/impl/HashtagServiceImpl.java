package com.growith.tailo.feed.hashtag.service.impl;

import com.growith.tailo.common.exception.ResourceNotFoundException;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.feed.feed.entity.FeedPostHashtag;
import com.growith.tailo.feed.feed.repository.FeedPostHashtagRepository;
import com.growith.tailo.feed.hashtag.dto.HashtagDto;
import com.growith.tailo.feed.hashtag.entity.Hashtags;
import com.growith.tailo.feed.hashtag.repository.HashtagRepository;
import com.growith.tailo.feed.hashtag.service.HashtagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
@Slf4j
public class HashtagServiceImpl implements HashtagService {

    private final HashtagRepository hashtagRepository;
    private final FeedPostHashtagRepository feedPostHashtagRepository;

    // 피드 게시물에 작성한 해시 연결
    @Override
    @Transactional
    public void linkHashtagsToPost(List<HashtagDto> hashtagList, FeedPost feedPost) {
        for (HashtagDto dto : hashtagList) {
            Hashtags hashtag = hashtagRepository.findByHashtag(dto.hashtag())
                    .orElseGet(() -> hashtagRepository.save(
                            Hashtags.builder().
                                    hashtag(dto.hashtag())
                                    .count(0)
                                    .build()
                    ));

            // 해시 카운트 증가
            hashtag.increaseCount();
            hashtagRepository.save(hashtag);

            FeedPostHashtag linkHashtag = FeedPostHashtag.builder()
                    .feedPost(feedPost)
                    .hashtags(hashtag)
                    .build();

            feedPostHashtagRepository.save(linkHashtag);
        }
    }

    // 해시 업데이트
    @Override
    public void updateHashtagHandler(List<HashtagDto> updatedHashtags, FeedPost feedPost) {

        List<FeedPostHashtag> currentHashtags = feedPostHashtagRepository.findHashtagByFeedPost(feedPost);

        Set<String> updatedHashtagNames = new HashSet<>();
        for (HashtagDto hashtag : updatedHashtags) {
            updatedHashtagNames.add(hashtag.hashtag());
        }

        Set<String> currentHashtagNames = new HashSet<>();
        for (FeedPostHashtag hashtag : currentHashtags) {
            currentHashtagNames.add(hashtag.getHashtags().getHashtag());
        }

        Set<String> deleteHashtagNames = new HashSet<>(currentHashtagNames);
        deleteHashtagNames.removeAll(updatedHashtagNames);

        Set<String> registerHashtagNames = new HashSet<>(updatedHashtagNames);
        registerHashtagNames.removeAll(currentHashtagNames);

        for (String deleteHashtagName : deleteHashtagNames) {
            deleteHashtag(deleteHashtagName, feedPost);
        }

        for (String registerHashtagName : registerHashtagNames) {
            registerHashtag(registerHashtagName, feedPost);
        }
    }

    // 해시 삭제
    private void deleteHashtag(String deleteHashtagName, FeedPost feedPost) {

        log.info("삭제될 요소 : " + deleteHashtagName);

        Hashtags hashtag = hashtagRepository.findByHashtag(deleteHashtagName)
                .orElseThrow(() -> new ResourceNotFoundException("해시태그를 찾을 수 없습니다."));

        // 해시 카운즈 차감
        hashtag.decreaseCount();
        hashtagRepository.save(hashtag);

        feedPostHashtagRepository.deleteByHashtagsAndFeedPost(hashtag, feedPost);
    }

    // 해시 수정
    private void registerHashtag(String registerHashtagName, FeedPost feedPost) {

        log.info("수정될 요소 : " + registerHashtagName);

        Hashtags hashtag = hashtagRepository.findByHashtag(registerHashtagName)
                .orElseGet(() -> hashtagRepository.save(
                        Hashtags.builder().
                                hashtag(registerHashtagName)
                                .count(0)
                                .build()
                ));

        // 해시 카운트 증가
        hashtag.increaseCount();
        hashtagRepository.save(hashtag);

        FeedPostHashtag linkHashtag = FeedPostHashtag.builder()
                .feedPost(feedPost)
                .hashtags(hashtag)
                .build();

        feedPostHashtagRepository.save(linkHashtag);
    }
}
