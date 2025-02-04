package com.namooinc.back_springboot_mssql.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.namooinc.back_springboot_mssql.dto.NoticeFileDTO.NoticeFileResponseDTO;
import com.namooinc.back_springboot_mssql.model.Notice;

import lombok.*;

public class NoticeDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeRequestDTO {
        private String title;

        private String content;

        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeResponseDTO {
        private int id;

        private String title;

        private String content;

        private LocalDateTime createdAt;

        private List<NoticeFileResponseDTO> files;

        public static NoticeResponseDTO from(Notice notice) {
            return NoticeResponseDTO.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .createdAt(notice.getCreatedAt())
                    .files(notice.getFiles().stream()
                            .map(file -> new NoticeFileResponseDTO(file.getId(), file.getOriginalName(),
                                    file.getSaveName()))
                            .collect(Collectors.toList()))
                    .build();
        }
    }
}
