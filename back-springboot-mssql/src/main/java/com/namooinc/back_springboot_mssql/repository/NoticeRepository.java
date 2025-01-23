package com.namooinc.back_springboot_mssql.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.namooinc.back_springboot_mssql.model.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findAll(Pageable pageable);

    Page<Notice> findByTitleContaining(Pageable pageable, String searchTerm);
}
