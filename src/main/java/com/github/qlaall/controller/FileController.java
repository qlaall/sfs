package com.github.qlaall.controller;

import com.github.qlaall.entity.FileEntity;
import com.github.qlaall.repository.FileEntityRepository;
import com.github.qlaall.service.FileService;
import com.github.qlaall.util.Md5Util;
import com.github.qlaall.vo.FileDescribe;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Api
@RequestMapping("/file")
@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping
    public FileDescribe uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        long size = file.getSize();
        String md5 = Md5Util.md5(file.getInputStream());

        FileDescribe fileDescribe = fileService.saveFile(size, md5, file.getOriginalFilename(), file.getContentType(), file.getInputStream());

        return fileDescribe;
    }
    @GetMapping
    public List<FileEntity> getAll(){
        return fileService.findAll();
    }
}
