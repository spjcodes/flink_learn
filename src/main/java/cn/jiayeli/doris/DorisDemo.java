package cn.jiayeli.doris;

import cn.jiayeli.movieAnalyse.util.EnvUtil;
import lombok.SneakyThrows;
import org.apache.doris.flink.cfg.DorisStreamOptions;
import org.apache.doris.flink.datastream.DorisSourceFunction;
import org.apache.doris.flink.deserialization.SimpleListDeserializationSchema;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class DorisDemo {

    @SneakyThrows
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("fenodes","node01:8030");
        properties.put("username","root");
        properties.put("password","root.123");
        properties.put("table.identifier","dblearn.movies");

        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(1);
        env.addSource(new DorisSourceFunction(
                        new DorisStreamOptions(properties),
                        new SimpleListDeserializationSchema()
                )
        ).print();

        env.execute();
    }
}
