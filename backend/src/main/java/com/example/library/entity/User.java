package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,length = 30)
    private String username;
    private String password;
    private String realName;
    private Integer maxBorrow;
    private LocalDateTime createTime = LocalDateTime.now();
}