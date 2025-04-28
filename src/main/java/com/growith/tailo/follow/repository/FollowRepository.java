package com.growith.tailo.follow.repository;

import com.growith.tailo.follow.entity.Follow;
import com.growith.tailo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {
    Optional<Follow> findByFollowerAndFollowing(Member follower, Member following);

    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Member findByFollower(Member follower);

    void deleteByFollowerAndFollowing(Member follower, Member following);

    boolean existsByFollower(Member follower);

    long countFollowByFollower(Member follower);

    long countFollowByFollowing(Member following);

    List<Follow> findByFollowerIdAndFollowingIdIn(Long id, List<Long> memberIds);
}
