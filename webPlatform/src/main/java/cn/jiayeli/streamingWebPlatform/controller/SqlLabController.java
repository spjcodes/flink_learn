package cn.jiayeli.streamingWebPlatform.controller;

import cn.jiayeli.streamingWebPlatform.common.CommonResponseType;
import cn.jiayeli.streamingWebPlatform.common.common.response.ResponseEnums;
import cn.jiayeli.streamingWebPlatform.model.SqlJobExecutorModule;
import cn.jiayeli.streamingWebPlatform.service.SqlLabService;
import cn.jiayeli.streamingWebPlatform.utils.validateor.ValidationResult;
import cn.jiayeli.streamingWebPlatform.utils.validateor.ValidatorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@CrossOrigin
@RequestMapping("sqlLab")
@Controller
public class SqlLabController {

    @Resource
    private SqlLabService sqlLabService;

    @Resource
    private ValidatorUtil validator;

    @PostMapping("executorSqlTask")
    @ResponseBody
    public CommonResponseType executorSqlTask(@RequestBody SqlJobExecutorModule sqlTask) {
        ValidationResult validateResult = validator.validate(sqlTask);
        if (validateResult.isHasErros()) {
            ResponseEnums parameterError = ResponseEnums.PARAMETER_ERROR;
            parameterError.setDesc(validateResult.getErroMsg());
            return CommonResponseType.error(parameterError);
        }
        sqlLabService.executorSqlTask(sqlTask);
        return CommonResponseType.ok(new SqlJobExecutorModule());
    }

}
