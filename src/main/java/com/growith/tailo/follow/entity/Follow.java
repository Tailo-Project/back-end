package com.growith.tailo.follow.entity;

import com.growith.tailo.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private Member follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private Member following;
    
    // getters and setters
}