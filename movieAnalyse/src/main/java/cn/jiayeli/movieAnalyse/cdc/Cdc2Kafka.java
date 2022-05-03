package cn.jiayeli.movieAnalyse.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * myql --mysqlCDC.--> kafka
 */
public class Cdc2Kafka {

    public static void main(String[] args) throws Exception {
        // enable checkpoint
        Configuration configuration = Configuration.fromMap(Map.of("rest.port", "10010"));
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.enableCheckpointing(3000);
        env.setParallelism(1);

        MySqlSource<String> mySqlSourceByMovieInfo = MySqlSource.<String>builder()
                .hostname("jiayeli")
                .port(3306)
                .databaseList("movieInfo") // set captured database, If you need to synchronize the whole database, Please set tableList to ".*".
                .tableList("movieInfo.movies") // set captured table
                .username("kuro")
                .password("kuro.123")
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
             .build();


        Properties properties = new Properties();
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setKafkaProducerConfig(properties)
                .setBootstrapServers("node03:9092,node02:9092,node05:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("movieInfo")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
//                .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .build();


        env
                .fromSource(mySqlSourceByMovieInfo, WatermarkStrategy.noWatermarks(), "movieInfo MySQL Source").setParallelism(4)
                .map(e -> {
                    System.out.println("info_" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " :\t[" + e + "]");
                    return e;
                })
                .sinkTo(kafkaSink);

/*        MySqlSource<RatingModule> mysqlSourceByRatingInfo = MySqlSource.<RatingModule>builder()
                .hostname("jiayeli")
                .port(3306)
                .databaseList("movieInfo")
                .tableList("movieInfo.ratings")
                .username("kuro")
                .password("kuro.123")
                .deserializer(new RatingDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .build();

        env
                .fromSource(
                        mysqlSourceByRatingInfo,
                        WatermarkStrategy
                                .<RatingModule>forBoundedOutOfOrderness(Duration.ofMillis(100))
                                .withTimestampAssigner((e, t) -> Long.parseLong(e.getTimestamp().toString())),
                        "ratingInfo Mysql source"
                        );*/




        env.execute("cdc mysql data to kafka");
    }

}
