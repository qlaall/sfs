package com.github.qlaall.service;

import com.github.qlaall.entity.FileEntity;
import com.github.qlaall.entity.PathNodeEntity;
import com.github.qlaall.repository.FileEntityRepository;
import com.github.qlaall.repository.PathNodeRepository;
import com.github.qlaall.util.PathUtils;
import com.github.qlaall.vo.FileDescribe;
import com.github.qlaall.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    @Autowired
    PathNodeRepository pathNodeRepository;


    /**
     * 保存文件
     *
     * @param size        文件大小 long
     * @param md5         md5值
     * @param fileName    文件名 /game/ra3/ra3.exe: ra3.exe
     * @param path        全路径的Path,不包含文件名，以"/"开头或""
     *                    /game/ra3/ra3.exe -> "/game/ra3"
     *                    /ra3.exe -> ""
     *                    /game/bamboo.exe -> "/game"
     * @param contentType
     * @param inputStream 输入流
     */
    @Transactional
    public FileDescribe saveFile(long size, String md5, String fileName, String path, String contentType, InputStream inputStream) throws IOException {
        FSAgent fsAgent = new FSAgent();
        String fileKey = fsAgent.saveToFS(inputStream, rootPath, md5, size);
        String fullPathName = path + "/" + fileName;
        updateNewPath(path);
        FileEntity fe = new FileEntity();
        fe.setKey(fileKey);
        fe.setContentType(contentType);
        fe.setFileName(fileName);
        fe.setFullPathName(fullPathName);
        fe.setMd5(md5);
        fe.setCreateTime(OffsetDateTime.now());
        fe.setFileSize(size);
        fe.setPathDepth(StringUtils.countMatches(fullPathName, '/'));
        fileEntityRepository.save(fe);
        return fe2Fd(fe);
    }

    /**
     * 根据fullPathName更新pathNode节点(增加新节点)
     *
     * @param path
     */
    private void updateNewPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        List<PathNodeEntity> split = PathUtils.split(path);
        for (PathNodeEntity pne : split) {
            pathNodeRepository.save(pne);
        }
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
    /**
     * 根据parentPath来列出目录
     *
     * @param parentPath 所在目录，目录以/结尾。根目录是'/'
     * @param pageSize
     * @param pageNum 页数，从1开始。
     * @return
     */
    public PageVo<PathNodeEntity> listPaths(@NonNull String parentPath, @NonNull Integer pageSize, @NonNull Integer pageNum){
        PathNodeEntity pathKey=new PathNodeEntity();
        pathKey.setParentPath(parentPath);
        Page<PathNodeEntity> all = pathNodeRepository.findAll(Example.of(pathKey), PageRequest.of(pageNum-1, pageSize));
        return toPageVo(all);
    }
    /**
     * 根据parentPath来列出文件
     *
     * @param parentPath 所在目录，目录以/结尾。根目录是'/'
     * @param pageSize
     * @param pageNum 页数，从1开始。
     * @return
     */
    public PageVo<FileEntity> listFiles(@NonNull String parentPath, @NonNull Integer pageSize, @NonNull Integer pageNum) {
        Page<FileEntity> fileEntities = fileEntityRepository.listFiles(
                parentPath,
                StringUtils.countMatches(parentPath, "/"),
                PageRequest.of(pageNum-1, pageSize));

        return toPageVo(fileEntities);

    }

    public List<PathNodeEntity> findAllPath() {
        return pathNodeRepository.findAll();
    }


    private<T> PageVo<T> toPageVo(Page<T> pageData){
        PageVo<T> p= new PageVo<>();
        p.setData(pageData.getContent());
        p.setPageNum(pageData.getPageable().getPageNumber()+1);
        p.setPageSize(pageData.getPageable().getPageSize());
        p.setTotal(pageData.getTotalElements());
        p.setPages(pageData.getTotalPages());
        return p;
    }
}
