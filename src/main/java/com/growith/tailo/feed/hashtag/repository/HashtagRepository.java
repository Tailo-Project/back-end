package com.growith.tailo.feed.hashtag.repository;

import com.growith.tailo.feed.hashtag.entity.Hashtag;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    // 동시성을 위해 비관적락 사용
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Hashtag> findByHashtag(String hashtag);

}
