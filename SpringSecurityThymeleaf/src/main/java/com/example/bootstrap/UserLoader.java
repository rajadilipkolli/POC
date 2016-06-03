package com.example.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@AllArgsConstructor
public class UserLoader implements ApplicationListener<ContextRefreshedEvent> {
    
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("admin");
        user.setPassword(new BCryptPasswordEncoder().encode("admin123"));
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        log.debug("Saved User Id {}",user.getId());
        User user1 = new User();
        user1.setUsername("user");
        user1.setPassword(new BCryptPasswordEncoder().encode("user123"));
        user1.setRole(Role.USER);
        userRepository.save(user1);
        log.debug("Saved User Id {}",user1.getId());
        
    }

}
