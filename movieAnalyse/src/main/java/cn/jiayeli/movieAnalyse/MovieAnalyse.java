package cn.jiayeli.movieAnalyse;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.source.MovieInfoSourceFunction;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoMapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieAnalyse {

    private static final Configuration conf = new Configuration();
    private static  StreamExecutionEnvironment env = EnvUtil.get();

    private static Logger logger = LoggerFactory.getLogger(MovieAnalyse.class.getName());


    public static void main(String[] args) {

        env.setParallelism(20);
        SingleOutputStreamOperator<Tuple4<String, String, Long, String>> movieDataStreamSource = env
                .addSource(new MovieInfoSourceFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<MovieModule>forMonotonousTimestamps()
                )
                .map(new MapFunction<MovieModule, Tuple4<String, String, Long, String>>() {
                    @Override
                    public Tuple4<String, String, Long, String> map(MovieModule movie) throws Exception {
                        return Tuple4.of(String.valueOf(movie.getMovieId()), String.valueOf(movie.getMovieTitle()), 0l, "");
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

        movieDataStreamSource
            .map(new RichMapFunction<Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>>() {
                @Override
                public Tuple4<String, String, Long, String> map(Tuple4<String, String, Long, String> value) throws Exception {
                    logger.info("task id" + getRuntimeContext().getTaskName() + "\t" + getRuntimeContext().getTaskNameWithSubtasks());
                    return value;
                }
            })
            .print();

       movieDataStreamSource
           .connect(ratingDataStreamSource)
           .keyBy(movie -> movie.f0, rating -> rating.f0)
           .map(new RichCoMapFunction<Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>, Tuple4<String, String, Long, String>>() {

               @Override
               public Tuple4<String, String, Long, String> map1(Tuple4<String, String, Long, String> value) throws Exception {
                   return value;
               }

               @Override
               public Tuple4<String, String, Long, String> map2(Tuple4<String, String, Long, String> value) throws Exception {
                   return value;
               }
           })
           .print();

        try {
            env.execute("movie rating analyse");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
