package cn.jiayeli.streamingWebPlatform.service;

import cn.jiayeli.streamingWebPlatform.model.SqlJobExecutorModule;
import org.springframework.stereotype.Service;


@Service
public interface SqlLabService {

        public boolean executor(SqlJobExecutorModule sqlTask);

        void executorSqlTask(SqlJobExecutorModule sqlTask);
}