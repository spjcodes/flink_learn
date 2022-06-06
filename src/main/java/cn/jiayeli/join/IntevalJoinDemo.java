package cn.jiayeli.join;

import cn.jiayeli.utils.EnvUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Objects;

public class IntevalJoinDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = EnvUtil.get();

        KeyedStream<Tuple2<Integer, String>, Integer> movieKeyedStream = env
                .fromCollection(DataGenerator.getMovies())
                .keyBy(e -> e.f0);

        DataGenerator.getRatingInfoStream(env)
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Tuple3<Integer, Integer, Long>>forBoundedOutOfOrderness(Duration.ofMillis(20L))
                        .withTimestampAssigner((e, t) -> e.f2))
                .keyBy(e -> e.f0)
                .intervalJoin(movieKeyedStream)
//                .inProcessingTime()
                .between(Time.milliseconds(0L), Time.milliseconds(10L))
                .process(new ProcessJoinFunction<Tuple3<Integer, Integer, Long>, Tuple2<Integer, String>, Tuple3<String, Double, Integer>>() {
                    @Override
                    public void processElement(Tuple3<Integer, Integer, Long> left, Tuple2<Integer, String> right, ProcessJoinFunction<Tuple3<Integer, Integer, Long>, Tuple2<Integer, String>, Tuple3<String, Double, Integer>>.Context ctx, Collector<Tuple3<String, Double, Integer>> out) throws Exception {

                        String movieName = Objects.equals(left.f0, right.f0) ? movieName = right.f1 :  left.f0.toString();
                        out.collect(Tuple3.of(movieName, Double.parseDouble(left.f1.toString()), 0));
                    }
                })
                .print();

        env.execute();
    }
}
