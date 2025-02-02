package com.namooinc.back_springboot_mssql.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.namooinc.back_springboot_mssql.dto.NoticeDTO.NoticeRequestDTO;
import com.namooinc.back_springboot_mssql.dto.NoticeDTO.NoticeResponseDTO;
import com.namooinc.back_springboot_mssql.model.Notice;
import com.namooinc.back_springboot_mssql.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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
    private NoticeService noticeService;

    @PostMapping("/create")
    public NoticeRequestDTO create(@RequestPart("data") NoticeRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        return noticeService.save(requestDTO, files);
    }

    @GetMapping("/read")
    public Page<NoticeResponseDTO> getNotices(@RequestParam int page, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return noticeService.list(page - 1);
        } else {
            return noticeService.searchList(page - 1, searchTerm);
        }
    }

    @GetMapping("/read/{id}")
    public NoticeResponseDTO get(@PathVariable int id) {
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
