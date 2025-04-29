package com.growith.tailo.search.member.docs;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = "search1")
public class MemberDocument {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, name = "account_id")
    private String accountId;

    @Field(type = FieldType.Text)
    private String nickname;

    @Field(type = FieldType.Text, name = "profile_image_url")
    private String profileImageUrl;

    // localDateTime 으로 포매팅이 불가능하여 String으로 받음
    @Field(type = FieldType.Text, name = "created_at")
    private String createdAt;

    @Field(type = FieldType.Text, name = "updated_at")
    private String updatedAt;

}
