package cn.jiayeli.tableAndSql;

import cn.jiayeli.util.EnvUtil;
import org.apache.flink.api.java.tuple.Tuple2;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamStatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.operations.QueryOperation;

import static org.apache.flink.table.api.Expressions.$;

public class TableEnvCreateByStreamEnv {

    private static Table tWord;

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        DataStreamSource<Tuple2<String, Integer>> words = env.fromElements(Tuple2.of("a", 1), Tuple2.of("b", 1), Tuple2.of("c", 1));
        // create a table by dataStream
        Table wordsTable =
                tEnv
                    .fromDataStream(words)
                    .as("word", "number");

       //1. table api
        Table tWord = wordsTable.groupBy($("word")).aggregate("sum(number) as counts").select($("word"), $("counts"));
        QueryOperation queryOperation = tWord.getQueryOperation();
        tWord.printSchema();
        TableResult tableResult = tWord.execute();
        tableResult.print();

/*
        //2. use flink sql

        // 2.1 register a table catalog
        tEnv.createTemporaryView("tbW", wordsTable);


        // 2.2 use the catalog info execute sql
        TableResult result = tEnv.executeSql("select word, sum(number) as counts from tbW group by word");
        result.print();

*/

        StreamStatementSet statementSet = tEnv.createStatementSet();
        statementSet.addInsert("default_catalog.default_database.tab1", tWord);
        statementSet.addInsert(
                TableDescriptor
                        .forConnector("kafka")
                        .schema(Schema
                                .newBuilder()
                                .column("f1", "")
                                .build())
                        .build(),
                tWord);
        tEnv.executeSql("");
        tEnv.sqlQuery("");
        tEnv.getConfig().getConfiguration().setInteger("checkpoint.interval", 1000);

    }
}
