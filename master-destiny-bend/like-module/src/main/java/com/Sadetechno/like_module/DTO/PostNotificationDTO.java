package com.Sadetechno.like_module.DTO;

import com.Sadetechno.like_module.model.PostNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostNotificationDTO {
    private List<PostNotification> notification;
    private int count;
}
