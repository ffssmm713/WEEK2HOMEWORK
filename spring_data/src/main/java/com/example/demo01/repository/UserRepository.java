package com.example.demo01.repository;


import com.example.demo01.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByUserNameContaining(String userName, Pageable pageable);
}


