package com.sadetech.permission_module.Repository;

import com.sadetech.permission_module.Model.Permission;
import com.sadetech.permission_module.Model.PermissionRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    List<Permission> findByUserId(Long userId);

    Optional<Permission> findByUserIdAndPermission(Long userId, PermissionRequired permission);
}
