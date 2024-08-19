package com.example.demo01.service;



import com.example.demo01.entity.User;
import com.example.demo01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

/*    public Page<User> getUserList(Pageable pageable){
        return userRepository.findAll(pageable);
    }*/

    public Page<User> getUsers(String userName, int pageNum, int pageSize, String sortBy, String sortOrder) {
        Pageable pageable;
        if ("desc".equalsIgnoreCase(sortOrder)) {
            pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(sortBy).descending());
        } else {
            pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(sortBy).ascending());
        }

        if (userName == null || userName.trim().isEmpty()) {
            return userRepository.findAll(pageable);
        } else {
            return userRepository.findByUserNameContaining(userName, pageable);
        }
    }

    /*public Page<User> getUsers(String userName, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        if (userName == null || userName.trim().isEmpty()) {
            return userRepository.findAll(pageable); // 返回所有用户
        } else {
            return userRepository.findByUserNameContaining(userName, pageable); // 模糊查询
        }
    }*/

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}


