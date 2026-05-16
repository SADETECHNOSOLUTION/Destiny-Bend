package com.sadetech.profession.model.sub_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "artist_social_media_account")
public class SocialMediaAccounts {
    @Id
    private String id;
    private Long userId;
    private List<Map<String,String>> platforms;
}
