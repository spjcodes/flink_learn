package cn.jiayeli.tableApi;

import org.apache.flink.table.api.*;

import static org.apache.flink.table.api.Expressions.$;

public class TableEnvCreateByEnvSettingFile {

    public static void main(String[] args) {
//        1. use config file create a table environment

        EnvironmentSettings envSetting = EnvironmentSettings
                .newInstance()
//                .inBatchMode()
                .inStreamingMode()
                /**
                 * 指定实例化TableEnvironment时要创建的初始目录的名称。
                 * 该目录是一个内存目录，将用于存储所有临时对象（例如来自TableEnvironment.createTemporaryView(String, Table)
                 * 或TableEnvironment.createTemporarySystemFunction(String, UserDefinedFunction)），
                 * 这些对象不能被持久化，因为它们没有可序列化的表示。
                 * 它也将是当前目录的初始值，可以通过TableEnvironment.useCatalog(String)改变。
                 * 默认值："default_catalog"。
                 */
//                .withBuiltInCatalogName("userCatalog")
                /**
                 * 指定初始目录中的默认数据库的名称，以便在实例化TableEnvironment时创建。
                 * 该数据库是一个内存数据库，将用于存储所有临时对象（例如来自TableEnvironment.createTemporaryView(String, Table)
                 * 或TableEnvironment.createTemporarySystemFunction(String, UserDefinedFunction)），
                 * 这些对象不能被持久化，因为它们没有可序列化的表示。
                 * 它也将是当前数据库的初始值，可以通过TableEnvironment.useDatabase(String)进行更改。
                 * 默认值："default_database"。
                 */
//                .withBuiltInDatabaseName("userDB")
                .build();
        /**
         * 创建了一个表环境，它是创建表和SQL API程序的入口和中心环境。
         * 它在语言层面上对所有基于JVM的语言都是统一的（也就是说，Scala和Java API之间没有区别），对有界和无界的数据处理也是如此。
         * 一个表环境负责
         * 连接到外部系统。
         * 从目录中注册和检索表和其他元对象。
         * 执行SQL语句。
         * 提供进一步的配置选项。
         * 注意：这个环境是为纯表程序准备的。如果你想从其他Flink API转换或转换到其他Flink API，可能需要在相应的桥接模块中使用一个可用的特定语言的表环境。
         * Params:
         * settings - 用于实例化TableEnvironment的环境设置。
         */
        TableEnvironment tEnv = TableEnvironment.create(envSetting);

        //3.
  /*      LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
*/
        /**
         * 描述表示源或接收器的 CatalogTable。
         * TableDescriptor 是用于创建 CatalogTable 实例的模板。
         * 它与“CREATE TABLE”SQL DDL 语句非常相似，包含schema、连接器选项和其他特征。
         * 由于 Flink 中的表通常由外部系统支持，因此描述符描述了连接器（可能还有其格式）的配置方式。
         * 这可用于在 Table API 中注册表，请参阅 TableEnvironment.createTemporaryTable(String, TableDescriptor)。
         */

        Table table = tEnv.from(TableDescriptor.forConnector("datagen")
                .schema(Schema.newBuilder()
                        .column("word", DataTypes.STRING())
                        .column("word1", DataTypes.BIGINT())
                        .column("word2", DataTypes.DECIMAL(5, 2))
                        .build())
                .build());
        tEnv.createTemporaryView("words", table);
        Table word = table.select($("word"));
//        table.select($("word"), $("word1"), $("word2")).execute().print();
//        tEnv.executeSql("select * from words").print();

        Schema schema = Schema
                .newBuilder()
                .column("userId", DataTypes.STRING())
                .column("age", DataTypes.STRING())
                .column("gender", DataTypes.STRING())
                .column("occupation", DataTypes.STRING())
                .column("zipCode", DataTypes.STRING())
                .build();


        tEnv.createTemporaryTable("userInfo", TableDescriptor
                .forConnector("filesystem")
                .schema(schema)
                .option("path", "src/main/resources/dataSet/u.user")
                .format(FormatDescriptor
                        .forFormat("csv")
                        .option("field-delimiter", "|")
                        .build())
                .build());

        tEnv.executeSql("select * from userInfo limit 10").collect().forEachRemaining(e -> System.out.println(e));
        
    }

}
