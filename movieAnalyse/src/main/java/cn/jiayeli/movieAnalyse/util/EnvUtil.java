package cn.jiayeli.movieAnalyse.util;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.net.URI;

public class EnvUtil {

    private static StreamExecutionEnvironment  env;

    static {
        Configuration conf = new Configuration();
        conf.setInteger("rest.port", 10010);
        env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
    }


    public static StreamExecutionEnvironment get() {
//        env.setParallelism(1);
//        env.setStateBackend(new RocksDBStateBackend("file:///home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/ckdir/"));
        /**
         * RocksDBStateBackend 已弃用，取而代之的是 EmbeddedRocksDBStateBackend 和 org.apache.flink.runtime.cn.jiayeli.state.storage.FileSystemCheckpointStorage。
         * 此更改不会影响 Job 的运行时特性，只是一个 API 更改，以帮助更好地传达 Flink 将本地状态存储与容错分离的方式。
         * 可以在不丢失状态的情况下升级作业。
         * 如果通过 StreamExecutionEnvironment 配置您的状态后端，请进行以下更改。
         * StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
         * env.setStateBackend(new EmbeddedRocksDBStateBackend());
         * env.getCheckpointConfig().setCheckpointStorage("hdfs://checkpoints");
         * 如果您通过 flink-conf.yaml 配置状态后端，则无需更改。
         * 将其状态存储在 RocksDB 中的状态后端。此状态后端可以存储超出内存并溢出到磁盘的非常大的状态。
         * 所有的键/值状态（包括窗口）都存储在 RocksDB 的键/值索引中。
         * 为了防止机器丢失，检查点会拍摄 RocksDB 数据库的快照，并将该快照保留在文件系统（默认情况下）或另一个可配置的状态后端中。
         * RocksDB 实例的行为可以通过使用 setPredefinedOptions(PredefinedOptions) 和 setRocksDBOptions(RocksDBOptionsFactory) 方法设置 RocksDB 选项来参数化。
         */
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.setStateBackend(new EmbeddedRocksDBStateBackend());
//        env.getCheckpointConfig().setCheckpointStorage(URI.create("file:///tmp/ckdir/"));
        env.getCheckpointConfig().setCheckpointStorage(URI.create("file:///home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/ckdir/"));
        env.getCheckpointConfig().setCheckpointInterval(1000 * 60 * 5);
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.seconds(5)));

        return env;

    }
}
