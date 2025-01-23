package com.namooinc.back_springboot_mssql.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.namooinc.back_springboot_mssql.model.Notice;
import com.namooinc.back_springboot_mssql.repository.NoticeRepository;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    // 페이지네이션을 위한 리스트 반환
    public Page<Notice> list(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return noticeRepository.findAll(pageable);
    }

    // 제목을 검색어로 필터링한 리스트 반환
    public Page<Notice> searchList(int page, String searchTerm) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return noticeRepository.findByTitleContaining(pageable, searchTerm);
    }

    public Notice get(int id) {
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        if (noticeOptional.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Notice not Found");
        }
        return noticeOptional.get();
    }

    public Notice update(int id, Notice notice) {
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        if (noticeOptional.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Notice not Found");
        }
        Notice existingNotice = noticeOptional.get();
        existingNotice.setTitle(notice.getTitle());
        existingNotice.setContent(notice.getContent());
        return noticeRepository.save(existingNotice);
    }

    public void delete(int id) {
        noticeRepository.deleteById(id);
    }

}
