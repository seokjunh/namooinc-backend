package com.namooinc.back_springboot_mssql.dto;

import java.time.LocalDateTime;

import lombok.*;

public class NoticeFileDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeFileRequestDTO {
        private String originalName;

        private String saveName;

        private Long size;

        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeFileResponseDTO {

        private int id;

        private String originalName;

        private String saveName;

    }

}
