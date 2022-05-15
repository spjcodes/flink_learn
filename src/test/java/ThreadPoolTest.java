import cn.jiayeli.movieAnalyse.module.MovieModule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {


    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                5,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(5),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("a");
            }
        });

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("B");
            }
        });
    }

    @Test
    public void t() {
        System.out.println(new MovieModule().getReleaseDate());
        System.out.println(new MovieModule().getReleaseDate() == null);

    }



}
