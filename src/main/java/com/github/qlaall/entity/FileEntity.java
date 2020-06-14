package com.github.qlaall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "t_file_entity")
public class FileEntity {
    @Id
    private String key;
    private String md5;
    /**
     * myName.txt
     */
    private String fileName;
    /**
     * such as /a/b/c/myName.txt
     */
    private String fullPathName;
    private String contentType;
    private OffsetDateTime createTime;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * this is the depth of path:
     * /a.txt : 1
     * /a/b.txt : 2
     * /a/b/c.txt : 3
     */
    private Integer pathDepth;
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMd5() {
        return md5;
    }

    public String getFullPathName() {
        return fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getPathDepth() {
        return pathDepth;
    }

    public void setPathDepth(Integer pathDepth) {
        this.pathDepth = pathDepth;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public OffsetDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(OffsetDateTime createTime) {
        this.createTime = createTime;
    }
}
