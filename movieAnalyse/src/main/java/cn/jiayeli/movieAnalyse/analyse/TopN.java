package cn.jiayeli.movieAnalyse.analyse;

import cn.jiayeli.movieAnalyse.functions.impl.TopNStateReduceFunction;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.DataParseUtil;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import com.mysql.cj.jdbc.Driver;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 维表处理成广播变量，或做成第三方缓存（redis）等可加快join的速度，但得考虑缓存对内存的占用问题
 */
public class TopN {

    private static StreamExecutionEnvironment env = EnvUtil.get();
    private static final HashMap<String, Tuple3<String, String, String>> movieInfoDataSet = DataParseUtil.getMovieInfoDataSet("src/main/resources/dataSet/u.item");



    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(TopN.class.getName());

        try {
            env.setParallelism(40);
            env
                    .addSource(new RatingInfoSourceFunction())
                    .assignTimestampsAndWatermarks(
                            WatermarkStrategy.<RatingModule>forMonotonousTimestamps()
                                    .withTimestampAssigner((e, t) -> Long.parseLong(e.getTimestamp().toString())))
                    .map(new RichMapFunction<RatingModule, Tuple2<String, Long>>() {
                        @Override
                        public Tuple2<String, Long> map(RatingModule value) throws Exception {
                            Tuple3<String, String, String> movieInfo = movieInfoDataSet.get(value.getItemId());
                            if (movieInfo != null) {
                                return Tuple2.of(movieInfo.f1, Long.valueOf(value.getRating()));
                            }
                            System.out.println("-------------------------------join fail：\t" + value.toString());
                            return null;
                        }
                    })
                    .keyBy(e -> e.f0)
                    .reduce(new TopNStateReduceFunction())
                    .process(new ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>>() {

//                        transient TreeMap<Long, Tuple2<String, Long>> treeMap = null;
                        HashMap<String, Tuple2<String, Long>> map = new HashMap<>();

                        @Override
                        public void open(Configuration parameters) throws Exception {
                            super.open(parameters);

                        }


                        /**
                         * @param value
                         * @param ctx
                         * @param out
                         * @throws Exception
                         */
                        @Override
                        public void processElement(Tuple2<String, Long> value, ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>>.Context ctx, Collector<Tuple2<String, Long>> out) throws Exception {
                            map.put(value.f0, value);
                            List<Tuple2<String, Long>> list = map.values().stream().sorted((e, ee) -> -e.f1.compareTo(ee.f1)).collect(Collectors.toList());
                            if (list.size() > 10) {
                                list.remove(10);
                            }
                            map.clear();
                            list.forEach(e -> {
                                out.collect(e);
                                logger.info("collect topN:\t" + e.toString());
                                map.put(e.f0, e);
                            });
                           
                        }
                    })
                    .addSink(JdbcSink.sink(
                            "INSERT INTO movieInfo.topN (movieName, ratings) VALUES(?, ?) on duplicate key update movieName = ?, ratings = ?",
                            (ps, e) -> {
                                ps.setString(1, e.f0);
                                ps.setLong(2, e.f1);
                                ps.setString(3, e.f0);
                                ps.setLong(4, e.f1);
                            },
                            JdbcExecutionOptions.builder()
                                    .withBatchSize(2)
                                    .withBatchIntervalMs(100)
                                    .build(),
                            new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                    .withUrl("jdbc:mysql://jiayeli:3306/movieInfo")
                                    .withUsername("kuro")
                                    .withPassword("kuro.123")
                                    .withDriverName(Driver.class.getName())
                                    .build()));
//                    .print();


            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}


