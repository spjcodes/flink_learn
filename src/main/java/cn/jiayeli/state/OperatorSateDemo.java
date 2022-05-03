package cn.jiayeli.state;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jiayeli.util.SensorDataGenerator;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class OperatorSateDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        env.setParallelism(1);
        //指定存储目录
        env.getCheckpointConfig().setCheckpointStorage(URI.create("file:///home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/ckdir/"));
        /**
         * RETAIN_ON_CANCELLATION
         * 在作业取消时保留外部化检查点。当您取消拥有的作业时，所有检查点状态都会保留。取消作业后，您必须手动删除检查点元数据和实际程序状态。
         *DELETE_ON_CANCELLATION
         * 删除作业取消时的外部检查点。当您取消拥有的作业时，所有检查点状态都将被删除，包括元数据和实际程序状态。因此，您无法在作业取消后从外部检查点恢复。请注意，如果作业以状态 JobStatus.FAILED 终止，则始终保持检查点状态
         *NO_EXTERNALIZED_CHECKPOINTS
         * 完全禁用外部化检查点
         */
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(5);
        env.getCheckpointConfig().setCheckpointTimeout(TimeUnit.SECONDS.toSeconds(15));
        //checkpoint间隔
        env.getCheckpointConfig().setCheckpointInterval(1000 * 10);
        //设置stateBackend，默认memoryStateBackend
        env.setStateBackend(new HashMapStateBackend());
        //设置失败重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(4, Time.seconds(5)));

        Logger logger = LoggerFactory.getLogger(KeyedStateDemo.class.getName());

        DataStreamSource<Tuple3<String, Long, Double>> tuple3DataStreamSource = env.addSource(new SensorDataGenerator());

        tuple3DataStreamSource.print();

        try {
            env.execute("operator cn.jiayeli.state demo");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
