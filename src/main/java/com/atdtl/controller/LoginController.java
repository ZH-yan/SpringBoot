package com.atdtl.controller;

import com.atdtl.config.ShiroConfiguration;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Administrator
 * @since 2018/8/1 17:02
 */

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(ShiroConfiguration.class);

    @GetMapping(value = "/hello")
    public String hello(){
        log.info("不登录也可以访问...");
        return "hello...";
    }

    @GetMapping(value = "/index")
    public String index(){
        log.info("登陆成功了...");
        return "index";
    }

    @GetMapping(value = "/denied")
    public String denied(){
        log.info("小伙子，权限不足，别挣扎了...");
        return "denied...";
    }

    @GetMapping(value = "/login")
    public String login(String username, String password, RedirectAttributes model) {
        // 想要得到 SecurityUtils.getSubject() 的对象.. 访问地址必须跟 shiro 的拦截地址内. 不然会报空指针异常
        Subject subject = SecurityUtils.getSubject();
        // 用户输入的账号和密码，存到 Username Password token 对象中  然后有shiro内部认证对比
        // 认证执行者交由 AuthReaml 中的 doGetAuthenticationInfo 处理
        // 当认证成功后会向下执行，认证失败则抛出异常
        UsernamePasswordToken authenticationToken = new UsernamePasswordToken(username, password);

        try {
            subject.login(authenticationToken);
        } catch (UnknownAccountException e) {
            log.error("对用户[{}]进行登录验证,验证未通过,用户不存在", username);
            authenticationToken.clear();
            return "UnknownAccountException";
            // e.printStackTrace();
        } catch (LockedAccountException e) {
            //e.printStackTrace();
            log.error("对用户[{}]进行登录验证,验证未通过,账户已锁定", username);
            authenticationToken.clear();
            return "LockedAccountException";
        } catch (ExcessiveAttemptsException e) {
            // e.printStackTrace();
            log.error("对用户[{}]进行登录验证,验证未通过,错误次数过多", username);
            authenticationToken.clear();
            return "ExcessiveAttemptsException";
        } catch (AuthenticationException e) {
            // e.printStackTrace();
            log.error("对用户[{}]进行登录验证,验证未通过,堆栈轨迹如下", username, e);
            token.clear();
            return "AuthenticationException";

        }
        return "success";
    }

}
