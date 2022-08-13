package cn.jiayeli.tableAndSql;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Case1 {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        env.setParallelism(1);
        Logger logger = LoggerFactory.getLogger(Case1.class.getName());


        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("node03:9092,node04:9092,node05:9092")
                .setTopics("t1")
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStreamSource<String> sourceStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka Source");
        sourceStream.print();



        /**
         * user level data generator (username, level, time)
         */
        DataStreamSource<Tuple3<String, Integer, Long>> userLevelStream = env.addSource(new SourceFunction<Tuple3<String, Integer, Long>>() {
            boolean isRunning = true;
            final Random random = new Random();

            Map<String, Integer> operatorState = new HashMap<String, Integer>();

          @Override
            public void run(SourceContext<Tuple3<String, Integer, Long>> ctx) throws Exception {
                while (isRunning) {
                    int rdmNm = Math.abs(random.nextInt() % 10);
                    TimeUnit.SECONDS.sleep(rdmNm%2);
                    String name = "u" + rdmNm;
                    int level = Math.abs(random.nextInt() % 100) > 80 ? operatorState.getOrDefault(name, 1) + 1 : operatorState.getOrDefault(name, 1);
                    Tuple3<String, Integer, Long> userLevelInfo = Tuple3.of(name, level, System.currentTimeMillis() + rdmNm * 10000);
                    operatorState.put(userLevelInfo.f0, userLevelInfo.f1);
                    logger.info("user info:\t[" + userLevelInfo + "]");
                    ctx.collect(userLevelInfo);
                }
            }

            @Override
            public void cancel() {
                this.isRunning = false;
            }
        });

        Properties properties = new Properties();

        KafkaSink<Tuple3<String, Integer, Long>> kafkaSink = KafkaSink
                .<Tuple3<String, Integer, Long>>builder()
                .setBootstrapServers("node03:9092,node04:9092,node05:9092")
                .setRecordSerializer(new KafkaRecordSerializationSchema<Tuple3<String, Integer, Long>>() {
                    @Override
                    public ProducerRecord<byte[], byte[]> serialize(Tuple3<String, Integer, Long> element, KafkaSinkContext context, Long timestamp) {
                        return new ProducerRecord<>("t1", element.toString().getBytes(StandardCharsets.UTF_8));
                    }
                })
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .setKafkaProducerConfig(properties)
                .build();

        userLevelStream.sinkTo(kafkaSink);

        env.execute();



    }

}
