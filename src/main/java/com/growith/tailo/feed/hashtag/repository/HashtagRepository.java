package com.growith.tailo.feed.hashtag.repository;

import com.growith.tailo.feed.hashtag.entity.Hashtags;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtags, Long> {

    // 동시성을 위해 비관적락 사용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Hashtags> findByHashtag(String hashtag);

}
