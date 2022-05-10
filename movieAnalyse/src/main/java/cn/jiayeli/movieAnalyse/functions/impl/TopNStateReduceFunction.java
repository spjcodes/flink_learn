package cn.jiayeli.movieAnalyse.functions.impl;

import cn.jiayeli.movieAnalyse.functions.StateReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.HashMap;

public class TopNStateReduceFunction extends StateReduceFunction {

    private HashMap<String, Tuple2<String, Long>> state;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        state = getState();
    }

    @Override
    public Tuple2<String, Long> reduce(Tuple2<String, Long> e, Tuple2<String, Long> ee) throws Exception {

         long result = state.get(e.f0) == null ? 0 : state.get(e.f0).f1;

        return Tuple2.of(e.f0, result+e.f1+ee.f1);
    }
}
