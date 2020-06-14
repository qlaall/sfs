package com.github.qlaall.config;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(toErrResponse(500,e.getMessage()));
    }
    @ExceptionHandler(ClientAbortException.class)
    public void ClientAbortException(ClientAbortException e) {
        logger.info("客户端关闭了连接。");
    }
    @ExceptionHandler(BizException.class)
    public ResponseEntity expireHandler(BizException e) {
        //业务异常无需打印异常栈，因为都是自己定义的 一看就知道了
        logger.error(e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(toErrResponse(e.getErrCode(), e.getMessage()));
    }
    private ErrorResponseBody toErrResponse(Integer errCode,String msg){
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        errorResponseBody.setCode(errCode);
        errorResponseBody.setMsg(msg);
        return errorResponseBody;
    }
    static class ErrorResponseBody {
        private Integer code;
        private String msg;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}