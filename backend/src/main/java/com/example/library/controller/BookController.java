package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.mapper.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // 查询所有
    @GetMapping("/list")
    public List<Book> list() {
        return bookRepository.findAll();
    }

    // 条件查询（书名/作者/ISBN）
    @GetMapping("/search")
    public List<Book> search(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingOrAuthorContainingOrIsbnContaining(
                keyword, keyword, keyword);
    }

    // 根据ID查询（修改时回填用）
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent()) {
            result.put("success", false);
            result.put("message", "图书不存在");
            return result;
        }

        result.put("success", true);
        result.put("data", book.get());
        return result;
    }

    // 添加（带参数校验）
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Book book) {
        Map<String, Object> result = new HashMap<>();

        // 参数校验
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "书名不能为空");
            return result;
        }
        if (book.getIsbn() != null && !book.getIsbn().matches("\\d+")) {
            result.put("success", false);
            result.put("message", "ISBN必须为纯数字");
            return result;
        }
        if (book.getStock() == null || book.getStock() < 0) {
            result.put("success", false);
            result.put("message", "库存不能为负数");
            return result;
        }

        bookRepository.save(book);
        result.put("success", true);
        result.put("message", "添加成功");
        return result;
    }

    // 修改（带参数校验）
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Book book) {
        Map<String, Object> result = new HashMap<>();

        if (book == null || book.getId() == null) {
            result.put("success", false);
            result.put("message", "ID不能为空");
            return result;
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "书名不能为空");
            return result;
        }
        if (book.getIsbn() != null && !book.getIsbn().matches("\\d+")) {
            result.put("success", false);
            result.put("message", "ISBN必须为纯数字");
            return result;
        }
        if (book.getStock() == null || book.getStock() < 0) {
            result.put("success", false);
            result.put("message", "库存不能为负数");
            return result;
        }

        // 检查图书是否存在
        Optional<Book> existing = bookRepository.findById(book.getId());
        if (!existing.isPresent()) {
            result.put("success", false);
            result.put("message", "图书不存在，无法修改");
            return result;
        }

        bookRepository.save(book);
        result.put("success", true);
        result.put("message", "修改成功");
        return result;
    }

    // 删除
    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();

        Optional<Book> book = bookRepository.findById(id);
        if (!book.isPresent()) {
            result.put("success", false);
            result.put("message", "图书不存在，无法删除");
            return result;
        }

        bookRepository.deleteById(id);
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }
}