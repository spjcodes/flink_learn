package cn.jiayeli.doris;

import cn.jiayeli.doris.model.UserLog;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.avro.data.Json;
import org.apache.doris.flink.cfg.*;
import org.apache.doris.flink.datastream.DorisSourceFunction;
import org.apache.doris.flink.deserialization.SimpleListDeserializationSchema;
import org.apache.doris.flink.table.DorisDynamicTableSink;
import org.apache.doris.flink.table.DorisDynamicTableSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class DorisDemo {



    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.put("fenodes","node01:8030");
        properties.put("username","root");
        properties.put("password","root.123");
        properties.put("table.identifier","dblearn.movies");

        StreamExecutionEnvironment env = EnvUtil.get();


        env.setParallelism(1);
        /*env.addSource(new DorisSourceFunction(
                        new DorisStreamOptions(properties),
                        new SimpleListDeserializationSchema()
                )
        ).print();*/

        DataStreamSource<UserLog> datas = env.fromElements(
                new UserLog(1, 1, "jim", 2),
                new UserLog(2, 1, "grace", 2),
                new UserLog(3, 2, "tom", 2),
                new UserLog(4, 3, "bush", 3),
                new UserLog(5, 3, "helen", 3)
        );

        Properties pro = new Properties();
        pro.setProperty("format", "json");
        pro.setProperty("strip_outer_array", "true");
        datas
                .map(new RichMapFunction<UserLog, String>() {
                    transient Gson gson;
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        gson = new Gson();
                    }

                    @Override
                    public String map(UserLog value) throws Exception {
                        return gson.toJson(value);
                    }
                })
                .addSink(
                     DorisSink.sink(
                     DorisReadOptions.builder().build(),
                     DorisExecutionOptions.builder()
                     .setBatchSize(3)
                     .setBatchIntervalMs(0l)
                     .setMaxRetries(3)
                     .setStreamLoadProp(pro).build(),
                     DorisOptions.builder()
                     .setFenodes("node01:8030")
                     .setTableIdentifier("dblearn.table1")
                     .setUsername("root")
                     .setPassword("root.123").build()
                 ));

        env.execute();
    } 
}
