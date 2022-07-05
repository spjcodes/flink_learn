package cn.jiayeli.druid;

import cn.jiayeli.util.EnvUtil;
import cn.jiayeli.util.KafkaConstant;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;

public class MovieDateETL {
    private static ParameterTool parameters = null;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = EnvUtil.get();
        String configFilePath = "src/main/resources/druid-conf.properties";
        try {
            parameters = ParameterTool.fromPropertiesFile(configFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Properties producerConfig = parameters.getProperties();
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers(parameters.get(KafkaConstant.BOOTSTRAP_SERVERS_CONFIG))
                .setTopics(parameters.get(KafkaConstant.CONSUMER_TOPIC))
                .setGroupId(KafkaConstant.GROUP_ID_CONFIG)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        DataStreamSource<String> source = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), parameters.get("source.name"));

        source.print();

        SingleOutputStreamOperator<String> finaDataStream = sourceDataTransform(source);

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers(parameters.get(KafkaConstant.BOOTSTRAP_SERVERS_CONFIG))
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(parameters.get(KafkaConstant.SINK_TOPIC))
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        finaDataStream.sinkTo(kafkaSink);
        env.execute(parameters.get("application.id"));
    }

    private static SingleOutputStreamOperator<String> sourceDataTransform(DataStreamSource<String> source) {
        return source.map((MapFunction<String, String>) value -> value);
    }


}
