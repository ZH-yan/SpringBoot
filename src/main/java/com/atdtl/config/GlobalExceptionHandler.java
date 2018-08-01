package com.atdtl.config;

import com.atdtl.exception.ErrorResponseEntity;
import com.atdtl.exception.MyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *      @RestControllerAdvice 捕获Controller层抛出的异常；如果添加 @ResponseBody，则返回的信息为json格式
 *      @ExceptionHandler 统一处理一钟类的异常，减少代码重复率，降低复杂度
 *
 * @author Administrator
 * @since 2018/7/26 19:22
 */
// @RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 定义要捕获的异常 可以有多个 @Exception（{}）
     * @param requeste  request
     * @param e         exception
     * @param response  response
     * @return          响应结果
     */
    @ExceptionHandler(MyException.class)
    public ErrorResponseEntity myExceptionHandler(HttpServletRequest requeste, final Exception e, HttpServletResponse response){
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        MyException exception = (MyException) e;
        return new ErrorResponseEntity(exception.getCode(), exception.getMessage());
    }

    /**
     * 捕获 RuntimeException 异常
     * TODO 如果你觉得在一个 exceptionHandler 通过 if（e instanceof  xxxException）太麻烦
     * TODO 那么你还可以写多个不同的 exceptionHandler 处理不同的异常
     *
     * @param requeste  request
     * @param e         exception
     * @param response  response
     * @return          响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponseEntity runtimeExceptionHandler(HttpServletRequest requeste, final Exception e, HttpServletResponse response){
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RuntimeException exception = (RuntimeException) e;
        return new ErrorResponseEntity(400, exception.getMessage());
    }

    /**
     *  调用的接口映射异常处理方法
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return new ResponseEntity<>(new ErrorResponseEntity(status.value(), exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()), status);
        }

        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            logger.error("参数转换失败，方法：" + exception.getParameter().getMethod().getName() + ",参数：" + exception.getName() +
                    ",信息：" + exception.getLocalizedMessage());
            return new ResponseEntity<>(new ErrorResponseEntity(status.value(), "参数转换失败"), status);
        }

        return new ResponseEntity<Object>(new ErrorResponseEntity(status.value(), "参数转换失败"),status);
    }
}
