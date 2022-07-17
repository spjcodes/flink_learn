package cn.jiayeli.dataLeaf;

import cn.jiayeli.dataLeaf.common.sql.SqlFileParse;
import cn.jiayeli.dataLeaf.utils.EnvUtils;
import org.apache.flink.table.api.internal.TableEnvironmentInternal;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.operations.Operation;
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
}
