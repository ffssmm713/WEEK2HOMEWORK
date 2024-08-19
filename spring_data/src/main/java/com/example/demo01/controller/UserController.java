package com.example.demo01.controller;

import com.example.demo01.entity.User;
import com.example.demo01.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


  /*  @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<User> usersPage;
        if (userName == null || userName.trim().isEmpty()) {
            // 如果用户名为空，则返回所有用户
            usersPage = userService.getUsers("", pageNum, pageSize);
        } else {
            usersPage = userService.getUsers(userName, pageNum, pageSize);
        }
        return ResponseEntity.ok(usersPage);
    }*/
  @GetMapping
  public ResponseEntity<Page<User>> getUsers(
          @RequestParam(value = "userName", required = false) String userName,
          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
          @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
          @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {

      Page<User> usersPage = userService.getUsers(userName, pageNum, pageSize, sortBy, sortOrder);
      return ResponseEntity.ok(usersPage);
  }




    /*// 分页获取用户列表
    @GetMapping
    public ResponseEntity<Page<User>> getUserList(
            @RequestParam(defaultValue = "0") int page,  // 页码，默认从 0 开始
            @RequestParam(defaultValue = "10") int size // 每页显示条数，默认 10 条
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.getUserList(pageable);
        return ResponseEntity.ok(userPage);
    }*/

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
