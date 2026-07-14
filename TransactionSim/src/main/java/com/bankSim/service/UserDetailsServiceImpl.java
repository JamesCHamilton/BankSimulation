package com.bankSim.service;

import com.bankSim.model.User;
import com.bankSim.repos.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We support both email and username login. Let's see if we can find by email first.
        User user = userRepository.findByEmail(username);
        if (user == null) {
            // Also let's try finding by userName if we can, but since UserRepository doesn't have findByUserName,
            // let's scan all users to see if any has this username as a fallback.
            user = userRepository.findAll().stream()
                    .filter(u -> u.getUserName().equalsIgnoreCase(username))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + username));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
