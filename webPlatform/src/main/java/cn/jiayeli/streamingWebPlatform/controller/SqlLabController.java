package cn.jiayeli.streamingWebPlatform.controller;

import cn.jiayeli.streamingWebPlatform.common.CommonResponseType;
import cn.jiayeli.streamingWebPlatform.common.common.response.ResponseEnums;
import cn.jiayeli.streamingWebPlatform.model.SqlTaskExecutorModule;
import cn.jiayeli.streamingWebPlatform.service.SqlLabService;
import cn.jiayeli.streamingWebPlatform.utils.validateor.ValidationResult;
import cn.jiayeli.streamingWebPlatform.utils.validateor.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@CrossOrigin
@RequestMapping("sqlLab")
@Controller
public class SqlLabController {

    @Resource
    private SqlLabService sqlLabService;

    @Autowired
    private ValidatorUtil validator;

    @PostMapping("executor")
    @ResponseBody
    public CommonResponseType executorSqlTask(@RequestBody SqlTaskExecutorModule sqlTask) {
        ValidationResult validateResult = validator.validate(sqlTask);
        if (validateResult.isHasErros()) {
            ResponseEnums parameterError = ResponseEnums.PARAMETER_ERROR;
            parameterError.setDesc(validateResult.getErroMsg());
            return CommonResponseType.error(parameterError);
        }
        sqlLabService.executorSqlTask(sqlTask);
        return CommonResponseType.ok(new SqlTaskExecutorModule());
    }

}
