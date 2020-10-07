package main.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import main.config.SecurityConfiguration;
import main.model.User;
import main.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;


    @Autowired
    SecurityConfiguration securityConfiguration;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user " + username + " not found!"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword()
                , new ArrayList<>());
    }

    public boolean login(String username, String password) {
        boolean answer = false;
        Optional<User> userByUsername = userRepo.findByEmail(username);
        if (userByUsername.isPresent() && securityConfiguration.bcryptPasswordEncoder()
                .matches(password, userByUsername.get().getPassword())) {
            answer = true;
        }
        return answer;
    }
}