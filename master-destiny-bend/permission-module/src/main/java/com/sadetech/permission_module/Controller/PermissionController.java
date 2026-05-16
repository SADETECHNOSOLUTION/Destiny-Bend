package com.sadetech.permission_module.Controller;

import com.sadetech.permission_module.Model.Permission;
import com.sadetech.permission_module.Model.PermissionRequired;
import com.sadetech.permission_module.Service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/grant")
    public ResponseEntity<String> createPermission(@RequestBody Permission permission) {
        try {
            String resultMessage = permissionService.grantPermission(permission);

            // Check the message to determine the response status
            if (resultMessage.contains("already")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(resultMessage);
            } else if (resultMessage.contains("updated")) {
                return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(resultMessage);
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input provided.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Permission>updateStatus(@PathVariable Long id, @RequestParam boolean status){
        try{
            Permission permission = permissionService.updateStatus(id,status);
            return ResponseEntity.status(200).body(permission);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/get-status/{userId}")
    public ResponseEntity<List<Permission>> getAllPermission(@PathVariable Long userId){
        try {
            List<Permission> permissions = permissionService.getAllPermissionByUserId(userId);
            return ResponseEntity.ok(permissions);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
