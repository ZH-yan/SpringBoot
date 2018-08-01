package com.atdtl.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  shiro 配置
 *
 * @author Administrator
 * @since 2018/8/1 14:59
 */
@Configuration
public class ShiroConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean
    public EhCacheManager getEhCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehCacheManager;
    }

    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     *  加密器： 数据库则是 密文储存
     *
     * @return
     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
//        // 散列算法： 这里我们使用MD5算法
//        matcher.setHashAlgorithmName("md5");
//        // 散列的次数，比如散列两次，相当于 md5(md5(""));
//        matcher.setHashIterations(2);
//        return matcher;
//    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean("authrealm")
    public Authrealm getAuthrealm(EhCacheManager cacheManager){
        Authrealm authrealm = new Authrealm();
        authrealm.setCacheManager(cacheManager);
        return authrealm;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Authrealm authrealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authrealm);
        securityManager.setCacheManager(getEhCacheManager());
        return securityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/index");
        filterFactoryBean.setUnauthorizedUrl("/denied");
        loadShiroFilterChain(filterFactoryBean);
        return filterFactoryBean;
    }

    /**
     *  加载 shirofilter 权限控制规则（从数据库读取然后配置）
     *
     * @param filterFactoryBean
     */
    private void loadShiroFilterChain(ShiroFilterFactoryBean filterFactoryBean) {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/resource/**", "anon");
        filterChainDefinitionMap.put("/install", "anon");
        filterChainDefinitionMap.put("/hello", "anon");

        log.info("##################从数据库读取权限规则，加载到shiroFilter中##################");

        // 不用注解也可以通过 API 方式加载权限规则
        Map<String, String> permissions = new LinkedHashMap<>();
        permissions.put("/users/find", "perms[user:find]");
        filterChainDefinitionMap.putAll(permissions);
        filterChainDefinitionMap.put("/**", "authc");
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
}
