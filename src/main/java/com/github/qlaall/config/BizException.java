package com.github.qlaall.config;

import org.springframework.http.HttpStatus;

/**
 * @author: qlaall
 * @Date:2018/7/31
 * @Time:23:49
 */
public class BizException extends RuntimeException {
    private Integer errCode;
    private HttpStatus status;

    public BizException(String message) {
        super(message);
        this.errCode = 500;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BizException(Integer errCode, String message) {
        super(message);
        this.errCode = errCode;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BizException(HttpStatus status, String message) {
        super(message);
        this.errCode = status.value();
        this.status = status;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
