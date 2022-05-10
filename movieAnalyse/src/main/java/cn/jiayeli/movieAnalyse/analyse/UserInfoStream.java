package cn.jiayeli.movieAnalyse.analyse;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.module.UserModule;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.DataParseUtil;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * user：id:58|age:27|gender:M|job: programmer|emailCode: 52246
 * movie：id:8|title:Babe (1995)|releaseData: 01-Jan-1995||url: http://us.imdb.com/M/title-exact?Babe%20(1995)|types: 0|0|0|0|1|1|0|0|1|0|0|0|0|0|0|0|0|0|0
 * rating：userid:840	movieid:432	rating:5	time:891209342
 */

public class UserInfoStream {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();
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

        env.addSource(new RatingInfoSourceFunction())
                .flatMap(new RichFlatMapFunction<RatingModule, Tuple3<UserModule, RatingModule, MovieModule>>() {
                    HashMap<String, UserModule> userDataSet ;
                    HashMap<String, MovieModule> movieInfoDataSet;
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        userDataSet = new DataParseUtil().getUserDataSetByFile(getRuntimeContext().getDistributedCache().getFile("userFile"));
                        movieInfoDataSet = new DataParseUtil().getMovieModuleDataSet(getRuntimeContext().getDistributedCache().getFile("movieFile"));
                    }

                    @Override
                    public void flatMap(RatingModule ratingModule, Collector<Tuple3<UserModule, RatingModule, MovieModule>> out) throws Exception {
                        out.collect(Tuple3.of(userDataSet.get(ratingModule.getUserId()), ratingModule, movieInfoDataSet.get(ratingModule.getItemId())));
                    }
                })
                .print();

        env.execute();
    }


}
