package cn.jiayeli.movieAnalyse.analyse;

import cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule;
import cn.jiayeli.movieAnalyse.schema.UserMovieRatingAvroSchema;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Analyse {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(1);

        KafkaSource<UserMovieRatingInfoModule> userMovieRatingSource = KafkaSource
                .<UserMovieRatingInfoModule>builder()
                .setTopics("userMovieRating")
                .setBootstrapServers("node02:9092,node03:9092")
                .setGroupId("MLG1")
                .setValueOnlyDeserializer(new UserMovieRatingAvroSchema())
                .build();

        KafkaSource<UserMovieRatingInfoModule> userMovieRatingSourceByBI = KafkaSource
                .<UserMovieRatingInfoModule>builder()
                .setTopics("userMovieRating")
                .setBootstrapServers("node02:9092,node03:9092")
                .setGroupId("BIG1")
                .setValueOnlyDeserializer(new UserMovieRatingAvroSchema())
                .build();

        /**
         * data demo:
         *  {"userId": "178", "age": "26", "gender": "M", "occupation": "other", "zipCode": "49512", "movieId": "568", "movieTitle": "Speed (1994)",
         *    "releaseDate": "01-Jan-1994", "videoReleaseDate": "", "IMDbURL": "http://us.imdb.com/M/title-exact?Speed%20(1994/I)", "type": "1|1|1",
         *    "rating": 4, "timestamp": "882826555"}
         */
        new UserInfoStream2Kafka().sink2kafka(env);

        /*env.fromSource(userMovieRatingSourceByBI, WatermarkStrategy.forMonotonousTimestamps(), "userMovieRatingKafkaSourceForBI")
                .print();*/

        env.fromSource(userMovieRatingSource, WatermarkStrategy.forMonotonousTimestamps(), "userMovieRatingKafkaSource")
                .map(new MapFunction<UserMovieRatingInfoModule, Tuple3<String, String, Double>>() {
                    @Override
                    public Tuple3<String, String, Double> map(UserMovieRatingInfoModule value) throws Exception {
                        return Tuple3.of(value.getMovieId(), value.getType(), Double.valueOf(value.getRating()));
                    }
                })
                .keyBy(movie -> movie.f1)
                .reduce(new RichReduceFunction<Tuple3<String, String, Double>>() {
                    double ratingCount = 0 ;
                    double raterCount = 1;
                    @Override
                    public Tuple3<String, String, Double> reduce(Tuple3<String, String, Double> value1, Tuple3<String, String, Double> value2) throws Exception {
                        raterCount += 1;
                        ratingCount += raterCount == 2 ? value1.f2 + value1.f2 : value1.f2;
                        return Tuple3.of(value1.f0, value1.f1, avg(ratingCount, raterCount));
                    }
                })
                .print();

        env.execute();
    }

    public static double avg(Double num1, Double number2) {
        return Math.round(num1/number2*100)/100.0;
    }
}
