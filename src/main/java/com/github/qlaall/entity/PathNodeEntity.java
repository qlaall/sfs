package com.github.qlaall.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "t_path_node_entity", uniqueConstraints = {@UniqueConstraint(columnNames = {"parentPath", "name"})})
public class PathNodeEntity {
    @Id
    private String fullPathName;
    /**
     * 父级目录,目录以'/'结尾:
     * "/"
     * "/a/"
     * "/a/b/"
     */
    private String parentPath;
    /**
     * 本级文件夹名
     */
    private String name;

    public String getFullPathName() {
        return fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
