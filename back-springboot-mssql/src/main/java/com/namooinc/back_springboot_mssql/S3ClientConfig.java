package com.namooinc.back_springboot_mssql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3ClientConfig {

    @Bean
    S3Client s3Client() {
        return S3Client.builder().region(Region.AP_NORTHEAST_2).credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

}
