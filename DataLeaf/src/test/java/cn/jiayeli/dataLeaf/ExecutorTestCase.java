package cn.jiayeli.dataLeaf;

import cn.jiayeli.dataLeaf.common.sql.SqlFileParse;
import cn.jiayeli.dataLeaf.core.SqlExecutor;
import cn.jiayeli.dataLeaf.utils.EnvUtils;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

import java.util.List;

public class ExecutorTestCase {

    @Test
    public void executorCase() {
        StreamTableEnvironment tEnv = EnvUtils.getStreamTableEnv();
        String filePath = "src/test/resources/sqlFile.sql";
        List<String> statements = SqlFileParse.parseFile2Sql(filePath);

        SqlExecutor
                .builder()
                .setEnv(tEnv)
                .execute(statements);
    }
//{"user_id":1, "item_id":1, "behavior":"view", "ts": 1l}
    @Test
    public void kafkaSqlTest() {
        StreamTableEnvironment tEnv = EnvUtils.getStreamTableEnv();
        TableResult kafkaResultTeable = tEnv.executeSql("CREATE TABLE kafkaTable (\n" +
                "     `user_id` BIGINT,\n" +
                "     `item_id` BIGINT,\n" +
                "     `behavior` STRING,\n" +
                "     `ts` TIMESTAMP(3) METADATA FROM 'timestamp'\n" +
                ") WITH (\n" +
                "    'connector' = 'kafka',\n" +
                "    'topic' = 'test1',\n" +
                "    'properties.bootstrap.servers' = 'node02:9092,node03:9092',\n" +
                "    'properties.group.id' = 'test1Group',\n" +
                "    'scan.startup.mode' = 'latest-offset',\n" +
                "    'format' = 'json'\n" +
                ")");

       tEnv.executeSql("select * from kafkaTable").print();
/*         StreamStatementSet statementSet = tEnv.createStatementSet();
        statementSet.addInsertSql("select * from KafkaTable");
        statementSet.addInsertSql("select user_id, behavior, count(item_id) from KafkaTable group by user_id, behavior");*/
//        tEnv.executeSql("select user_id, behavior, count(1) from KafkaTable group by user_id, behavior").print();
    }

    @Test
    public void sqlExecutorClassCase() {
        String filePath = "src/test/resources/sqlFile.sql";
        SqlExecutor.builder()
                .setEnv(EnvUtils.getStreamTableEnv())
                .execute(SqlFileParse.parseFile2Sql(filePath));
    }
}
