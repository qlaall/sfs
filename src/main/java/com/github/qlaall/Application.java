package com.github.qlaall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //、formData某个文件的最大大小
        factory.setMaxFileSize(DataSize.of(20, DataUnit.MEGABYTES));
        /// 设置一个formData的总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(100, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }
}
