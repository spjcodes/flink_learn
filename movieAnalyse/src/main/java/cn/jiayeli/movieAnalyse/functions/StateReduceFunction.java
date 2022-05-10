package cn.jiayeli.movieAnalyse.functions;

import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class StateReduceFunction extends RichReduceFunction<Tuple2<String, Long>> implements CheckpointedFunction {

    Logger logger = LoggerFactory.getLogger(StateReduceFunction.class.getName());

    MapStateDescriptor<String, Tuple2<String, Long>> mapStateDesc = new MapStateDescriptor<String, Tuple2<String, Long>>("mapState",  Types.STRING(), TypeInformation.of(new TypeHint<Tuple2<String, Long>>() {}));
    MapState<String, Tuple2<String, Long>> mapState;
    HashMap<String, Tuple2<String, Long>> state = new HashMap<>();


    public abstract   Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception;

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        mapState.clear();
        mapState.putAll(state);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        mapState = context.getKeyedStateStore().getMapState(mapStateDesc);
        mapState.entries().forEach(e -> state.put(e.getKey(), e.getValue()));
        logger.info("initializeState:+\t[");
        mapState.entries().forEach(e -> System.out.printf("(%s, %s)", e.getKey(), e.getValue()));
        System.out.println("]");
    }

    public HashMap<String, Tuple2<String, Long>> getState() {
        return this.state;
    }
}
