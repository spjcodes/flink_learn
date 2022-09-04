package cn.jiayeli.streamingWebPlatform.service;

import cn.jiayeli.streamingWebPlatform.model.SqlTaskExecutorModule;
import org.springframework.stereotype.Service;


@Service
public interface SqlLabService {

        public boolean executor(SqlTaskExecutorModule sqlTask);

        void executorSqlTask(SqlTaskExecutorModule sqlTask);
}