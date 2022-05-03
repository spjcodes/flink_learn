package cn.jiayeli.state;

import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jiayeli.util.SockTuple2Source;

public class KeyedStateDemo {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(1);
        SingleOutputStreamOperator<Tuple2<String, String>> sourceTuple2 = SockTuple2Source.getTuple2String(env);
        Logger logger = LoggerFactory.getLogger(KeyedStateDemo.class.getName());


        KeyedStream<Tuple2<String, String>, String> tuple2StringKeyedStream = sourceTuple2.keyBy(e -> e.f0);
        tuple2StringKeyedStream
                .filter(new RichFilterFunction<Tuple2<String, String>>() {
                    //
                    ValueStateDescriptor<String> userListStateDesc = new ValueStateDescriptor<String>("userIdListState", Types.STRING());

                    ValueState<String> state = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        logger.info("bind local variable to flink cn.jiayeli.state handle...");
                        state = getRuntimeContext().getState(userListStateDesc);
                    }

                    @Override
                    public boolean filter(Tuple2<String, String> value) throws Exception {
                        if (state.value() == null) {
                            state.update(value.f0);
                            return true;
                        }

                        return false;



                    }
                })
                .print();

    /*    KeyedStream<Tuple2<String, String>, String> tuple2StringKeyedStream = sourceTuple2.keyBy(e -> e.f0);
                tuple2StringKeyedStream
               .filter(new RichFilterFunction<Tuple2<String, String>>() {
                    //
                    ListStateDescriptor<String> userListStateDesc = new ListStateDescriptor<String>("userIdListState", Types.STRING());

                    ListState<String> cn.jiayeli.state = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        logger.info("bind local variable to flink cn.jiayeli.state handle...");
                        cn.jiayeli.state = getRuntimeContext().getListState(userListStateDesc);
                    }

                    @Override
                    public boolean filter(Tuple2<String, String> value) throws Exception {
                        Iterable<String> userids = cn.jiayeli.state.get();
                        for (String userid : userids) {
                            if (userid.equals(value.f0)) {
                                System.out.println("this userId is old userId:\t" + value.f0);
                                return false;
                            }
                        }

                        cn.jiayeli.state.add(value.f0);
                        return true;

                    }
                })
                .print();*/


        tuple2StringKeyedStream.process(new KeyedProcessFunction<String, Tuple2<String, String>, String>() {

            ValueState<String> userIdValueState;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                userIdValueState = getRuntimeContext().getState(new ValueStateDescriptor<String>("userIdValueState", TypeInformation.of(new TypeHint<String>() {})));
            }

            @Override
            public void processElement(Tuple2<String, String> value, KeyedProcessFunction<String, Tuple2<String, String>, String>.Context ctx, Collector<String> out) throws Exception {
                System.out.println(userIdValueState.value());
                System.out.println(userIdValueState);
                if (userIdValueState.value() == null) {
                    out.collect(value.f0);
                    userIdValueState.update(value.f0);
                }
            }
        }).print();

        System.out.println(env.getExecutionPlan());

        try {
            env.execute("use cn.jiayeli.state distinct user");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
