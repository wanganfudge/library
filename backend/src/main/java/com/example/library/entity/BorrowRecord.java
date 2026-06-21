package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "borrow_record")
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;           // 借阅编号

    private Long userId;       // 用户编号
    private Long bookId;       // 图书编号
    private LocalDate borrowDate;   // 借阅日期
    private LocalDate dueDate;      // 应还日期
    private LocalDate returnDate;   // 实际归还日期（可为空）
    private Integer status;    // 0:借阅中 1:已归还 2:逾期

    @Transient  // 不存数据库，查询时回填
    private String userName;

    @Transient
    private String bookTitle;

    private LocalDateTime createTime = LocalDateTime.now();
}