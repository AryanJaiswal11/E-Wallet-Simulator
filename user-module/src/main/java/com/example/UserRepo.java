package com.example;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<AbstractUser,Integer> {

    AbstractUser findByUsername(String username);
}
