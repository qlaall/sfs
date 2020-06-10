package com.github.qlaall.controller;

import com.github.qlaall.entity.FileEntity;
import com.github.qlaall.repository.FileEntityRepository;
import com.github.qlaall.service.FileService;
import com.github.qlaall.util.Md5Util;
import com.github.qlaall.vo.FileDescribe;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * default fullPathName is root path
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping
    public FileDescribe uploadFile(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "fullPathName", required = false) String fullPathNameParam) throws IOException {
        long size = file.getSize();
        String md5 = Md5Util.md5(file.getInputStream());
        final String fullPathName;
        final String fileName;
        String fullPathNameNormalized = normalizefullPathName(fullPathNameParam);
        if (fullPathNameNormalized == null) {
            fileName = file.getOriginalFilename();
            fullPathName = "/" + fileName;
        } else {
            fileName = StringUtils.substringAfterLast(fullPathNameNormalized, "/");
            fullPathName = fullPathNameNormalized;
        }
        FileDescribe fileDescribe = fileService.saveFile(
                size,
                md5,
                fileName,
                fullPathName,
                file.getContentType(),
                file.getInputStream()
        );

        return fileDescribe;
    }

    /**
     * check fullPathName is Valid :
     * 1, it CANNOT end with '/'
     * 2, It uses '/' as the separator of the folder
     * <p>
     * correct：
     * /a/b/c/aa.txt
     * a/b/c/aa.txt (== /a/b/c/aa.txt )
     *
     * @param fullPathName
     */
    private String normalizefullPathName(String fullPathName) {
        if (StringUtils.isBlank(fullPathName)) {
            return null;
        }
        if (fullPathName.endsWith("/")) {
            throw new RuntimeException("the fullPathName CANNOT end with '/'");
        }
        if (fullPathName.contains("//")) {
            throw new RuntimeException("the fullPathName CANNOT contains '//'");
        }
        if (!fullPathName.startsWith("/")) {
            return "/" + fullPathName;
        } else {
            return fullPathName;
        }
    }

    @GetMapping
    public List<FileEntity> getAll() {
        return fileService.findAll();
    }
}
