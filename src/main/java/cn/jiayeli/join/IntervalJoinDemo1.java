package cn.jiayeli.join;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.Random;

public class IntervalJoinDemo1 {

    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = EnvUtil.get();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        Logger logger = LoggerFactory.getLogger(IntervalJoinDemo1.class.getName());

//        <orderNo, desc, time>
        SingleOutputStreamOperator<Tuple3<String, String, Long>> orders = env
                .socketTextStream("jiayeli", 10010)
                .flatMap(new FlatMapFunction<String, Tuple3<String, String, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple3<String, String, Long>> out) throws Exception {
                        String[] strings = value.split("\\s+");
                        Tuple3<String, String, Long> order = Tuple3.of(strings[0], strings[1], Long.parseLong(strings[2]));
                        out.collect(order);
                        logger.info(order.toString());
                    }
                }).assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<Tuple3<String, String, Long>>forBoundedOutOfOrderness(Duration.ofMillis(10))
                                .withTimestampAssigner((e, t) -> e.f2));

//        <orderNo, sum, time>
        SingleOutputStreamOperator<Tuple3<String, Integer, Long>> play = env
                .socketTextStream("localhost", 10011)
                .flatMap(new FlatMapFunction<String, Tuple3<String, Integer, Long>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple3<String, Integer, Long>> out) throws Exception {
                        String[] strings = value.split("\\s+");
                        Tuple3<String, Integer, Long> play = Tuple3.of(strings[0],Integer.valueOf(strings[1]), Long.parseLong(strings[2]));
                        out.collect(play);
                        logger.info(play.toString());
                    }
                }).assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<Tuple3<String, Integer, Long>>forBoundedOutOfOrderness(Duration.ofMillis(10))
                                .withTimestampAssigner((e, t) -> e.f2));

        orders
                .keyBy(o -> o.f0)
                .intervalJoin(play.keyBy(p -> p.f0))
                .between(Time.milliseconds(-10L), Time.milliseconds(10L))
                .process(new ProcessJoinFunction<Tuple3<String, String, Long>, Tuple3<String, Integer, Long>, Tuple4<String, String, Integer, Long>>() {
                    @Override
                    public void processElement(Tuple3<String, String, Long> left, Tuple3<String, Integer, Long> right, ProcessJoinFunction<Tuple3<String, String, Long>, Tuple3<String, Integer, Long>, Tuple4<String, String, Integer, Long>>.Context ctx, Collector<Tuple4<String, String, Integer, Long>> out) throws Exception {
                        if (left.f0.equals(right.f0)) {
                            out.collect(Tuple4.of(left.f0, left.f1, right.f1, right.f2));
                        }
                    }
                })
                .print();

        env.execute();
    }
}
