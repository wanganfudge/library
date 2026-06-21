package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;           // 图书编号（自动生成）

    private String title;      // 书名
    private String author;     // 作者
    private String isbn;       // ISBN
    private String publisher;  // 出版社
    private Integer publishYear; // 出版年份
    private Integer stock;     // 库存数量

    private LocalDateTime createTime = LocalDateTime.now();
}