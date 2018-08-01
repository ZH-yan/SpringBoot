package com.atdtl.exception;

/**
 * 自定义异常
 *
 * @author Administrator
 * @since 2018/7/26 19:12
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = -5363629156577659520L;

    private int code;

    public MyException() {
        super();
    }

    public MyException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
