package com.github.qlaall.repository;

import com.github.qlaall.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileEntityRepository extends JpaRepository<FileEntity,String> {
    @Query(value = "select * from t_file_entity WHERE full_path_name like :parentPath% AND path_depth = :depth",nativeQuery = true)
    Page<FileEntity> listFiles(@Param("parentPath")String parentPath, @Param("depth")Integer depth, Pageable pageable);
}
