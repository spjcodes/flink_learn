
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class WorCount {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        Logger logger = LoggerFactory.getLogger(WorCount.class.getName());
        int cpuCores = Runtime.getRuntime().availableProcessors();

        logger.info("can use parallelism:\t" + cpuCores);

        env.setParallelism(10);
         /*d
        DataStreamSource<List<String>> datas = env.fromElements(Arrays.asList("A", "B", "C", "d", "E", "f", "a", "b", "c", "d", "a", "d", "C"));

       atas.map(new MapFunction<List<String>, Tuple2<String, Integer>>() {
           @Override
           public Tuple2<String, Integer> map(List<String> strings) throws Exception {
               return Tuple2.of(strings.get(0), 1);
           }
       })
        .keyBy(e -> e.f0)
        .reduce((e, ee) -> Tuple2.of(e.f0, e.f1 + ee.f1))
        .print();*/
        DataStreamSource<String> stream = env.socketTextStream("localhost", 10010);
        SingleOutputStreamOperator<Tuple2<String, Integer>> datas = stream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                Arrays.stream(s.split("\\s+")).forEach(e -> collector.collect(Tuple2.of(e, 1)));
            }
        });

        datas
                .keyBy(e -> e.f0)
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> e, Tuple2<String, Integer> ee) throws Exception {
                        return Tuple2.of(e.f0, e.f1 + ee.f1);
                    }
                })
                .print();

        try {
            env.execute("word count");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
