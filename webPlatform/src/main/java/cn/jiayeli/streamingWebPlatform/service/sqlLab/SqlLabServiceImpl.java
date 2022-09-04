package cn.jiayeli.streamingWebPlatform.service.sqlLab;

import cn.jiayeli.streamingWebPlatform.model.SqlTaskExecutorModule;
import cn.jiayeli.streamingWebPlatform.service.SqlLabService;
import org.springframework.stereotype.Service;

/**
 * @author: jiayeli.cn
 * @description sql lab services
 * @date: 2022/8/24 下午8:22
 */
@Service
public class SqlLabServiceImpl implements SqlLabService {

    @Override
    public boolean executor(SqlTaskExecutorModule sqlTask) {

        return false;
    }

    @Override
    public void executorSqlTask(SqlTaskExecutorModule sqlTask) {

    }

}
