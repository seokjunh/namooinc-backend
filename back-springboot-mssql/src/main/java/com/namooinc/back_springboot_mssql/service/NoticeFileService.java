package com.namooinc.back_springboot_mssql.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.namooinc.back_springboot_mssql.model.Notice;
import com.namooinc.back_springboot_mssql.model.NoticeFile;
import com.namooinc.back_springboot_mssql.repository.NoticeFileRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class NoticeFileService {

    @Autowired
    private NoticeFileRepository noticeFileRepository;

    @Autowired
    private S3Client s3Client;

    private static final String BUCKET_NAME = "namoobucket";

    public void uploadFile(Notice savedNotice, MultipartFile[] files) {

        for (MultipartFile file : files) {

            String originalFilename = file.getOriginalFilename();

            String savedFileName = generateSaveFilename(file.getOriginalFilename());

            NoticeFile noticeFile = NoticeFile.builder()
                    .originalName(originalFilename)
                    .saveName(savedFileName)
                    .size(file.getSize())
                    .notice(savedNotice)
                    .build();

            noticeFileRepository.save(noticeFile);

            uploadFileToS3(file, savedFileName);

        }
    }

    private String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return uuid + "." + extension;
    }

    private void uploadFileToS3(MultipartFile file, String savedFileName) {
        try {

            byte[] bytes = file.getBytes();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(savedFileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

        } catch (IOException e) {

            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public ResponseEntity<InputStreamResource> downloadFile(String savedFileName) {

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(savedFileName)
                    .build();

            InputStream imageStream = s3Client.getObject(getObjectRequest);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + savedFileName + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(imageStream));

        } catch (S3Exception e) {

            System.err.println("Error occurred while downloading from S3: " + e.awsErrorDetails().errorMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
