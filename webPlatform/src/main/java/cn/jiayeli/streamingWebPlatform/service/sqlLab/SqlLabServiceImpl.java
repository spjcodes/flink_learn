package cn.jiayeli.streamingWebPlatform.service.sqlLab;

import cn.jiayeli.dataLeaf.core.SqlExecutor;
import cn.jiayeli.dataLeaf.utils.EnvUtils;
import cn.jiayeli.streamingWebPlatform.model.SqlJobExecutorModule;
import cn.jiayeli.streamingWebPlatform.service.SqlLabService;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.springframework.stereotype.Service;

/**
 * @author: jiayeli.cn
 * @description sql lab services
 * @date: 2022/8/24 下午8:22
 */
@Service
public class SqlLabServiceImpl implements SqlLabService {



    @Override
    public boolean executor(SqlJobExecutorModule sqlTask) {

        return false;
    }

    @Override
    public void executorSqlTask(SqlJobExecutorModule sqlTask) {
        StreamTableEnvironment tEnv = EnvUtils.getStreamTableEnv();
        SqlExecutor.builder()
                .setEnv(tEnv)
                .execute(sqlTask.getSqlScript());
//                .setConfig(null);

    }

}
