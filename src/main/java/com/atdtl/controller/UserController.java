package com.atdtl.controller;

import com.atdtl.entity.User;
import com.battcn.swagger.properties.ApiDataType;
import com.battcn.swagger.properties.ApiParamType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @since 2018/7/20 15:09
 */
@RestController
@RequestMapping("/users")
@Api(tags = "1.1", description = "用户管理", value = "用户管理")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @ApiOperation(value = "条件查询(DONE)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = ApiDataType.STRING, paramType = ApiParamType.QUERY),
            @ApiImplicitParam(name = "passwaord", value = "密码", dataType = ApiDataType.STRING, paramType = ApiParamType.QUERY),
    })
    public User query(String username, String password){
        log.info("多个参数 @ApiImplicitParams");
        return new User(1, username, password);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "主键查询(DONE)")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", dataType = ApiDataType.INT, paramType = ApiParamType.QUERY)})
    public User get(@PathVariable Integer id){
        log.info("多个参数 @ApiImplicitParam");
        return new User(id, "u1", "p1");
    }

    @PostMapping
    @ApiOperation(value = "添加用户(DONE)")
    public User post(@RequestBody User user){
        log.info("如果是POST PUT 这种带有 @requesBody 的可以不用写 @ApiImplicitParam");
        return user;
    }

    public void put(@PathVariable Integer id, @RequestBody User user){
        log.info("如果你不想写 @ApiImplicitParam 那么 Swagger 也会使用默认的参数名作为描述信息 ");
    }
}
