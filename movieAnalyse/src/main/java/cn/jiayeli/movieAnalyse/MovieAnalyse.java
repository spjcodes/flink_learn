package cn.jiayeli.movieAnalyse;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.source.MovieInfoSourceFunction;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;
import org.apache.flink.util.Collector;
import org.codehaus.jackson.map.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieAnalyse {

    private static final Configuration conf = new Configuration();
    private static  StreamExecutionEnvironment env = EnvUtil.get();

    private static Logger logger = LoggerFactory.getLogger(MovieAnalyse.class.getName());


    public static void main(String[] args) throws InterruptedException {

        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple4<String, String, Long, String>> movieDataStreamSource = env
                .addSource(new MovieInfoSourceFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<MovieModule>forMonotonousTimestamps()
                )
                .map(new MapFunction<MovieModule, Tuple4<String, String, Long, String>>() {
                    @Override
                    public Tuple4<String, String, Long, String> map(MovieModule movie) throws Exception {
                        return Tuple4.of(String.valueOf(movie.getMovieId()), String.valueOf(movie.getMovieTitle()), 0L, "");
                    }
                });


        SingleOutputStreamOperator<Tuple4<String, String, Long, String>> ratingDataStreamSource = env
                .addSource(new RatingInfoSourceFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                        .forMonotonousTimestamps()
                )
                .map(new MapFunction<RatingModule, Tuple4<String, String, Long, String>>() {
                    @Override
                    public Tuple4<String, String, Long, String> map(RatingModule rating) throws Exception {
                        return Tuple4.of(String.valueOf(rating.getItemId()), String.valueOf(rating.getUserId()), Long.valueOf(rating.getRating()), String.valueOf(rating.getTimestamp()));
                    }
                });

        /**
         * movie:<movieid, title, 0, "">
         * ratin:<movieid, userid, rating, time>
         */
      /* movieDataStreamSource
           .connect(ratingDataStreamSource)
           .keyBy(movie -> movie.f0, rating -> rating.f0)
           .process(new KeyedCoProcessFunction<String, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple3<String, String, Long>>() {

               String movieId = null;
               HashMap<String, Tuple4<String, String, Long, String>> movieInfos = new HashMap<>();
               @Override
               public void processElement1(Tuple4<String, String, Long, String> value, KeyedCoProcessFunction<String, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple3<String, String, Long>>.Context ctx, Collector<Tuple3<String, String, Long>> out) throws Exception {
                   movieId = ctx.getCurrentKey();
                   movieInfos.put(movieId, value);
               }

               @Override
               public void processElement2(Tuple4<String, String, Long, String> value, KeyedCoProcessFunction<String, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple3<String, String, Long>>.Context ctx, Collector<Tuple3<String, String, Long>> out) throws Exception {
                   Tuple4<String, String, Long, String> movieInfo = movieInfos.get(value.f1);
                   if (movieInfo != null) {
                       out.collect(Tuple3.of(value.f0, movieInfo.f1, value.f2));
                   }
               }
           })
           .print();*/



        MapStateDescriptor<String, Tuple4<String, String, Long, String>> moviBroadcastStateDesc
                = new MapStateDescriptor<>("movieBroadcastDesc", Types.STRING(), TypeInformation.of(new TypeHint<Tuple4<String, String, Long, String>>() {
        }));

        HashMap<String, Tuple4<String, String, Long, String>> movieDataSet = new HashMap<>();

        movieDataStreamSource.map(new MapFunction<Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>>() {

            @Override
            public Tuple4<String, String, Long, String> map(Tuple4<String, String, Long, String> value) throws Exception {
                return value;
            }

        });

        BroadcastStream<Tuple4<String, String, Long, String>> movieBroadcast = movieDataStreamSource.broadcast(moviBroadcastStateDesc);

        ratingDataStreamSource
                .connect(movieBroadcast)
                .process(new BroadcastProcessFunction<Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>>() {

                    //rating info cache
                    HashMap<String, Tuple4<String, String, Long, String>> ratingInfos = new HashMap<>();

                    @Override
                    public void processElement(Tuple4<String, String, Long, String> rating, BroadcastProcessFunction<Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>>.ReadOnlyContext ctx, Collector<Tuple4<String, String, Long, String>> out) throws Exception {
                        Tuple4<String, String, Long, String> movieInfo = ctx.getBroadcastState(moviBroadcastStateDesc).get(rating.f0);
                        ratingInfos.put(rating.f0, rating);
                        if (movieInfo != null && ratingInfos.get(movieInfo.f0) != null ) {
                            out.collect(Tuple4.of(movieInfo == null ? null : movieInfo.f1, rating.f0, rating.f2, rating.f3));
                            ratingInfos.remove(rating.f0);
                        }
                    }

                    @Override
                    public void processBroadcastElement(Tuple4<String, String, Long, String> value, BroadcastProcessFunction<Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>>.Context ctx, Collector<Tuple4<String, String, Long, String>> out) throws Exception {
                        ctx.getBroadcastState(moviBroadcastStateDesc).put(value.f0, value);
                    }

                })
                .keyBy(e -> e.f0)
                .reduce((e, ee) -> Tuple4.of(e.f0, e.f1, e.f2, e.f3))
                //topN
                .flatMap(new RichFlatMapFunction<Tuple4<String, String, Long, String>, Tuple3<String, Long, String>>() {

                    LinkedList<Tuple3<String, Long, String>> collect;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        collect = new LinkedList<>();
                    }

                    @Override
                    public void flatMap(Tuple4<String, String, Long, String> value, Collector<Tuple3<String, Long, String>> out) throws Exception {
                        collect.add(Tuple3.of(value.f0, value.f2, value.f3));
                        Stream<Tuple3<String, Long, String>> topN = new ArrayList<>(collect)
                                .stream()
                                .sorted(new Comparator<Tuple3<String, Long, String>>() {
                                    @Override
                                    public int compare(Tuple3<String, Long, String> e, Tuple3<String, Long, String> ee) {
                                        return -e.f1.compareTo(ee.f1);
                                    }
                                })
                                .limit(10);
                        List<Tuple3<String, Long, String>> tops = topN.collect(Collectors.toList());

                        collect.forEach(out::collect);
                        if (this.collect.size() > 10) {
                            this.collect.clear();
                            this.collect.addAll(tops);
                        }
                    }

                  /*
                  //
                    TreeMap<Tuple2<String, Long>, Tuple3<String, Long, String>> topN = null;
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        topN = new TreeMap<Tuple2<String, Long>, Tuple3<String, Long, String>>((e, ee) -> e.f1.compareTo(ee.f1));
                    }

                    @Override
                    public void flatMap(Tuple4<String, String, Long, String> value, Collector<Tuple3<String, Long, String>> out) throws Exception {
                        topN.put(Tuple2.of(value.f0, value.f2), Tuple3.of(value.f0, value.f2, value.f3));
                        if (topN.size() > 10) {
                            topN.pollLastEntry();
                        }
                        topN.forEach((k, v) -> out.collect(v));
                    }*/



                })
                .print();

        try {
            env.execute("movie rating analyse");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
