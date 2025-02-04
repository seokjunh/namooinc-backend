package com.namooinc.back_springboot_mssql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.namooinc.back_springboot_mssql.service.NoticeFileService;

@RestController
@RequestMapping("/files")
public class NoticeFileController {

    @Autowired
    private NoticeFileService noticeFileService;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String savedFileName) {
        return noticeFileService.downloadFile(savedFileName);
    }
}
