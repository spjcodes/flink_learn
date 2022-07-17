package cn.jiayeli.dataLeaf.core;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentInternal;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.operations.Operation;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SqlExecutor {
    private static TableResult result = null;
    private TableEnvironmentInternal tEnvInternal;

    public  void execute(List<String> sqlList) {

        Parser parser = tEnvInternal.getParser();


        for (String stmt : sqlList) {

            Operation operation = parser.parse(stmt).get(0);
            switch (caseType(operation)) {
                //match DDL
                case "DDL":

                    break;
                case "DML":
                    break;
                case "DQL":
                    break;
                default:
                    result = tEnvInternal.executeInternal(operation);
                    break;

            }

            processResult(result);
            try {
                TimeUnit.SECONDS.sleep(5l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tEnvInternal.executeSql("select * from KafkaTable");


        }

    }

    private String caseType(Operation operation) {
        String name = operation.getClass().getSimpleName().toUpperCase();

        if (name.startsWith("")) {

        } else if (name.startsWith("")) {

        } else if (name.startsWith("")) {

        }
        return null;
    }

    public static void processResult(TableResult result) {
        result.print();
    }

    public static SqlExecutor builder() {
        return new SqlExecutor();
    }

    public SqlExecutor setEnv(StreamTableEnvironment tEnv) {
        this.tEnvInternal = (TableEnvironmentInternal) tEnv;
        return this;
    }
}
