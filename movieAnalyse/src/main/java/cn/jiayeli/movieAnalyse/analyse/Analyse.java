package cn.jiayeli.movieAnalyse.analyse;

import cn.jiayeli.movieAnalyse.etl.UserMovieRatingInfoStream;
import cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule;
import cn.jiayeli.movieAnalyse.schema.UserMovieRatingAvroSchema;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Analyse {

    Logger logger = LoggerFactory.getLogger(Analyse.class.getName());

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(1);

        KafkaSource<UserMovieRatingInfoModule> userMovieRatingSource = KafkaSource
                .<UserMovieRatingInfoModule>builder()
                .setTopics("userMovieRating")
                .setBootstrapServers("node02:9092,node03:9092")
                .setGroupId("MLG1")
                .setValueOnlyDeserializer(new UserMovieRatingAvroSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();

        KafkaSource<UserMovieRatingInfoModule> userMovieRatingSourceByBI = KafkaSource
                .<UserMovieRatingInfoModule>builder()
                .setTopics("userMovieRating")
                .setBootstrapServers("node02:9092,node03:9092")
                .setGroupId("BIG1")
                .setValueOnlyDeserializer(new UserMovieRatingAvroSchema())
                .build();

        /**
         * send userMovieRating info to kafka userMovieRating
         * data demo:
         *  {"userId": "178", "age": "26", "gender": "M", "occupation": "other", "zipCode": "49512", "movieId": "568", "movieTitle": "Speed (1994)",
         *    "releaseDate": "01-Jan-1994", "videoReleaseDate": "", "IMDbURL": "http://us.imdb.com/M/title-exact?Speed%20(1994/I)", "type": "1|1|1",
         *    "rating": 4, "timestamp": "882826555"}
         */

        UserMovieRatingInfoStream userInfoStream = new UserMovieRatingInfoStream(env);

        userInfoStream.sink2kafka();
        //可写入hive或clickhouse，doris等进行adhoc分析，此处写入mysql方便进行结果认证
        userInfoStream.sink2Mysql();

        /*env.fromSource(userMovieRatingSourceByBI, WatermarkStrategy.forMonotonousTimestamps(), "userMovieRatingKafkaSourceForBI")
                .print();*/

        DataStreamSource<UserMovieRatingInfoModule> userMovieRatingStream = env.fromSource(userMovieRatingSource, WatermarkStrategy.forMonotonousTimestamps(), "userMovieRatingKafkaSource");

        Analyse analyse = new Analyse();
        //movie rating top N analyse
        analyse.movieRatingTopNAnalyse(userMovieRatingStream);

        //Top N most popular movies type of the year analyse
//        analyse.yearMovieTypeTopAnalyse(userMovieRatingStream);



        env.execute();
    }

    @Test
    public void yearMovieTypeTopAnalyse() throws ParseException {

    }




    public void movieRatingTopNAnalyse(DataStreamSource<UserMovieRatingInfoModule> userMovieRatingStream) {
        SingleOutputStreamOperator<Tuple3<String, String, Double>> movieRatingResult = userMovieRatingStream
                .map(new MapFunction<UserMovieRatingInfoModule, Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> map(UserMovieRatingInfoModule value) throws Exception {
                        return Tuple3.of(value.getMovieId(), value.getType(), Double.valueOf(value.getRating()));
                    }
                })
                .keyBy(movie -> movie.f1)
                /*   .reduce(new RichReduceFunction<Tuple3<String, String, Double>>() {
                       double ratingCount = 0;
                       double raterCount = 1;
                       @Override
                       public Tuple3<String, String, Double> reduce(Tuple3<String, String, Double> value1, Tuple3<String, String, Double> value2) throws Exception {
                           raterCount += 1;
                           ratingCount += raterCount == 2 ? value1.f2 + value1.f2 : value1.f2;
                           return Tuple3.of(value1.f0, value1.f1, ComputeUtils.avg(ratingCount, raterCount));
                       }
                   })*/
                .reduce(new RichReduceFunction<Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> reduce(Tuple3<String, String, Double> value1, Tuple3<String, String, Double> value2) throws Exception {
                        return Tuple3.of(value1.f0, value1.f1, value1.f2 + value2.f2);
                    }
                });

        movieRatingResult
                .map(new MapFunction<Tuple3<String, String, Double>, Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> map(Tuple3<String, String, Double> value) throws Exception {
                        logger.info("ts: " + System.currentTimeMillis() + "\t");
                        return value;
                    }
                })
                .print();
    }




}
