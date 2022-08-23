package cn.jiayeli.streamingWebPlatform.controller;

import cn.jiayeli.streamingWebPlatform.common.CommonResponseType;
import cn.jiayeli.streamingWebPlatform.common.common.response.ResponseEnums;
import cn.jiayeli.streamingWebPlatform.model.SqlTaskExecutorModule;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;


@CrossOrigin
@RequestMapping("sqlLab")
@Controller
public class SqlLabController {

    @PostMapping("executor")
    @ResponseBody
    public CommonResponseType executorSqlTask(@RequestBody SqlTaskExecutorModule sqlTask) {
        System.out.println(sqlTask.getSqlScript());
        return (Math.abs(new Random().nextInt())%10 == 0) ? CommonResponseType.ok(new SqlTaskExecutorModule()) : CommonResponseType.error(ResponseEnums.UNKNOWN_ERROR);
    }

}
