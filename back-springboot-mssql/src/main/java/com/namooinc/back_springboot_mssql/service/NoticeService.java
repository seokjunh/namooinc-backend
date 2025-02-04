package com.namooinc.back_springboot_mssql.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.namooinc.back_springboot_mssql.dto.NoticeDTO.NoticeRequestDTO;
import com.namooinc.back_springboot_mssql.dto.NoticeDTO.NoticeResponseDTO;
import com.namooinc.back_springboot_mssql.model.Notice;
import com.namooinc.back_springboot_mssql.model.NoticeFile;
import com.namooinc.back_springboot_mssql.repository.NoticeFileRepository;
import com.namooinc.back_springboot_mssql.repository.NoticeRepository;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeFileService noticeFileService;

    @Autowired
    private NoticeFileRepository noticeFileRepository;

    public void save(NoticeRequestDTO requestDTO, MultipartFile[] files) {
        Notice notice = Notice.builder().title(requestDTO.getTitle()).content(requestDTO.getContent())
                .createdAt(requestDTO.getCreatedAt()).build();

        Notice savedNotice = noticeRepository.save(notice);

        if (files != null && files.length > 0) {
            noticeFileService.uploadFile(savedNotice, files);
        }

    }

    public Page<NoticeResponseDTO> list(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        return noticePage.map(notice -> NoticeResponseDTO.from(notice));
    }

    public Page<NoticeResponseDTO> searchList(int page, String searchTerm) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Notice> noticePage = noticeRepository.findByTitleContaining(pageable, searchTerm);

        return noticePage.map(notice -> NoticeResponseDTO.from(notice));
    }

    public NoticeResponseDTO get(int id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Notice not Found"));

        return NoticeResponseDTO.from(notice);
    }

    public void update(int id, NoticeRequestDTO requestDTO, MultipartFile[] files) {
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        List<NoticeFile> savedFiles = noticeFileRepository.findByNotice_Id(id);

        if (noticeOptional.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Notice not Found");
        } else {

            Notice existingNotice = noticeOptional.get();

            existingNotice.setTitle(requestDTO.getTitle());
            existingNotice.setContent(requestDTO.getContent());

            Notice patchedNotice = noticeRepository.save(existingNotice);

            for (NoticeFile file: savedFiles) {
                noticeFileRepository.delete(file);
            }

            if (files != null && files.length > 0) {
                noticeFileService.uploadFile(patchedNotice, files);
            }
        }
    }

    public void delete(int id) {
        noticeRepository.deleteById(id);
    }
}
