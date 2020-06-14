package com.github.qlaall.controller;

import com.github.qlaall.config.BizException;
import com.github.qlaall.entity.FileEntity;
import com.github.qlaall.entity.PathNodeEntity;
import com.github.qlaall.service.FileService;
import com.github.qlaall.util.Md5Util;
import com.github.qlaall.vo.FileDescribe;
import com.github.qlaall.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        final String path;
        final String fileName;
        String fullPathNameNormalized = normalizefullPathName(fullPathNameParam);
        if (fullPathNameNormalized == null) {
            fileName = file.getOriginalFilename();
            path = "" ;
        } else {
            fileName = StringUtils.substringAfterLast(fullPathNameNormalized, "/");
            path = StringUtils.substringBeforeLast(fullPathNameNormalized, "/");
        }
        if (StringUtils.isBlank(fileName)){
            throw new BizException("the fileName CANNOT Blank , Please enter a legal fileName in the param 'fullPathName'");
        }
        if(fileName.contains("/")) {
            throw new BizException("the fileName CANNOT contains '/' , Please enter a legal fileName in the param 'fullPathName'");
        }

        FileDescribe fileDescribe = fileService.saveFile(
                size,
                md5,
                fileName,
                path,
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
            throw new BizException("the fullPathName CANNOT end with '/'");
        }
        if (fullPathName.contains("//")) {
            throw new BizException("the fullPathName CANNOT contains '//'");
        }
        if (!fullPathName.startsWith("/")) {
            return "/" + fullPathName;
        } else {
            return fullPathName;
        }
    }

    @GetMapping("files")
    public List<FileEntity> getAll() {
        return fileService.findAll();
    }
    @GetMapping("paths")
    public List<PathNodeEntity> getAllPaths(){
        return fileService.findAllPath();
    }

    @GetMapping("listFiles")
    @ApiOperation(value = "列出路径下的文件", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentPath", value = "父路径，默认获取'/'路径下的文件和目录,以/结尾，比如查询/a/b/目录下的文件"),
            @ApiImplicitParam(name = "pageSize", value = "本页的文件和目录，要获取多少个"),
            @ApiImplicitParam(name = "pageNum",value = "要查询的页数，从1开始")
    })
    public PageVo<FileEntity> listFiles(@RequestParam(value = "parentPath",required = false, defaultValue = "/")String parentPath,
                                        @RequestParam(value = "pageSize",required = false, defaultValue = "20") Integer pageSize,
                                        @RequestParam(value = "pageNum",required = false, defaultValue = "1") Integer pageNum) {
        return fileService.listFiles(parentPath,pageSize,pageNum);
    }
    @GetMapping("listPaths")
    @ApiOperation(value = "列出路径下的目录", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentPath", value = "父路径，默认获取'/'路径下的文件和目录,以/结尾，比如查询/a/b/目录下的文件"),
            @ApiImplicitParam(name = "pageSize", value = "本页的文件和目录，要获取多少个"),
            @ApiImplicitParam(name = "pageNum",value = "要查询的页数，从1开始")
    })
    public PageVo<PathNodeEntity> listPaths(@RequestParam(value = "parentPath",required = false, defaultValue = "/")String parentPath,
                                            @RequestParam(value = "pageSize",required = false, defaultValue = "20") Integer pageSize,
                                            @RequestParam(value = "pageNum",required = false, defaultValue = "1") Integer pageNum) {
        return fileService.listPaths(parentPath,pageSize,pageNum);
    }
}
