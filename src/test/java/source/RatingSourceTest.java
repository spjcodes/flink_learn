package source;

import cn.jiayeli.movieAnalyse.source.MovieInfoSourceFunction;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import cn.jiayeli.util.EnvUtil;

public class RatingSourceTest {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();

        env
            .addSource(new RatingInfoSourceFunction()).setParallelism(1)
            .print();

        env.execute();
    }
}
