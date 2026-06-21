package com.example.library.mapper.repository;

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    // 按书名、作者、ISBN模糊查询
    List<Book> findByTitleContainingOrAuthorContainingOrIsbnContaining(
            String title, String author, String isbn);
}