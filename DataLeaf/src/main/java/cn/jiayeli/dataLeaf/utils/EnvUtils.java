package cn.jiayeli.dataLeaf.utils;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class EnvUtils {

    public static StreamTableEnvironment getStreamTableEnv() {
        return StreamTableEnvironment.create(getStreamEnv());
    }

    public static  StreamExecutionEnvironment getStreamEnv() {
        return StreamExecutionEnvironment.getExecutionEnvironment();
    }

    public static StreamTableEnvironment getStreamTableEnv(String a) {
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                /*
                .withBuiltInCatalogName("")
                .withBuiltInDatabaseName("")
                */
                .build();

        TableEnvironment.create(settings);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(getStreamEnv());
        tableEnv.getConfig().getConfiguration().setString("", "");
        return tableEnv;
    }

}
