package com.github.qlaall.controller;

import com.github.qlaall.config.BizException;
import com.github.qlaall.entity.FileEntity;
import com.github.qlaall.entity.PathNodeEntity;
import com.github.qlaall.service.FSAgent;
import com.github.qlaall.service.FileService;
import com.github.qlaall.util.Md5Util;
import com.github.qlaall.vo.FileDescribe;
import com.github.qlaall.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Api
@RequestMapping("/file")
@RestController
public class FileController {
    @Value("${external.mount}")
    String rootPath;
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
    @ApiOperation(value = "上传文件", notes = "", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fullPathName", value = "指定文件的完整目录，如不传，默认为根目录",required = false)
    })
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
    @GetMapping("filekey")
    @ApiOperation(value = "获取文件，通过上传返回的fileKey", notes = "", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileKey", value = "文件的key"),
            @ApiImplicitParam(name = "inline", value = "true表示在浏览器中打开，false表示作为附件下载",required = false,defaultValue = "true")
    })
    public ResponseEntity<byte[]> getByFileKey(@RequestParam("fileKey") String fileKey, @RequestParam(value = "inline",required = false,defaultValue = "true")Boolean inline) throws IOException {
        FileEntity fe=fileService.getFileEntityByKey(fileKey);
        InputStream is=new FSAgent().readStreamByFileKey(rootPath,fe.getKey());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fe.getContentType()));
        headers.setContentLength(fe.getFileSize());
        String inlineSetting=inline?"inline":"attachment";
        headers.add("Content-Disposition", inlineSetting+"; filename=" + URLEncoder.encode(fe.getFileName(),"UTF-8"));
        return new ResponseEntity(IOUtils.toByteArray(is), headers, HttpStatus.OK);
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
