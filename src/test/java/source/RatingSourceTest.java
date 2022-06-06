package source;

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
