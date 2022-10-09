package cn.jiayeli.window;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.evictors.Evictor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.runtime.operators.windowing.TimestampedValue;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @author: jiayeli.cn
 * @description
 * @date: 2022/9/26 下午8:50
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        SingleOutputStreamOperator<Tuple3<String, Long, Integer>> haveTimeWords = env.socketTextStream("localhost", 10010)
                .flatMap(wordsSplit())
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Tuple3<String, Long, Integer>>forMonotonousTimestamps()
                        .withTimestampAssigner((e, t) -> e.f1)
                );

        haveTimeWords.print("source");
        
        haveTimeWords
                .map(trans2Tup2())
                .keyBy(wordPartition())
                .window(TumblingEventTimeWindows.of(Time.milliseconds(5)))
                .trigger(new Trigger<Tuple2<String, Integer>, TimeWindow>() {
                    @Override
                    public TriggerResult onElement(Tuple2<String, Integer> element, long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
                        if (element.f1 % 2 == 0) {
                            return TriggerResult.FIRE_AND_PURGE;

                        }
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onProcessingTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
                        return null;
                    }

                    @Override
                    public TriggerResult onEventTime(long l, TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {
                        return null;
                    }

                    @Override
                    public void clear(TimeWindow timeWindow, TriggerContext triggerContext) throws Exception {

                    }
                })
                .evictor(new Evictor<Tuple2<String, Integer>, TimeWindow>() {
                    @Override
                    public void evictBefore(Iterable<TimestampedValue<Tuple2<String, Integer>>> elements, int size, TimeWindow window, EvictorContext evictorContext) {

                    }

                    @Override
                    public void evictAfter(Iterable<TimestampedValue<Tuple2<String, Integer>>> elements, int size, TimeWindow window, EvictorContext evictorContext) {

                    }
                })
                .reduce(wordCount())
                .map(printStream())
                ;

        env.execute("socketWordCountDemo");
    }

    private static MapFunction<Tuple3<String, Long, Integer>, Tuple2<String, Integer>> trans2Tup2() {
        return new MapFunction<Tuple3<String, Long, Integer>, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(Tuple3<String, Long, Integer> haveTimeTuple3) throws Exception {
                return Tuple2.of(haveTimeTuple3.f0, haveTimeTuple3.f2);
            }
        };
    }

    private static RichMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>> printStream() {
        return new RichMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                System.out.println("register operator........................");
            }

            @Override
            public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
                System.out.println(" output ====================>\t" + value);
                return value;
            }

            @Override
            public void close() throws Exception {
                super.close();
                System.out.println("-----------------end------------------------");
            }
        };
    }

    private static ReduceFunction<Tuple2<String, Integer>> wordCount() {
        return new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> fistTup2, Tuple2<String, Integer> secondTup2) throws Exception {
                System.out.println("process..");
                return Tuple2.of(fistTup2.f0, fistTup2.f1 + secondTup2.f1);
            }
        };
    }

    private static KeySelector<Tuple2<String, Integer>, String> wordPartition() {
        return new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> wordAndOne) throws Exception {
                return wordAndOne.f0;
            }
        };
    }

    private static FlatMapFunction<String, Tuple3<String, Long, Integer>> wordsSplit() {
        return new FlatMapFunction<String, Tuple3<String, Long, Integer>>() {
            @Override
            public void flatMap(String words, Collector<Tuple3<String, Long, Integer>> collector) throws Exception {
                    Arrays.stream(words.split("\\s+"))
                            .forEach(word -> collector.collect(Tuple3.of(
                                    words.substring(0, word.indexOf("_")),
                                    Long.valueOf(word.substring(word.indexOf("_") + 1)),
                                    1)));
            }
        };
    }


}
