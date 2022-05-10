package cn.jiayeli.Test.test;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.UserModule;
import cn.jiayeli.movieAnalyse.util.DataParseUtil;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

    @Test
    public void t() throws ExecutionException, InterruptedException {
        FutureTask<HashMap<String, UserModule>> getUserDataSetThread = new FutureTask<>((Callable<HashMap<String, UserModule>>) () -> {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("b");
            return new DataParseUtil().getUserDataSetByFile(new File("/home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/dataSet/u.user"));
        });

        FutureTask<HashMap<String, MovieModule>> getMovieDataSetThread = new FutureTask<>((Callable<HashMap<String, MovieModule>>) () -> {
            System.out.println("mm");
            return new DataParseUtil().getMovieModuleDataSet(new File("/home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/dataSet/u.item"));
        });

        new Thread(getMovieDataSetThread).start();
        new Thread(getUserDataSetThread).start();
        HashMap<String, MovieModule> movieInfoDataSet = getMovieDataSetThread.get();
        HashMap<String, UserModule> userDataSet = getUserDataSetThread.get();
        System.out.println("a");
        System.out.println("m");
    }
}
