package com.atdtl.endpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * @EndPoint    构建 rest api 的唯一路径
 *  不同的请求操作，调用时缺少必需的参数，或者使用无法转换为所需类型的参数，则不会调用操作方法，响应状态为400（错误请求）
 * @ReadOperation   get请求 ，响应状态为200 如没有返回值响应404（资源未找到）
 * @WritOperation   Post请求，响应状态W为200    如没有返回值响应204（无响应内容）
 * @DeleteOperation Delete请求，响应状态W为200    如没有返回值响应204（无响应内容）
 *
 * @author Administrator
 * @since 2018/7/25 11:53
 */
@Endpoint(id = "atdtl")
public class MyEndPoint {

    @ReadOperation
    public Map<String , Object> hello(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("author", "Yzh");
        result.put("age", "24");
        result.put("email", "974206359@qq.com");
        return result;
    }

}
