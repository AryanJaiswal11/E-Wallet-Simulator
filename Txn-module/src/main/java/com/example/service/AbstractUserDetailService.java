package com.example.service;


import com.example.Model.UserResponseModel;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AbstractUserDetailService implements UserDetailsService {
    RestTemplate restTemplate = new RestTemplate();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            //todo:make api call to user-module
            ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(
                    "http://localhost:8120/users/internal/" + username,
                    JSONObject.class);

            JSONObject data = responseEntity.getBody();
            System.out.println("Abstract User Details JSON: "+data);
            List<?> authoritiesData = (List<?>) data.get("authorities");

            List<SimpleGrantedAuthority> authorities =
                    authoritiesData.stream()
                            .map(obj -> {
                                var map = (java.util.Map<?, ?>) obj;
                                return new SimpleGrantedAuthority(
                                        map.get("authority").toString()
                                );
                            })
                            .toList();
            UserResponseModel userResponseModel = UserResponseModel.builder()
                    .UserId((Integer) data.get("id"))
                    .username(data.get("username").toString())
                    .password(data.get("password").toString())
                    .grantedAuthorities((List<GrantedAuthority>)(List<?>) authorities)
                    .build();

            return userResponseModel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
