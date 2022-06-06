package dataGenerator;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import schema.MovieSerDer;

import java.util.HashMap;
import java.util.Map;

public class MovieSinkFunction extends TwoPhaseCommitSinkFunction<MovieModule, MovieModule, SinkFunction.Context> {

    private static String KAFKA_TOPIC = "";
    private static String KAFKA_BROKERS = "";
    private static transient KafkaProducer<String, MovieModule> producer;



    public MovieSinkFunction(TypeSerializer<MovieModule> transactionSerializer, TypeSerializer<Context> contextSerializer) {
        super(transactionSerializer, contextSerializer);
    }

    public  void DataGenerator() {
        Map<String, Object> propts = new HashMap<>();
        propts.put(ProducerConfig.ACKS_CONFIG, "-1");
        propts.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node03:9092,node04:9092,node05:9092");
        propts.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Serdes.String());
        propts.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MovieSerDer.class);

        Producer<String, MovieModule> producer = new KafkaProducer<>(propts);
        MovieModule movieModule = new MovieModule();
        producer.send(
                new ProducerRecord<>(KAFKA_TOPIC, "movieInfo", movieModule),
                (recordMetadata, e) -> System.out.println("call back" + recordMetadata.topic()));

    }


    @Override
    protected void invoke(MovieModule transaction, MovieModule value, Context context) throws Exception {
        System.out.println("invoke" + "\t" + transaction + "\t" + value);
    }

    @Override
    public void writeWatermark(Watermark watermark) throws Exception {
        super.writeWatermark(watermark);
        System.out.println("water:\t" + watermark.getFormattedTimestamp());
    }

    @Override
    protected MovieModule beginTransaction() throws Exception {
        System.out.println("beginTransaction");
        return null;
    }

    @Override
    protected void preCommit(MovieModule transaction) throws Exception {
        System.out.println("preCommit");
    }

    @Override
    protected void commit(MovieModule transaction) {
        System.out.println("commit");
    }

    @Override
    protected void abort(MovieModule transaction) {
        System.out.println("abort");
    }


}
