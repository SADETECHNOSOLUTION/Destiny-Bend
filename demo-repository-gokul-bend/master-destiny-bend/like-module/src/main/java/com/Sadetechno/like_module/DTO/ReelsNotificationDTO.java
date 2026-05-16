package com.Sadetechno.like_module.DTO;

import com.Sadetechno.like_module.model.ReelsNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReelsNotificationDTO {

    private List<ReelsNotification> notification;
    private int count;
}
