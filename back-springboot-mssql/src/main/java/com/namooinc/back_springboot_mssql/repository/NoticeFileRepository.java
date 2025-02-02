package com.namooinc.back_springboot_mssql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.namooinc.back_springboot_mssql.model.NoticeFile;

@Repository
public interface NoticeFileRepository extends JpaRepository<NoticeFile, Integer> {

}
