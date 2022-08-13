package cn.jiayeli.tableAndSql;

import org.apache.flink.table.api.*;
import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;


public class Test {

    public static void main(String[] args) {

        /**
         * TableEnvironment 是 Table API 和 SQL 的核心概念。它负责:
         *
         *     在内部的 catalog 中注册 Table
         *     注册外部的 catalog
         *     加载可插拔模块
         *     执行 SQL 查询
         *     注册自定义函数 （scalar、table 或 aggregation）
         *     DataStream 和 Table 之间的转换(面向 StreamTableEnvironment )
         *
         * Table 总是与特定的 TableEnvironment 绑定。 不能在同一条查询中使用不同 TableEnvironment 中的表，例如，对它们进行 cn.jiayeli.join 或 union 操作。
         * TableEnvironment 可以通过静态方法 TableEnvironment.create() 创建。
         */
        // Create a TableEnvironment for batch or streaming execution.
        // See the "Create a TableEnvironment" section for details.
        EnvironmentSettings envSetting = new EnvironmentSettings.Builder()
                .inStreamingMode()
                /**
                 * 指定实例化TableEnvironment时要创建的初始目录的名称。
                 * 该目录是一个内存目录，将用于存储所有临时对象（例如来自TableEnvironment.createTemporaryView(String, Table)
                 * 或TableEnvironment.createTemporarySystemFunction(String, UserDefinedFunction)），
                 * 这些对象不能被持久化，因为它们没有可序列化的表示。
                 * 它也将是当前目录的初始值，可以通过TableEnvironment.useCatalog(String)改变。
                 * 默认值："default_catalog"。
                 */
                .withBuiltInCatalogName("userCatalog")
                /**
                 * 指定初始目录中的默认数据库的名称，以便在实例化TableEnvironment时创建。
                 * 该数据库是一个内存数据库，将用于存储所有临时对象（例如来自TableEnvironment.createTemporaryView(String, Table)
                 * 或TableEnvironment.createTemporarySystemFunction(String, UserDefinedFunction)），
                 * 这些对象不能被持久化，因为它们没有可序列化的表示。
                 * 它也将是当前数据库的初始值，可以通过TableEnvironment.useDatabase(String)进行更改。
                 * 默认值："default_database"。
                 */
                .withBuiltInDatabaseName("userDB")
                .build();

        TableEnvironment tEnv = TableEnvironment.create(envSetting);

        /**
         *  tEnv.createTemporaryTable("MyTable", TableDescriptor.forConnector("datagen")
         *    .schema(Schema.newBuilder()
         *      .column("f0", DataTypes.STRING())
         *      .build())
         *    .option(DataGenOptions.ROWS_PER_SECOND, 10)
         *    .option("fields.f0.kind", "random")
         *    .build());
         */

        TableDescriptor tableDescriptor = TableDescriptor.forConnector("datagen")
                .schema(Schema.newBuilder()
                        .column("f0", DataTypes.STRING())
                        .build())
                .option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
                .build();
        // Create a source table
        tEnv.createTemporaryTable("SourceTable", tableDescriptor);

        /**
         * 将给定的TableDescriptor注册为一个目录表。
         * 该描述符被转换为CatalogTable并存储在目录中。
         * 如果该表不应该被永久地存储在目录中，请使用createTemporaryTable(String, TableDescriptor)代替。
         * 示例。tEnv.createTable("MyTable",
         *  TableDescriptor
         *      .forConnector("datagen")
         *      .schema(Schema.newBuilder()
         *      .column("f0", DataTypes.STRING())
         *      .build())
         *  .option(DataGenOptions.ROWS_PER_SECOND, 10)
         *  .option("field.f0.kind", "random")
         *  .build())。
         *
         *  Params:
         *  path - 表将被注册的路径。关于路径的格式，请参见TableEnvironment类的描述。
         *  descriptor - 用于创建CatalogTable实例的模板。
         */
        tEnv.createTable("sourceTable", tableDescriptor);

        // Create a sink table (using SQL DDL)
        tEnv.executeSql("CREATE TEMPORARY TABLE SinkTable WITH ('connector' = 'blackhole') LIKE SourceTable");

        // Create a Table object from a Table API query
        Table table2 = tEnv.from("SourceTable");

        // Create a Table object from a SQL query
        Table table3 = tEnv.sqlQuery("SELECT * FROM SourceTable");

        // Emit a Table API result Table to a TableSink, same for SQL result
        TableResult tableResult = table2.executeInsert("SinkTable");

    }
}
