package com.atdtl;

import com.atdtl.endpoint.MyEndPoint;
import com.atdtl.interceptor.CacheKeyGenerator;
import com.atdtl.interceptor.LockKeyGenerator;
import com.battcn.swagger.annotation.EnableSwagger2Doc;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.Arrays;

/**
 *  我的第一个SpringBoot程序
 *  其中@RestController 相当于（@Controller 和 @ResponseBody）
 *  @EnableAdminServer  注解代表是 Server 端，集成UI的
 *
 */
@EnableWebSocket
@EnableAsync
@EnableScheduling
@EnableSwagger2Doc
@RestController
@SpringBootApplication
@EnableCaching
@EnableAdminServer
public class Chapter1Application {

	public static void main(String[] args) {
		SpringApplication.run(Chapter1Application.class, args);
	}

	@RequestMapping("/hello")
    public String demo1(){
	    return "hello dtl";
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx){
        // 目的是
        return args -> {
            // System.out.println("来看看 SpringBoot 默认为我们提供的 Bean：");
            String[] beansName = ctx.getBeanDefinitionNames();
            Arrays.sort(beansName);
            // Arrays.stream(beansName).forEach(System.out::print);
        };
    }

    /**
     *
     * 声明成一个类
     */
    @Configuration
    static class MyEndPointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnEnabledEndpoint
        public MyEndPoint myEndPoint(){
            return new MyEndPoint();
        }
    }

    /**
     * dev 环境加载
     */
    @Profile("dev")
    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll().and()
                    .csrf().disable();
        }
    }

    /**
     * prod 环境加载
     * actuator和admin服务管理和监控
     */
    @Profile("prod")
    @Configuration
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");

            http.authorizeRequests().antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler)
                    .and()
                    .logout().logoutUrl(adminContextPath + "/logout")
                    .and()
                    .httpBasic()
                    .and()
                    .csrf().disable();
        }
    }

    /**
     *  默认情况下： TaskScheduler 的 poolsize 为1
     * @return  线程池
     */
    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }

    @Bean
    public CacheKeyGenerator cacheKeyGenerator() {
        return new LockKeyGenerator();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
