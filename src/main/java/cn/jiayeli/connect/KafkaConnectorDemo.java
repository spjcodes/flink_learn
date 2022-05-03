package cn.jiayeli.connect;

import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.schema.RatingSchema;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaConnectorDemo {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(1);

        Properties producerConfig = new Properties();
        producerConfig.setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        producerConfig.setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 1000*60*6+"");

        KafkaSink<RatingModule> kafkaSink =  KafkaSink.<RatingModule>builder()
                .setBootstrapServers("node02:9092,node03:9092,node05:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.<RatingModule>builder()
                        .setTopic("ratings")
                        .setValueSerializationSchema(new RatingSchema())
                        .build())
                .setKafkaProducerConfig(producerConfig)
                .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .setTransactionalIdPrefix("ratingInfo_KafkaSink")
                .build();

        env.addSource(new RatingInfoSourceFunction())
                .sinkTo(kafkaSink);

        KafkaSource<RatingModule> kafkaSource = KafkaSource.<RatingModule>builder()
                .setBootstrapServers("node02:9092")
                .setGroupId("g1")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setTopics("ratings")
                .setValueOnlyDeserializer(new RatingSchema())
                .build();

        env
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "ratingKafkaSource")
                .print();


        env.execute();
    }
}
