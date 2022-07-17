package cn.jiayeli.stream;

import cn.jiayeli.util.EnvUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.Collector;
import org.h2.tools.Csv;

import java.util.Arrays;

public class Demo {
    private static Object DataGenOptions;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = EnvUtil.get();

        env.setParallelism(1);

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne = env
                .socketTextStream("localhost", 10010)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String line, Collector<Tuple2<String, Integer>> out) throws Exception {
                        Arrays.stream(line.split("\\s+"))
                                .forEach(e -> out.collect(Tuple2.of(e, 1)));
                    }
                });

        Table tw = tEnv.fromDataStream(wordAndOne).as("word", "number");

        tEnv.createTemporaryView("ttw", tw);
      /*  tw.printSchema();

        Table result = tEnv.sqlQuery("select word, sum(number) from ttw group by word");
        tEnv.toChangelogStream(result).print();*/

//        tw.groupBy("word").aggregate("sum(number) as total").select("word, total").execute().print();
        Table table = tEnv.sqlQuery("select * from ttw");
        tEnv.toChangelogStream(table);
        table.execute().print();
//        tEnv.execute("");

        tEnv.getConfig().addJobParameter("", "");
        TableResult ddl = tEnv.executeSql("ddl");
        Table dql = tEnv.sqlQuery("dql");
        String explain = tEnv.explainSql("explain");


    }
}
