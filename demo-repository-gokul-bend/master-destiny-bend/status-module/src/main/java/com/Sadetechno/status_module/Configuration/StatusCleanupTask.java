package com.Sadetechno.status_module.Configuration;

import com.Sadetechno.status_module.Repository.StatusRepository;
import com.Sadetechno.status_module.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatusCleanupTask {

    @Autowired
    private StatusRepository statusRepository;

    @Scheduled(fixedRate = 3600000) // Runs every hour (3600000 milliseconds)
    public void deleteOldStatuses() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24); // 24 hours ago
        List<Status> statusesToDelete = statusRepository.findByCreatedAtBefore(cutoff);
        statusRepository.deleteAll(statusesToDelete);
        System.out.println("Deleted " + statusesToDelete.size() + " old statuses");
    }
}
