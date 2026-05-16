package com.Sadetechno.jwt_module.Service;
import com.Sadetechno.jwt_module.Repository.UsersRepo;
import com.Sadetechno.jwt_module.model.OurUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OurUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;
    @Override
    public OurUsers loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepo.findByEmail(username).orElseThrow();
    }

}
