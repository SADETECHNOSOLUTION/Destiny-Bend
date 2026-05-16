package com.Sadetechno.like_module.DTO;

import com.Sadetechno.like_module.model.PostNotification;
import com.Sadetechno.like_module.model.StatusNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusNotificationDTO {

    private List<StatusNotification> notification;
    private int count;
}
