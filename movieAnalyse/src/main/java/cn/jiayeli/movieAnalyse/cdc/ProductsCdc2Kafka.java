package cn.jiayeli.movieAnalyse.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Map;

/**
 * myql --mysqlCDC.--> kafka
 */
public class ProductsCdc2Kafka {


    public static void main(String[] args) throws Exception {
        // enable checkpoint
        Configuration configuration = Configuration.fromMap(Map.of("rest.port", "10010"));
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.enableCheckpointing(3000);
        env.setParallelism(1);

        MySqlSource<String> mySqlSourceByProducts = MySqlSource.<String>builder()
                .hostname("jiayeli")
                .port(3306)
                .databaseList("db_learn") // set captured database, If you need to synchronize the whole database, Please set tableList to ".*".
                .tableList("db_learn.products") // set captured table
                .username("kuro")
                .password("kuro.123")
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .build();


        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers("node03:9092,node02:9092,node05:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("products")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build()
                )
//                .setDeliverGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                .build();


        env
                .fromSource(mySqlSourceByProducts, WatermarkStrategy.noWatermarks(), "movieInfo MySQL Source")
                .setParallelism(4)
                .sinkTo(kafkaSink);




        env.execute("Print MySQL Snapshot + Binlog");
    }

}
