package com.github.qlaall.util;

import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

public class Md5Util {
    public static String md5(byte[] bytes){
        return DigestUtils.md5DigestAsHex(bytes);
    }
    public static String md5(InputStream inputStream){
        try {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            return null;
        }
    }
}
