package com.github.qlaall.repository;

import com.github.qlaall.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<FileEntity,String> {
}
