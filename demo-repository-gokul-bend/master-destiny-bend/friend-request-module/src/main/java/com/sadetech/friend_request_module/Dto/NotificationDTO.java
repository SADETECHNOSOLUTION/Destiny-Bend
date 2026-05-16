package com.sadetech.friend_request_module.Dto;

import com.sadetech.friend_request_module.Model.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private List<Notification> notification;
    private int count;
}
