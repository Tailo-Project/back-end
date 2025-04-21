package com.growith.tailo.feed.comment.repository;

import com.growith.tailo.feed.comment.entity.Comment;
import com.growith.tailo.feed.feed.entity.FeedPost;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    void deleteByFeedPost(FeedPost feedPost);

    boolean existsByAuthorAndFeedPostAndContentAndCreatedAtAfter(Member member, FeedPost feedPost, String content, LocalDateTime time);

    List<Comment> findByParentComment(Comment comment);
}
