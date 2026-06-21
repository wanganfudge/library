package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.User;
import com.example.library.mapper.repository.BookRepository;
import com.example.library.mapper.repository.BorrowRecordRepository;
import com.example.library.mapper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    // ========== 借书（带事务和参数校验） ==========
    @Transactional
    @PostMapping("/{userId}/{bookId}")
    public Map<String, Object> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 检查用户
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }
            User user = userOpt.get();

            // 2. 检查用户已借数量
            long currentBorrows = borrowRecordRepository.findByUserId(userId)
                    .stream().filter(r -> r.getStatus() == 0).count();
            if (currentBorrows >= user.getMaxBorrow()) {
                result.put("success", false);
                result.put("message", "已达到最大借阅数量：" + user.getMaxBorrow());
                return result;
            }

            // 3. 检查图书
            Optional<Book> bookOpt = bookRepository.findById(bookId);
            if (!bookOpt.isPresent()) {
                result.put("success", false);
                result.put("message", "图书不存在");
                return result;
            }
            Book book = bookOpt.get();
            if (book.getStock() <= 0) {
                result.put("success", false);
                result.put("message", "图书库存不足");
                return result;
            }

            // 4. 扣减库存 + 创建记录（事务保证一致性）
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);

            BorrowRecord record = new BorrowRecord();
            record.setUserId(userId);
            record.setBookId(bookId);
            record.setBorrowDate(LocalDate.now());
            record.setDueDate(LocalDate.now().plusDays(30));
            record.setStatus(0);
            borrowRecordRepository.save(record);

            result.put("success", true);
            result.put("message", "借书成功，应还日期：" + record.getDueDate());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "借书失败：" + e.getMessage());
            throw e; // 抛出让事务回滚
        }

        return result;
    }

    // ========== 还书（带事务） ==========
    @Transactional
    @PostMapping("/return/{recordId}")
    public Map<String, Object> returnBook(@PathVariable Long recordId) {
        Map<String, Object> result = new HashMap<>();

        Optional<BorrowRecord> recordOpt = borrowRecordRepository.findById(recordId);
        if (!recordOpt.isPresent()) {
            result.put("success", false);
            result.put("message", "记录不存在");
            return result;
        }

        BorrowRecord record = recordOpt.get();
        if (record.getStatus() != 0) {
            result.put("success", false);
            result.put("message", "该书已归还");
            return result;
        }

        try {
            // 1. 更新记录
            record.setReturnDate(LocalDate.now());
            boolean isOverdue = record.getDueDate().isBefore(LocalDate.now());
            record.setStatus(isOverdue ? 2 : 1); // 2=逾期
            borrowRecordRepository.save(record);

            // 2. 恢复库存
            Optional<Book> bookOpt = bookRepository.findById(record.getBookId());
            if (bookOpt.isPresent()) {
                Book book = bookOpt.get();
                book.setStock(book.getStock() + 1);
                bookRepository.save(book);
            }

            result.put("success", true);
            result.put("message", isOverdue ? "归还成功（已逾期）" : "归还成功");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "还书失败：" + e.getMessage());
            throw e;
        }

        return result;
    }

    // ========== 查询全部（带用户名和书名） ==========
    @GetMapping("/list")
    public List<Map<String, Object>> list() {
        List<BorrowRecord> records = borrowRecordRepository.findAll();
        return records.stream().map(this::convertToMap).toList();
    }

    // ========== 查询某用户（带用户名和书名） ==========
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> findByUser(@PathVariable Long userId) {
        List<BorrowRecord> records = borrowRecordRepository.findByUserId(userId);
        return records.stream().map(this::convertToMap).toList();
    }

    // 把记录转成 Map，并回填用户名和书名
    private Map<String, Object> convertToMap(BorrowRecord r) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", r.getId());
        map.put("userId", r.getUserId());
        map.put("bookId", r.getBookId());
        map.put("borrowDate", r.getBorrowDate());
        map.put("dueDate", r.getDueDate());
        map.put("returnDate", r.getReturnDate());
        map.put("status", r.getStatus());

        Optional<User> user = userRepository.findById(r.getUserId());
        Optional<Book> book = bookRepository.findById(r.getBookId());
        map.put("userName", user.map(User::getRealName).orElse("未知用户"));
        map.put("bookTitle", book.map(Book::getTitle).orElse("未知图书"));

        return map;
    }
}