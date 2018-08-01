package com.atdtl.entity;

import com.atdtl.validator.groups.Groups;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 轻松搞定数据验证：如果在后端不对数据进行校验，我们完全可以模拟HTTP请求到后台地址，
 * 模拟请求中发送一些涉及系统安全的数据到后台，这并不安全。
 * @NotNull     用在基本类型上
 * @NotEmpty    用在集合类上
 * @NotBlank    用在字符串上
 *
 * @author Administrator
 * @since 2018/7/23 15:40
 */
public class Book implements Serializable {
    private static final long serialVersionUID = -6423003725084775680L;

    @NotNull(message = "id 不能为空", groups = Groups.Update.class)
    private String id;

    @NotBlank(message = "name 不允许为空", groups = Groups.Default.class)
    @Length(min = 2, max = 10, message = "name 长度必须在 {min}-{max} 之间")
    private String name;

    @NotNull(message = "price 不允许为空", groups = Groups.Default.class)
    @DecimalMin(value = "0.1", message = "价格不能低于${value}元")
    private BigDecimal price;

    public Book() {
    }

    public Book(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
