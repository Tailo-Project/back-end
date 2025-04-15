package com.growith.tailo.feed.hashtag.entity;

import com.growith.tailo.feed.feed.entity.FeedPostHashtag;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hashtag")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashtag;

    @OneToMany(mappedBy = "hashtag")
    private List<FeedPostHashtag> feedPostHashtags;
    
    // getters and setters
}
