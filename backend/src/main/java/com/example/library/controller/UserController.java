package com.example.library.controller;

import com.example.library.entity.User;
import com.example.library.mapper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<User> list() {
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public String add(@RequestBody User user) {
        userRepository.save(user);
        return "添加用户成功";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "删除用户成功";
    }
}