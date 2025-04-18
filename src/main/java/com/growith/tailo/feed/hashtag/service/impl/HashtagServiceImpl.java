package com.growith.tailo.feed.hashtag.service.impl;

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

import java.util.List;

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
}
