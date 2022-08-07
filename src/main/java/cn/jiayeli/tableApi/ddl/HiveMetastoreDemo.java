package cn.jiayeli.tableApi.ddl;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.catalog.hive.HiveCatalog;

public class HiveMetastoreDemo {


    public static void main(String[] args) {
        EnvironmentSettings settings = EnvironmentSettings.inStreamingMode();
        TableEnvironment tEnv = TableEnvironment.create(settings);

        String name            = "myhive";
        String defaultDatabase = "mydatabase";
        String hiveConfDir     = "/home/kuro/workspace/bigdata/FLINK_LEARN/src/main/resources/hive-conf/hive-site.xml";

        HiveCatalog hive = new HiveCatalog(name, defaultDatabase, hiveConfDir);

        tEnv.registerCatalog(name, hive);

        tEnv.useCatalog(name);

        tEnv.sqlQuery("show tables").execute().print();

    }

}
