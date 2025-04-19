package com.growith.tailo.feed.comment.repository;

import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.feed.feed.entity.FeedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByFeedPost(FeedPost feedPost);
}
