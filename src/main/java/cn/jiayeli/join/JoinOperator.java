package cn.jiayeli.join;

import cn.jiayeli.utils.EnvUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JoinOperator {

    private static StreamExecutionEnvironment   env = EnvUtil.get();

    private static final Logger logger = LoggerFactory.getLogger(JoinOperator.class.getName());



    public static void main(String[] args) throws Exception {
        env.setParallelism(2);

        SingleOutputStreamOperator<Tuple2<String, Long>> exposureStream = env
                .addSource(new SourceFunction<Tuple2<String, Long>>() {
                               private boolean isRunning = true;
                               private final List<String> navbar = Arrays.asList("ad1", "ad2", "ad3", "ad4", "a", "ad6");
                               private final Random random = new Random();

                               @Override
                               public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
                                   while (isRunning) {
                                       TimeUnit.SECONDS.sleep(Math.abs(random.nextInt()) % 3);
                                       ctx.collect(Tuple2.of(navbar.get(Math.abs(random.nextInt()) % navbar.size()), System.currentTimeMillis()));
                                   }
                               }

                               @Override
                               public void cancel() {
                                   this.isRunning = false;
                               }
                           }

                )
                .assignTimestampsAndWatermarks(WatermarkStrategy
                        .<Tuple2<String, Long>>forBoundedOutOfOrderness(Duration.ofMillis(2000))
                        .withTimestampAssigner((e, l) -> e.f1));

        SingleOutputStreamOperator<Tuple4<String, String, String, Long>> clickEvents = env.addSource(new SourceFunction<Tuple4<String, String, String, Long>>() {
            private boolean isRunning = true;
            private final List<String> navbar = Arrays.asList("ad1", "ad2", "ad3", "ad4", "a", "ad6");
            private final List<String> events = Arrays.asList("click", "browse", "favorites", "buy", "cancel", "add");
            private final List<String> userList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
            private final Random random = new Random();

            @Override
            public void run(SourceContext<Tuple4<String, String, String, Long>> ctx) throws Exception {
                while (isRunning) {
                    TimeUnit.SECONDS.sleep(Math.abs(random.nextInt()) % 2);
                    ctx.collect(Tuple4.of(userList.get(Math.abs(random.nextInt() % userList.size())),
                                    navbar.get(Math.abs(random.nextInt()) % navbar.size()),
                                    events.get(Math.abs(random.nextInt()) % events.size()),
                                    System.currentTimeMillis() )
                    );
                }
            }

            @Override
            public void cancel() {
                this.isRunning = false;
            }
        }
        )
        .assignTimestampsAndWatermarks( WatermarkStrategy
            .<Tuple4<String, String, String, Long>>forBoundedOutOfOrderness(Duration.ofMillis(2000))
            .withTimestampAssigner((e, l) -> e.f3));

/*        exposureStream
            .map(new MapFunction<Tuple2<String, Long>, Tuple2<String, Long>> () {

                @Override
                public Tuple2<String, Long> map(Tuple2<String, Long> value) throws Exception {
                    logger.info("exposure event:\t[" + value + "]");
                    return value;
                }
            } )
            .cn.jiayeli.join( clickEvents.map(new MapFunction<Tuple4<String, String, String, Long>, Tuple4<String, String, String, Long>>() {
                @Override
                public Tuple4<String, String, String, Long> map(Tuple4<String, String, String, Long> value) throws Exception {
                    logger.info("clickEvent:\t[" + value + "]");
                    return value;
                }
            }))
            .where(exposureS  -> exposureS.f0)
            .equalTo(clickEvent  -> clickEvent.f1)
            .window(TumblingEventTimeWindows.of(Time.seconds(3)))
            .apply(new JoinFunction<Tuple2<String, Long>, Tuple4<String, String, String, Long>, Tuple5<String, String, String, Long, Long>>() {
                @Override
                public Tuple5<String, String, String, Long, Long> cn.jiayeli.join(Tuple2<String, Long> exposure, Tuple4<String, String, String, Long> event) throws Exception {
                    return Tuple5.of(event.f0, event.f1, event.f2, 1L, exposure.f1);
                }

            })
            .print();*/

        clickEvents
                .keyBy(e -> e.f1)
                .intervalJoin(exposureStream.keyBy(e -> e.f0))
                .between(Time.seconds(-1), Time.seconds(2))
                .process(new ProcessJoinFunction<Tuple4<String, String, String, Long>, Tuple2<String, Long>, Tuple3<String, Boolean, Long>>() {
                    @Override
                    public void processElement(Tuple4<String, String, String, Long> clickEvent, Tuple2<String, Long> exposure, ProcessJoinFunction<Tuple4<String, String, String, Long>, Tuple2<String, Long>, Tuple3<String, Boolean, Long>>.Context ctx, Collector<Tuple3<String, Boolean, Long>> out) throws Exception {
                        System.out.println("getLeftTimestamp\t" + ctx.getLeftTimestamp());
                        System.out.println("getRightTimestamp\t" + ctx.getRightTimestamp());
                        System.out.println("getTimestamp\t" + ctx.getTimestamp());
                        out.collect(Tuple3.of(clickEvent.f1, true, exposure.f1));
                    }
                })
                .print();




       env.execute();
    }
}
