package com.jerryMouse.controller;

import com.jerryMouse.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        String originalName = Objects.requireNonNullElse(file.getOriginalFilename(), "unknown");
        log.info("文件上传: {}", originalName);

        String extension = "";
        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = originalName.substring(dotIndex);
        }

        String fileName = UUID.randomUUID() + extension;
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);

        Path targetFile = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        return Result.success("/uploads/" + fileName);
    }
}
