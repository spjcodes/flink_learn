package cn.jiayeli.dataLeaf.core;

import cn.jiayeli.dataLeaf.enums.OperationTypeEnum;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentInternal;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.table.operations.command.SetOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SqlExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SqlExecutor.class);
    private static TableResult result = null;
    private TableEnvironmentInternal tEnvInternal;

    public  void execute(List<String> sqlList) {

        Parser parser = tEnvInternal.getParser();

        for (String stmt : sqlList) {

            Operation operation = parser.parse(stmt).get(0);
            switch (caseType(operation)) {
                //match DDL
                case DDL:
                    result = tEnvInternal.executeInternal(operation);
                    break;
                case DML:
                    result = tEnvInternal.executeInternal(operation);
                    break;
                case DQL:
                    result = tEnvInternal.executeInternal(operation);
                    break;
                case SET:
                    this.setConfig(operation);
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
           result.print();
        }

    }

    private void setConfig(Operation operation) {
        SetOperation setOperation = (SetOperation) operation;
        if (!setOperation.getKey().isEmpty() && !setOperation.getValue().isEmpty()) {
            tEnvInternal
                    .getConfig().getConfiguration()
                    .setString(setOperation.getKey().get(), setOperation.getValue().get());
        } else {
            logger.error("set parameter error by:\t[" + operation.asSummaryString() + "]\t 'set' need a key and value.");
        }
    }

    private OperationTypeEnum caseType(Operation operation) {
        OperationTypeEnum operationType = OperationTypeEnum.UNKOWN;
        String name = operation.getClass().getSimpleName().toUpperCase();

        if (name.startsWith("ShowCreateTable".toUpperCase())) {
            operationType = OperationTypeEnum.DQL;
        } else if (name.startsWith("Explain".toUpperCase())) {
            operationType = OperationTypeEnum.DQL;
        } else if (name.startsWith("PlannerQuery".toUpperCase())) {
            operationType = OperationTypeEnum.DQL;
        } else if (name.startsWith("Set".toUpperCase()))  {
            operationType = OperationTypeEnum.SET;
        }
        return operationType;
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
