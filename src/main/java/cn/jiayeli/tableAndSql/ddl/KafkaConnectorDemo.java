package cn.jiayeli.tableAndSql.ddl;

import cn.jiayeli.util.EnvUtil;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KafkaConnectorDemo {
    public static void main(String[] args) throws Exception {
        executorDataGenerator();
    }

    public static void executorDataGenerator() throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(10);
        env.addSource(new RichParallelSourceFunction<String>() {

            boolean isRunning = true;
            List<Integer> users;
            List<Integer> items;
            List<String> behavior;
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                users = Arrays.asList(1, 2, 3, 4, 5, 9, 10);
                items = Arrays.asList(10010, 20012, 19903, 10084, 23521, 34459, 10234);
                behavior = Arrays.asList("view", "click", "buy", "collect", "click", "buy", "collect");
            }

            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                int n;
                Gson gson = new Gson();
                Random random = new Random();
                while (isRunning) {
                    n = Math.abs(random.nextInt() % users.size());
                    TimeUnit.SECONDS.sleep(n);
                    ctx.collect(gson.toJson(new UserBehavior(users.get(n), items.get(n), behavior.get(n))));
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        }).sinkTo(
                KafkaSink.<String>builder()
                        .setBootstrapServers("node02:9092,node03:9092,node05:9092")
                        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                                .setTopic("user_behavior")
                                .setValueSerializationSchema(new SimpleStringSchema())
                                .build()
                        )
                        .build()
        );

        env.addSource(new RichParallelSourceFunction<String>() {

            boolean isRunning = true;
            List<Integer> prices;
            List<Integer> items;
            List<String> names;
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                prices = Arrays.asList(10000, 4552, 3454, 34344, 5666, 944343, 34434410);
                items = Arrays.asList(10010, 20012, 19903, 10084, 23521, 34459, 10234);
                names = Arrays.asList("macbookpro", "macbookAit", "hpPc", "huaweip10", "uniqloCap", "feifierC", "clickMe");
            }

            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                int n;
                Gson gson = new Gson();
                Random random = new Random();
                while (isRunning) {
                    n = Math.abs(random.nextInt() % items.size());
                    TimeUnit.SECONDS.sleep(n);
                    ctx.collect(gson.toJson(new ProduceInfo(items.get(n), names.get(n), prices.get(n))));
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        }).sinkTo(
                KafkaSink.<String>builder()
                        .setBootstrapServers("node02:9092,node03:9092,node05:9092")
                        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                                .setTopic("produceInfo")
                                .setValueSerializationSchema(new SimpleStringSchema())
                                .build()
                        ).build()
        );

        env.execute();
    }
}

@Data
@AllArgsConstructor
class UserBehavior {
    private Integer user_id;
    private Integer item_id;
    private String behavior;
}

@Data
@AllArgsConstructor
class ProduceInfo {
    private Integer item_id;
    private String name;
    private long price;
}


