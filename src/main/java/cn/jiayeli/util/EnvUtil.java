package cn.jiayeli.util;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class EnvUtil {
    public static StreamExecutionEnvironment get() {
        return new StreamExecutionEnvironment();
    }
}
