package com.sadetech.admin_login.Service;
import com.sadetech.admin_login.Repository.AdminRepo;
import com.sadetech.admin_login.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminRepo usersRepo;
    @Override
    public AdminUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepo.findByEmail(username).orElseThrow(IllegalArgumentException::new);
    }
}
