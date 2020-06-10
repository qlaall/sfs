package com.github.qlaall.vo;

public class FileDescribe {
    /**
     * etag 在这个项目中作为文件唯一标识 一般由md5+文件大小组合而成
     */
    private String etag;
    /**
     * 文件md5
     */
    private String md5;

    /**
     * 程序猜测出的contentType
     */
    private String contentType;
    /**
     * 原始文件名
     */
    private String fileName;
    private String fullPathName;

    public String getEtag() {
        return etag;
    }

    public String getFullPathName() {
        return fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
