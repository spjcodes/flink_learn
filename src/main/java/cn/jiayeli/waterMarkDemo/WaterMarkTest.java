package cn.jiayeli.waterMarkDemo;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;

public class WaterMarkTest {

    private static final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    public static void main(String[] args) {
        env.setParallelism(1);
        SingleOutputStreamOperator<Tuple2<String, Integer>> dataSource = env
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

        SingleOutputStreamOperator<Tuple2<String, Integer>> dataSource1 = env
                .socketTextStream("localhost", 10011)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] strings = value.split("\\s+");
                        for (int i = 0; i < strings.length; i += 2) {
                            out.collect(Tuple2.of(strings[i], Integer.valueOf(strings[i + 1])));
                        }
                    }
                });

        //                .fromElements(Tuple2.of("A", 1), Tuple2.of("B", 1), Tuple2.of("A", 2), Tuple2.of("A", 3), Tuple2.of("A", 5), Tuple2.of("A", 4), Tuple2.of("A", 11), Tuple2.of("A", 9));


        SingleOutputStreamOperator<Tuple2<String, Integer>> reduce = dataSource
                .assignTimestampsAndWatermarks(
                        /**
                         * WatermarkStrategy 定义了如何在流源中生成 Watermark。
                         * WatermarkStrategy 是生成水印的 WatermarkGenerator 和分配记录的内部时间戳的 TimestampAssigner 的构建器/工厂。
                         * 此接口分为三部分：
                         *      1）此接口的实现者需要实现的方法，
                         *      2）用于在基本策略上构建 WatermarkStrategy 的构建器方法，
                         *      3）用于为常见的内置策略或基于构建 WatermarkStrategy 的便捷方法在 WatermarkGeneratorSupplier 上该接口的实现者只需要实现
                         *      createWatermarkGenerator(WatermarkGeneratorSupplier.Context)。 或者，您可以实现 createTimestampAssigner(TimestampAssignerSupplier.Context)构建器方法，
                         *      如 withIdleness(Duration) 或 createTimestampAssigner(TimestampAssignerSupplier.Context) 创建一个新的 WatermarkStrategy 来包装和丰富基本策略。
                         * 调用该方法的策略是基本策略。
                         * 便利方法，例如 forBoundedOutOfOrderness(Duration)，为常见的内置策略创建了 WatermarkStrategy。
                         * 该接口是可序列化的，因为水印策略可能会在分布式执行期间发送给operator。
                         */
                        WatermarkStrategy
                                //设置乱序延迟时间
                                .<Tuple2<String, Integer>>forBoundedOutOfOrderness(Duration.ofMillis(3))
                                //.withTimestampAssigner(TimestampAssigner（SerializableTimestampAssigner）.extractTimestamp)
                                // 分配时间戳，从event中提取
                                .withTimestampAssigner((event, timestamp) -> {
                                    System.out.println(timestamp + "\t" + event);
                                    return event.f1;
                                })
                                /**
                                 * 空闲检测。
                                 * 为水印策略添加空闲超时。
                                 * 如果在这段时间内没有记录在流的分区中流动，则该分区被视为“空闲”并且不会阻碍下游操作符中水印的进度。
                                 * 如果某些分区的数据很少并且在某些时期可能没有事件，如果没有空闲，这些流可能会延迟应用程序的整体事件时间进度。
                                 */
                                .withIdleness(Duration.ofMillis(5000))
                )
                .keyBy((KeySelector<Tuple2<String, Integer>, String>) value -> value.f0)
                .window(TumblingEventTimeWindows.of(Time.milliseconds(5)))
                .reduce((ReduceFunction<Tuple2<String, Integer>>) (e, ee) -> Tuple2.of(e.f0, e.f1 + ee.f1));

        dataSource1
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<Tuple2<String, Integer>>forBoundedOutOfOrderness(Duration.ofMillis(0))
                                .withTimestampAssigner((event, timestamp) -> event.f1) )
                .join(reduce)
                .where(r1 -> r1.f1)
                .equalTo(r2 -> r2.f1)
                .window(TumblingEventTimeWindows.of(Time.milliseconds(5)))
                .apply(new JoinFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> join(Tuple2<String, Integer> first, Tuple2<String, Integer> second) throws Exception {
                        return Tuple2.of(first.f0, first.f1 * second.f1);
                    }
                })
        .print().setParallelism(1);




        try {
            env.execute("watermark demo");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
