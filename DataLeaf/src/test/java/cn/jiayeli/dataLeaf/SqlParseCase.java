package cn.jiayeli.dataLeaf;

import cn.jiayeli.dataLeaf.common.sql.SqlFileParse;
import cn.jiayeli.dataLeaf.utils.EnvUtils;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentInternal;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.table.operations.command.SetOperation;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class SqlParseCase {

    @Test
    public void fileParse2Sql() throws IOException {
        String filePath = "src/test/resources/sqlFile.sql";
        List<String> strings = SqlFileParse.parseFile2Sql(filePath);
        System.out.println(strings.toString());

    }

    @Test
    public void t() {
        tt("a", 1);
        tt("a", 1.1F);
        tt("a", 1.2D);
        tt("a", "str");
        tt("a", true);
    }

    public void tt(String s, Object o) {
        if (o instanceof Integer) {
            System.out.println("int");
        }
        if (o instanceof String) {
            System.out.println("String");
        }
        if (o instanceof Double) {
            System.out.println("Double");
        }
        if (o instanceof Float) {
            System.out.println("Float");
        }
        if (o instanceof Boolean) {
            System.out.println("Boolean");
        }
    }

    @Test
    public void sqlOperationTestCase() {
        String create = "CREATE TABLE MyTable1 (`count` bigint, word VARCHAR(256)) WITH ('connector' = 'datagen')";

        String create1 = "CREATE TABLE MyTable2 (`count` bigint, word VARCHAR(256)) WITH ('connector' = 'datagen')";

        String show = "show create table KafkaTable";

        String select =  " SELECT `count`, word FROM MyTable1 WHERE word LIKE 'F%'  UNION ALL SELECT `count`, word FROM MyTable2";

        String set = "SET 'table.local-time-zone' = 'Europe/Berlin'";

        String explain = "EXPLAIN PLAN FOR  SELECT `count`, word FROM MyTable1 WHERE word LIKE 'F%' ";

        TableEnvironmentInternal tEnvItvl = (TableEnvironmentInternal) EnvUtils.getStreamTableEnv();

        tEnvItvl.executeSql(create);
        tEnvItvl.executeSql(create1);

        Parser parser = tEnvItvl.getParser();

        Operation showOperator = parser.parse(show).get(0);
        Operation explainOperator = parser.parse(explain).get(0);
        Operation selectOperator = parser.parse(select).get(0);
        Operation setOperator = parser.parse(set).get(0);

        /**
         * ShowCreateTableOperation
         * ExplainOperation
         * PlannerQueryOperation
         * SetOperation
         */
        System.out.println(showOperator.getClass().getSimpleName());
        System.out.println(explainOperator.getClass().getSimpleName());
        System.out.println(selectOperator.getClass().getSimpleName());
        System.out.println(setOperator.getClass().getSimpleName());

    }

    @Test
    public void setOperationCase() {
        TableEnvironmentInternal tEnvInt = (TableEnvironmentInternal) EnvUtils.getStreamTableEnv();
        Parser parser = tEnvInt.getParser();
        List<Operation> parse = parser.parse("set \"sql-client.display.max-column-width\"=30L");
        List<Operation> parse1 = parser.parse("set sql-client.execution.result-mode='TABLE'");
        SetOperation setOperation = (SetOperation) parse1.get(0);
        System.out.println(setOperation.getKey().get());
        Object b = setOperation.getValue().get();
        System.out.println(b);
        System.out.println(b instanceof Long);
        System.out.println(setOperation.asSummaryString());
//        tEnvInt.executeInternal(new SetOperation("pipeline.max-parallelism", "10"));
        tEnvInt.getConfig().getConfiguration().setInteger("pipeline.max-parallelism", 10);
        String str = "1|1.2D|1.222F|100L";
        Object i = 1;
        Object d = 1.2D;
        Object f = 1.222F;
        Object l = 100L;

        System.out.println(i instanceof Integer);
        System.out.println(d instanceof Double);
        System.out.println(f instanceof Float);
        System.out.println(l instanceof Long);
    }
}
