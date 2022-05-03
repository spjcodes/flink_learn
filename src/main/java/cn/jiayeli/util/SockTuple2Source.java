package cn.jiayeli.util;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class SockTuple2Source {

    private static  StreamExecutionEnvironment env = null;


    public  SockTuple2Source(StreamExecutionEnvironment environment) {
        this.env = environment;
    }

    public SockTuple2Source() {}

    public static SingleOutputStreamOperator<Tuple2<String, Integer>> get(StreamExecutionEnvironment env) {
        return env
                .socketTextStream("localhost", 10010)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] strings = value.split("\\s+");
                        for (int i = 0; i < strings.length; i += 2) {
                            out.collect(Tuple2.of(strings[i], Integer.valueOf(strings[i + 1])));
                        }
                    }
                });
    }

    public static SingleOutputStreamOperator<Tuple2<String, String>> getTuple2String(StreamExecutionEnvironment env) {
        return env
                .socketTextStream("localhost", 10010)
                .flatMap(new FlatMapFunction<String, Tuple2<String, String>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, String>> out) throws Exception {
                        String[] strings = value.split("\\s+");
                        for (int i = 0; i < strings.length; i += 2) {
                            out.collect(Tuple2.of(strings[i], strings[i + 1]));
                        }
                    }
                });
    }
}
