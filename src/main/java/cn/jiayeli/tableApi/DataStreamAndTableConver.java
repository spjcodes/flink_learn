package cn.jiayeli.tableApi;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Test;

import java.util.Arrays;

import static org.apache.flink.table.api.Expressions.$;

public class DataStreamAndTableConver {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        DataStreamSource<String> words = env.fromCollection(Arrays.asList("a", "b", "c", "a", "c", "d", "A", "a", "d", "c", "e", "f"));
        Schema schema = Schema
                .newBuilder()
                .column("f0", DataTypes.STRING())
                .build();

        Table table1 = tEnv.fromDataStream(words).select($("f0").as("word"));
//        table1.select($("f0").as("word")).execute().print();



     /*
        tEnv.createTemporaryView("tb1", table1);
        tEnv.sqlQuery("select word, count(word) as nums from tb1 group by word").execute().print();
        */

        DataStream<Row> dataStream = env.fromElements(
                Row.of("Alice", 12),
                Row.of("Bob", 10),
                Row.of("Alice", 100));

        Table inputTable = tEnv.fromDataStream(dataStream).as("name", "score");

        tEnv.createTemporaryView("InputTable", inputTable);
        Table resultTable = tEnv.sqlQuery(
                "SELECT name, SUM(score) FROM InputTable GROUP BY name");

        DataStream<Row> resultStream = tEnv.toChangelogStream(resultTable);

        resultStream.print();
        env.execute();







    }


    @Test
    public void a() {
        System.out.println(4>>>2);


    }
}
