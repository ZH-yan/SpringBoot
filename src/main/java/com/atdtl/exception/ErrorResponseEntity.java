package com.atdtl.exception;

/**
 * 异常信息模板
 *
 * @author Administrator
 * @since 2018/7/26 19:15
 */
public class ErrorResponseEntity {
    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponseEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
