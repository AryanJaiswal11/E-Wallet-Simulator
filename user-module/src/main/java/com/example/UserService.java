package com.example;


import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import com.example.AbstractUser.*;

@Service
public class UserService implements UserDetailsService {

    private static final String User_Topic = "user-created";

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KafkaTemplate<String, String>kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public com.example.AbstractUser CreateUser(UserDTO userDTO) {
        try {
            com.example.AbstractUser user = userDTO.to();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAuthorities("USER");
            this.userRepo.save(user);
            //todo: publish a kafka event to notify user has been created

            String data = this.objectMapper.writeValueAsString(user);
            this.kafkaTemplate.send(User_Topic, data);

            return user;
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username)
            throws UsernameNotFoundException {

        return this.userRepo.findByUsername(username);
    }


}
