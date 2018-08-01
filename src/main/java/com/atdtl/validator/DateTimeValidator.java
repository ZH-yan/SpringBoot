package com.atdtl.validator;

import com.atdtl.annotation.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *  日期格式验证
 *
 * @author Administrator
 * @since 2018/7/27 14:26
 */
public class DateTimeValidator implements ConstraintValidator<DateTime, String> {

    private DateTime dateTime;

    /**
     * initialize : 主要用于初始化，它可以获得当前注解的所有属性
     * @param dateTime
     */
    @Override
    public void initialize(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     *  isValid : 进行约束验证的主题方法，其中 value 就是验证参数的具体实例， context 代表约束执行的上下文环境
     *  注意： 为空验证可以使用 @NotBlank、@NotNull、@NotEmpty 等注解来进行来控制，而不是在一个注解中做各种各样的规则判断，应该职责分离
     *
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // 如果 value 为空则不进行格式校验
        if (value == null) {
            return true;
        }
        String format = dateTime.format();
        if (value.length() != format.length()){
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
