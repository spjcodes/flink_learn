package cn.jiayeli.join;

import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 电影实时评分
 */
public class BroadcastJoinDemo {

    private static StreamExecutionEnvironment env = EnvUtil.get();

    private static final Logger logger = LoggerFactory.getLogger(BroadcastJoinDemo.class.getName());



    public static void main(String[] args) throws Exception {
        env.setParallelism(1);
        MapStateDescriptor<Integer, Tuple2<Integer, String>> movieBroadcast
                = new MapStateDescriptor<Integer, Tuple2<Integer, String>>("movieBroadCaseState",
                Types.INT(),
                TypeInformation.of(new TypeHint<Tuple2<Integer, String>>() {}));

        BroadcastStream<Tuple2<Integer, String>> movieBroadcastStream = env
                .fromCollection(DataGenerator.getMovies())
                .broadcast(movieBroadcast);

        DataStreamSource<Tuple3<Integer, Integer, Long>> ratingInfo = DataGenerator.getRatingInfoStream(env);

        ratingInfo
                .connect(movieBroadcastStream)
                .process(new BroadcastProcessFunction<Tuple3<Integer, Integer, Long>, Tuple2<Integer, String>, Tuple3<String, Integer, Long>>() {

                        @Override
                        public void processElement(Tuple3<Integer, Integer, Long> value, BroadcastProcessFunction<Tuple3<Integer, Integer, Long>, Tuple2<Integer, String>, Tuple3<String, Integer, Long>>.ReadOnlyContext ctx, Collector<Tuple3<String, Integer, Long>> out) throws Exception {
                            ReadOnlyBroadcastState<Integer, Tuple2<Integer, String>> broadcastState = ctx.getBroadcastState(movieBroadcast);
                            String movieName = broadcastState.get(value.f0) != null ? broadcastState.get(value.f0).f1 : null;
                            out.collect(Tuple3.of(Objects.requireNonNullElseGet(movieName, () -> value.f0 + ""), value.f1, value.f2));
                        }

                        @Override
                        public void processBroadcastElement(Tuple2<Integer, String> value, BroadcastProcessFunction<Tuple3<Integer, Integer, Long>, Tuple2<Integer, String>, Tuple3<String, Integer, Long>>.Context ctx, Collector<Tuple3<String, Integer, Long>> out) throws Exception {
                            ctx.getBroadcastState(movieBroadcast).put(value.f0, value);
                        }
                    })
                .map(new MapFunction<Tuple3<String, Integer, Long>, Tuple3<String, Double, Long>>() {
                    @Override
                    public Tuple3<String, Double, Long> map(Tuple3<String, Integer, Long> value) throws Exception {
                      return Tuple3.of(value.f0, Double.valueOf(value.f1.toString()), value.f2);
                    }
                })
                .keyBy(e -> e.f0)
                .reduce(new ReduceFunction<Tuple3<String, Double, Long>>() {
                    double total = 1;
                    double totalRating = 0.0;
                    @Override
                    public Tuple3<String, Double, Long> reduce(Tuple3<String, Double, Long> value1, Tuple3<String, Double, Long> value2) throws Exception {
                            total += 1.0;
                            totalRating += total == 2 ? value1.f1 + value2.f1  : value2.f1;
                        return Tuple3.of(value1.f0, avg(totalRating, total), value2.f2);
                    }
                })
                .print();



        env.execute();
    }


    public static double avg(Double num1, Double number2) {
        return Math.round(num1/number2*100)/100.0;
    }

}

