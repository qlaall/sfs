package com.github.qlaall.service;

import com.github.qlaall.entity.FileEntity;
import com.github.qlaall.repository.FileEntityRepository;
import com.github.qlaall.vo.FileDescribe;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class FileService {
    @Value("${external.mount}")
    String rootPath;
    @Autowired
    FileEntityRepository fileEntityRepository;
    /**
     * 保存文件
     * @param size  文件大小 long
     * @param md5   md5值
     * @param originalFilename  源文件名
     * @param fullPathName  全路径文件名,以"/"开头
     * @param contentType
     * @param inputStream   输入流
     */
    public FileDescribe saveFile(long size, String md5, String originalFilename, String fullPathName, String contentType, InputStream inputStream) throws IOException {
        String fileKey = md5 + size;
        FileUtils.copyInputStreamToFile(inputStream,new File(rootPath+fileKey));
        FileEntity fe = new FileEntity();
        fe.setKey(fileKey);
        fe.setContentType(contentType);
        fe.setFileName(originalFilename);
        fe.setFullPathName(fullPathName);
        fe.setMd5(md5);
        fe.setCreateTime(OffsetDateTime.now());
        fileEntityRepository.save(fe);
        return fe2Fd(fe);

    }

    private FileDescribe fe2Fd(FileEntity fe) {
        FileDescribe fileDescribe = new FileDescribe();

        fileDescribe.setEtag(fe.getKey());
        fileDescribe.setContentType(fe.getContentType());
        fileDescribe.setMd5(fe.getMd5());
        fileDescribe.setFileName(fe.getFileName());
        fileDescribe.setFullPathName(fe.getFullPathName());
        return fileDescribe;

    }

    public List<FileEntity> findAll() {

        return fileEntityRepository.findAll();
    }
}
