package com.sadetech.settings.repository;

import com.sadetech.settings.model.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings,Integer> {
}
