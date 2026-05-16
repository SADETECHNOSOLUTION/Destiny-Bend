package com.example.follow_module.DTO;

import com.example.follow_module.Model.Notification;
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
