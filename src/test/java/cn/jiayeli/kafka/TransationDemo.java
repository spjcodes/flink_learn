package cn.jiayeli.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TransationDemo {

    public static void main(String[] args) throws InterruptedException {
        Properties prots = new Properties();
        prots.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node01:9092,node02:9092");
        prots.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        prots.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        prots.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "pt-1");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(prots);

        List<String> words = Arrays.asList("aa", "bb", "cc", "dd", "ee", "ff", "gg");
        producer.beginTransaction();
        for(int i=0; i>0; i++) {
            TimeUnit.SECONDS.sleep(1);
            ProducerRecord<String, String> record = new ProducerRecord<>("topic-a", i + "", words.get(i / words.size()));
            producer.send(record);
            if (i % 10 == 0) {
                producer.commitTransaction();
            }
        }
    }
}
