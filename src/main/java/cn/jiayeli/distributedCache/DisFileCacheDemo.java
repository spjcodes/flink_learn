package cn.jiayeli.distributedCache;

import cn.jiayeli.utils.EnvUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

public class DisFileCacheDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = EnvUtil.get();
        final String distributCacheFile = "movieInfoFile";
        /**
         * 在给定名称下在分布式缓存中注册文件。
         * 可以从本地路径下（分布式）运行时中的任何用户定义函数访问该文件。
         * 文件可以是本地文件（将通过 BlobServer 分发），也可以是分布式文件系统中的文件。
         * 如果需要，运行时会将文件临时复制到本地缓存。
         */

        // 1. register
        env.registerCachedFile("file:////home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/dataSet/movieInfo.data", distributCacheFile, false);

        env.fromCollection(Arrays.asList(Tuple2.of("1", 4.0F), Tuple2.of("2", 4.5F), Tuple2.of("3", 5.0F)))
                .flatMap(new RichFlatMapFunction<Tuple2<String, Float>, Tuple2<String, Float>>() {

                    File file = null;
                    FileReader fileReader = null;
                    HashMap<String, String> movieInfo = new HashMap<>();
                    BufferedReader bufferedReader = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);

        //2. get
                        File file = getRuntimeContext().getDistributedCache().getFile(distributCacheFile);
                        fileReader = new FileReader(file);
                        bufferedReader = new BufferedReader(fileReader);
                        String line = null;
                        String[] split = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            split = line.split("\\|");
                            movieInfo.put(split[0], split[1]);
                        }
                    }

                    @Override
                    public void flatMap(Tuple2<String, Float> value, Collector<Tuple2<String, Float>> out) throws Exception {
                      out.collect(Tuple2.of(movieInfo.get(value.f0), value.f1));
                    }

                    @Override
                    public void close() throws Exception {
                        super.close();
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (fileReader != null) {
                            fileReader.close();
                        }
                    }
                })
                .print();

        env.execute();
    }
}
