package com.atdtl.config;

import com.atdtl.entity.Employee;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *  安全认证 和 权限验证d的核心处理就是重写 AuthorizingRealm 中的
 *      doGetAuthenticationInfo（登录认证） 与  doGetAuthorizationInfo（权限认证）
 *
 * @author Administrator
 * @since 2018/8/1 15:13
 */
@Configuration
public class Authrealm extends AuthorizingRealm {

    /**
     *  重写登录认证
     *  认证回调函数，登录时调用
     *  1.根据传入的用户名获取employee 信息，如果 employee 为空，那么抛出没找到账号异常
     *  2.如果 employee 找到了，但锁定了抛出锁定异常
     *  3.最后生成 authenticationInfo 信息
     *  交给间接父类 AuthenticaatingRealm 使用 CredentialsMather 进行判断密码是否匹配
     *  如果不匹配将抛出密码错误异常 IncorrentCredentialsException;
     *  另外如果密码重试
     *  在组装 SimpleAuthenticationInfo 信息时，需要传入：身份信息（用户名）、凭据（密文密码）
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String principal = (String) token.getPrincipal();
        Employee employee = Optional.ofNullable(DBCahce.EMPLOYEE_CACHE.get(principal)).orElseThrow(UnknownAccountException::new);
        if (!employee.isLocked()){
            throw new LockedAccountException();
        }

        // 从数据库查询出来的账号和密码，需要与用户输入的账号和密码对比
        // 当用户执行登录时，在方法处理上要实现 employee.login(employdd)
        // 然后会自动进入这个类进行认证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, employee.getPassword(), getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("EMPLOYEE_SESSION", employee);

        return authenticationInfo;
    }

    /**
     *  重写 权限校验
     *  只有需要验证权限时才会调用，授权查询回调函数，进行鉴权但缓存中无用户的授权信息时调用。
     *  在配有缓存的情况下，只加载一次
     *
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        Session session = SecurityUtils.getSubject().getSession();
        Employee employee = (Employee) session.getAttribute("EMPLOYEE_SESSION");

        // 权限信息对象info，用来存放查出的用户的所有的角色（role) 及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 用户的角色集合
        Set<String> roles = new HashSet<>();
        roles.add(employee.getRoleName());
        info.setRoles(roles);

        // 用户的角色对应的所有权限，
        final Map<String, Collection<String>> permissionsCache = DBCahce.PERMISSIONS_CACHE;
        final Collection<String> permissions = permissionsCache.get(employee.getRoleName());
        info.addStringPermissions(permissions);

        return info;
    }

}
