package com.sadetech.permission_module.Service;

import com.sadetech.permission_module.Model.Permission;
import com.sadetech.permission_module.Model.PermissionRequired;
import com.sadetech.permission_module.Repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


    public String grantPermission(Permission permissionRequest) {
            Optional<Permission> existingPermissionOpt = permissionRepository.findByUserIdAndPermission(
                    permissionRequest.getUserId(),
                    permissionRequest.getPermission()
            );

            if (existingPermissionOpt.isPresent()) {
                Permission existingPermission = existingPermissionOpt.get();

                // Check if the permission status is already as requested
                if (existingPermission.isStatus() == permissionRequest.isStatus()) {
                    return "Permission already " + (existingPermission.isStatus() ? "granted" : "restricted") + ".";
                }

                // Update the status if the request is different
                existingPermission.setStatus(permissionRequest.isStatus());
                permissionRepository.save(existingPermission);
                return "Permission status updated to " + (permissionRequest.isStatus() ? "granted" : "restricted") + ".";
            }

            // If permission does not exist, create a new one
            Permission newPermission = new Permission();
            newPermission.setUserId(permissionRequest.getUserId());
            newPermission.setPermission(permissionRequest.getPermission());
            newPermission.setStatus(permissionRequest.isStatus());

            permissionRepository.save(newPermission);
            return "New permission " + (permissionRequest.isStatus() ? "granted" : "restricted") + ".";
    }

    public Permission updateStatus(Long id, boolean status){
        Optional<Permission> permission = permissionRepository.findById(id);

        if(permission.isPresent()){
            Permission permission1 = permission.get();
            permission1.setStatus(status);
            return permissionRepository.save(permission1);
        }else {
            throw new IllegalArgumentException("Id not found");
        }
    }

    public List<Permission> getAllPermissionByUserId(Long userId){
        return permissionRepository.findByUserId(userId);
    }
}
