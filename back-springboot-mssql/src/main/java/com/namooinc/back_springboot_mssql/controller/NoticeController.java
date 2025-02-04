package com.namooinc.back_springboot_mssql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.namooinc.back_springboot_mssql.dto.NoticeDTO.*;
import com.namooinc.back_springboot_mssql.service.NoticeService;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/create")
    public void create(@RequestPart("data") NoticeRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        noticeService.save(requestDTO, files);
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
    public void update(@PathVariable int id, @RequestPart("data") NoticeRequestDTO requestDTO,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {
        noticeService.update(id, requestDTO, files);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        noticeService.delete(id);
    }

}
