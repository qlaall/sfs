package com.github.qlaall.service;

import com.github.qlaall.config.BizException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FSAgent {
    /**
     * 持久化文件到文件系统,并返回文件的key
     *
     * @param inputStream readable
     * @param rootPath end with '/'
     * @param md5 length>2
     * @param size
     * @return fileKey(unique number)
     */
    public String saveToFS(InputStream inputStream, String rootPath, String md5, long size) {
        String fileKey = md5 + size;
        String p=md5.substring(0,2);
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(rootPath+ p+"/" + fileKey));
        } catch (IOException e) {
            throw new BizException("save fail");
        }
        return fileKey;
    }
}
