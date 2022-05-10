package cn.jiayeli.movieAnalyse.analyse;

import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.commons.math3.fitting.leastsquares.EvaluationRmsChecker;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 维表处理成广播变量，或做成第三方缓存（redis）等可加快join的速度，但得考虑缓存对内存的占用问题
 */
public class TopN {

    private static StreamExecutionEnvironment env = EnvUtil.get();
    private static final HashMap<String, Tuple3<String, String, String>> movieInfoDataSet = getMovieInfoDataSet("src/main/resources/dataSet/u.item");



    public static void main(String[] args) {

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
                    .reduce((e, ee) -> Tuple2.of(e.f0, e.f1+ee.f1))
                    .process(new ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>>() {

                        transient TreeMap<Long, Tuple2<String, Long>> treeMap = null;
                        HashMap<String, Tuple2<String, Long>> map = new HashMap<>();

                        @Override
                        public void open(Configuration parameters) throws Exception {
                            super.open(parameters);
                           /* treeMap = new TreeMap<>(new Comparator<Long>() {
                                @Override
                                public int compare(Long e, Long ee) {
                                    return e > ee ? -1 : 1;
                                }
                            });*/
                        }


                        /**
                         * @param value
                         * @param ctx
                         * @param out
                         * @throws Exception
                         */
                        @Override
                        public void processElement(Tuple2<String, Long> value, ProcessFunction<Tuple2<String, Long>, Tuple2<String, Long>>.Context ctx, Collector<Tuple2<String, Long>> out) throws Exception {
                           /* treeMap.values().forEach(e -> {
                                if (e.f0.equals(value.f0)) {
                                    treeMap.remove(e.f1);
                                }
                            });
                            treeMap.put(value.f1, value);
                            if (treeMap.size() > 10) {
                                treeMap.pollLastEntry();
                            }
                            treeMap.forEach((k, v) -> out.collect(v));*/
                        }



                    })
                    .print();


            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String, Tuple3<String, String, String>> getMovieInfoDataSet(String fileName) {

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        HashMap<String, Tuple3<String, String, String>> movieInfos = new HashMap<>();

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] movieArray = line.split("\\|");
                if (movieArray.length == 24) {
                    movieInfos.put(movieArray[0], Tuple3.of(movieArray[0], movieArray[1], movieArray[4]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieInfos;
    }


}


