package com.example.library.mapper.repository;

import com.example.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    // 查某用户的所有借阅
    List<BorrowRecord> findByUserId(Long userId);
}