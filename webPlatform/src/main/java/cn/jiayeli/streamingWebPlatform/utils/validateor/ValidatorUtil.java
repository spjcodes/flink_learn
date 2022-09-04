package cn.jiayeli.streamingWebPlatform.utils.validateor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author: jiayeli.cn
 * @description validate config
 * @date: 2022/9/3 下午4:49
 */

@Component
public class ValidatorUtil implements InitializingBean {

    private Validator validator = null;

    public ValidationResult validate(Object bean) {

        ValidationResult result = new ValidationResult();
        //错误结果集
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if (constraintViolationSet.size() > 0) {
            constraintViolationSet.forEach(constraintViolation -> {
                result.setHasErros(true);
                String erroMsg = constraintViolation.getMessage();
                String paramName = constraintViolation.getPropertyPath().toString();
                result.getErroMsgMap().put(paramName, erroMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("into initializer method.......");
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

/*    public void afterPropertiesSet() throws Exception {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }*/
}
