package com.namooinc.back_springboot_mssql.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.namooinc.back_springboot_mssql.model.Notice;
import com.namooinc.back_springboot_mssql.model.NoticeFile;
import com.namooinc.back_springboot_mssql.repository.NoticeFileRepository;

@Service
public class NoticeFileService {

    @Autowired
    private NoticeFileRepository noticeFileRepository;

    private AmazonS3 amazonS3;

    private String bucket = System.getenv("AWS_S3_BUCKET");

    public List<String> uploadFile(Notice savedNotice, MultipartFile[] files) {
        List<String> fileNameList = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            String savedFileName = generateSaveFilename(file.getOriginalFilename());

            NoticeFile noticeFile = NoticeFile.builder()
                    .originalName(file.getOriginalFilename())
                    .saveName(savedFileName)
                    .size(file.getSize())
                    .notice(savedNotice)
                    .build();

            noticeFileRepository.save(noticeFile);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, originalFilename, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            fileNameList.add(originalFilename);

        }
        return fileNameList;
    }

    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return uuid + "." + extension;
    }

}
