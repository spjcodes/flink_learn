package cn.jiayeli.movieAnalyse.analyse;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.module.UserModule;
import cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule;
import cn.jiayeli.movieAnalyse.schema.UserMovieRatingAvroSchema;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.DataParseUtil;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.HashMap;

/**
 * 将用户信息，电影信息和评分信息进行聚合，并写入kafka
 *  kafka-topics --zookeeper node02:2181 --create --topic userMovieRating --replication-factor 2 --partitions 3
 * user：id:58|age:27|gender:M|job: programmer|emailCode: 52246
 * movie：id:8|title:Babe (1995)|releaseData: 01-Jan-1995||url: http://us.imdb.com/M/title-exact?Babe%20(1995)|types: 0|0|0|0|1|1|0|0|1|0|0|0|0|0|0|0|0|0|0
 * rating：userid:840	movieid:432	rating:5	time:891209342
 */

public class UserInfoStream2Kafka {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();
        new UserInfoStream2Kafka().sink2kafka(env);
        env.execute();
    }

    public void sink2kafka(StreamExecutionEnvironment env) {
            //register distribute cache file
            env.registerCachedFile("src/main/resources/dataSet/u.user", "userFile");
            env.registerCachedFile("src/main/resources/dataSet/u.item", "movieFile");

      /*have a exception unread block data at java.base/java.io.ObjectInputStream$BlockDataInputStrea
        FutureTask<HashMap<String, UserModule>> getUserDataSetThread = new FutureTask<>((Callable<HashMap<String, UserModule>>) () -> {
            return new DataParseUtil().getUserDataSetByFile("src/main/resources/dataSet/u.user");
        });
        FutureTask<HashMap<String, MovieModule>> getMovieDataSetThread = new FutureTask<>((Callable<HashMap<String, MovieModule>>) () -> {
            return new DataParseUtil().getMovieModuleDataSet("src/main/resources/dataSet/u.item");
        });
        new Thread(getUserDataSetThread).start();
        new Thread(getMovieDataSetThread).start();
        HashMap<String, UserModule> userDataSet = getUserDataSetThread.get();
        HashMap<String, MovieModule> movieInfoDataSet = getMovieDataSetThread.get();
        */

            KafkaSink<UserMovieRatingInfoModule> sink = KafkaSink.<UserMovieRatingInfoModule>builder()
                    .setBootstrapServers("node02:9092,node03:9092")
                    .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                            .setTopic("userMovieRating")
                            .setValueSerializationSchema(new UserMovieRatingAvroSchema())
                            .build()
                    )
                    .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                    .build();

            env.addSource(new RatingInfoSourceFunction())
                    .flatMap(new RichFlatMapFunction<RatingModule, Tuple3<UserModule, RatingModule, MovieModule>>() {
                        HashMap<String, UserModule> userDataSet ;
                        HashMap<String, MovieModule> movieInfoDataSet;
                        @Override
                        public void open(Configuration parameters) throws Exception {
                            super.open(parameters);
                            //get cache file
                            userDataSet = new DataParseUtil().getUserDataSetByFile(getRuntimeContext().getDistributedCache().getFile("userFile"));
                            movieInfoDataSet = new DataParseUtil().getMovieModuleDataSet(getRuntimeContext().getDistributedCache().getFile("movieFile"));
                        }

                        @Override
                        public void flatMap(RatingModule ratingModule, Collector<Tuple3<UserModule, RatingModule, MovieModule>> out) throws Exception {
                            out.collect(Tuple3.of(userDataSet.get(ratingModule.getUserId()), ratingModule, movieInfoDataSet.get(ratingModule.getItemId())));
                        }
                    })
                    .flatMap(new FlatMapFunction<Tuple3<UserModule, RatingModule, MovieModule>, UserMovieRatingInfoModule>() {
                        @Override
                        public void flatMap(Tuple3<UserModule, RatingModule, MovieModule> value, Collector<UserMovieRatingInfoModule> out) throws Exception {
                            String type = "";
                            UserMovieRatingInfoModule record = new UserMovieRatingInfoModule();
                            record.setGender(value.f0.getGender().toString());
                            record.setIMDbURL(value.f2.getIMDbURL());
                            record.setMovieId(value.f2.getMovieId());
                            record.setOccupation(value.f0.getOccupation().toString());
                            record.setRating(value.f1.getRating());
                            record.setUserId(value.f0.getUserId().toString());
                            record.setZipCode(value.f0.getZipCode().toString());
                            record.setMovieTitle(value.f2.getMovieTitle());
                            record.setReleaseDate(value.f2.getReleaseDate());
                            record.setVideoReleaseDate(value.f2.getVideoReleaseDate());
                            record.setAge(value.f0.getAge().toString());

                            if (!value.f2.getUnknown().equals("0")) {
                                type += "unknown" + "|";
                            }
                            if (!value.f2.getAction().equals("0")) {
                                type += "action" + "|";
                            }
                            if (!value.f2.getAdventure().equals("0")) {
                                type += "adventure" + "|";
                            }
                            if (!value.f2.getAnimation().equals("0")) {
                                type += "animation" + "|";
                            }
                            if (!value.f2.getChildrens().equals("0")) {
                                type += "childrens" + "|";
                            }
                            if (!value.f2.getComedy().equals("0")) {
                                type += "comedy" + "|";
                            }
                            if (!value.f2.getCrime().equals("0")) {
                                type +="crime" + "|";
                            }
                            if (!value.f2.getDocumentary().equals("0")) {
                                type += "documentary" + "|";
                            }
                            if (!value.f2.getDrama().equals("0")) {
                                type += "drama" + "|";
                            }
                            if (!value.f2.getFantasy().equals("0")) {
                                type += "fantasy" + "|";
                            }
                            if (!value.f2.getFilmNoir().equals("0")) {
                                type += "filmNoir" + "|";
                            }
                            if (!value.f2.getHorror().equals("0")) {
                                type += "horror" + "|";
                            }
                            if (!value.f2.getMusical().equals("0")) {
                                type += "musical" + "|";
                            }
                            if (!value.f2.getMystery().equals("0")) {
                                type += "mystery" + "|";
                            }
                            if (!value.f2.getRomance().equals("0")) {
                                type += "romance" + "|";
                            }
                            if (!value.f2.getSciFi().equals("0")) {
                                type += "sciFi" + "|";
                            }
                            if (!value.f2.getThriller().equals("0")) {
                                type += "thriller" + "|";
                            }
                            if (!value.f2.getWar().equals("0")) {
                                type += "war" + "|";
                            }
                            if (!value.f2.getWestern().equals("0")) {
                                type += "western" + "|";
                            }

                            record.setType(type.substring(0, type.length()-1));
                            record.setTimestamp(value.f1.getTimestamp().toString());

                            out.collect(record);
                        }
                    })
                    .sinkTo(sink);
//              .print();

    }


}
