package com.namooinc.back_springboot_mssql.controller;

import org.springframework.web.bind.annotation.RestController;

import com.namooinc.back_springboot_mssql.model.Notice;
import com.namooinc.back_springboot_mssql.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @PostMapping("/create")
    public Notice create(@RequestBody Notice notice) {
        return noticeService.save(notice);
    }

    // 페이지네이션 및 검색을 처리하는 GET 메서드
    @GetMapping("/read")
    public Page<Notice> getNotices(@RequestParam int page, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return noticeService.list(page - 1); // 검색어가 없으면 모든 공지사항 반환
        } else {
            return noticeService.searchList(page - 1, searchTerm); // 검색어가 있으면 제목에 포함된 공지사항 반환
        }
    }

    @GetMapping("/read/{id}")
    public Notice get(@PathVariable int id) {
        return noticeService.get(id);
    }

    @PatchMapping("/{id}")
    public Notice update(@PathVariable int id, @RequestBody Notice notice) {
        return noticeService.update(id, notice);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        noticeService.delete(id);
    }

}
