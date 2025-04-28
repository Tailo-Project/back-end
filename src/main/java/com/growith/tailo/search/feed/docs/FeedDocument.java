package com.growith.tailo.search.feed.docs;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "search2")
public class FeedDocument {

    @Id
    @Field(type = FieldType.Long, name = "feed_post_id")
    private Long feedPostId;

    @Field(type = FieldType.Text, name = "account_id")
    private String accountId;

    @Field(type = FieldType.Text)
    private String nickname;

    @Field(type = FieldType.Text, name = "profile_image_url")
    private String profileImageUrl;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String hashtags;

    @Field(type = FieldType.Text, name = "image_urls")
    private String imageUrls;

    // localDateTime 으로 포매팅이 불가능하여 String으로 받음
    @Field(type = FieldType.Text, name = "created_at")
    private String createdAt;

    @Field(type = FieldType.Text, name = "updated_at")
    private String updatedAt;

}
