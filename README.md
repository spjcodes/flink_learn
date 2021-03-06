# flink_learn

*****
>flink学习

# dataStream

---
## state
### 内存
### fileSystem
### rocksdb

---
## checkpoint
jobManager端生成 executorGraph 时添加监听器， 其会监听job的状态，当状态为running的时候就会调用starkCheckpointScheal，
其会根据设置的checkpointInteval周期性的调用triggerCheckpoint方法
triggerCheckpoint会通过触发taskmanager的checkpoint，其会在source中插入checkpointBrian，checkpointBrain 继承自streamEvent，和我们要处理的数据中的event一样，可以和数据一起向下游传播。
当下游的算子收到后如果有多个输入就会进行brain对齐，对齐过程中如果对数据进行缓存不做处理就是exectlyOnce,如果不做缓存，继续处理的话就是atlateonce state恢复时数据可能会重复处理。
数据对齐后就会将state数据备份到sateBackend，接着进行下一个算子的checkpoint（重复这个过程),直到所有的算子都完成就会通知jobManagecheckpint完成。继续进行数据处理

---
## source
**原理**
[FLIP-27](https://cwiki.apache.org/confluence/display/FLINK/FLIP-27%3A+Refactor+Source+Interface)
SourceFunction
ParallelSourceFunction
RichParallelSourceFunction
<br/>
---
**usage case**:
### kafkaSource
```xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-connector-kafka</artifactId>
    <version>1.15.0</version>
</dependency>
<!--如果使用 Kafka source，flink-connector-base 也需要包含在依赖中-->
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-connector-base</artifactId>
    <version>1.15.0</version>
</dependency>
```
```java
KafkaSource<String> source = KafkaSource.<String>builder()
    .setBootstrapServers(brokers)
    .setTopics("input-topic")
    .setGroupId("my-group")
    .setStartingOffsets(OffsetsInitializer.earliest())
    .setValueOnlyDeserializer(new SimpleStringSchema())
    .build();

env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");
```

### doris source
```xml
<!-- flink-doris-connector -->
<dependency>
    <groupId>org.apache.doris</groupId>
    <artifactId>flink-doris-connector-1.14_2.12</artifactId>
    <version>1.0.3</version>
</dependency> 
```
```java
 Properties properties = new Properties();
 properties.put("fenodes","FE_IP:8030");
 properties.put("username","root");
 properties.put("password","");
 properties.put("table.identifier","db.table");
 env.addSource(new DorisSourceFunction(
                        new DorisStreamOptions(properties), 
                        new SimpleListDeserializationSchema()
                )
        ).print();
```

---
## sink
### JDBCSink
>注意该连接器目前还 不是 二进制发行版的一部分,需要添加依赖。
已创建的 JDBC Sink 能够保证至少一次的语义。 更有效的精确执行一次可以通过 upsert 语句或幂等更新实现。

```xml
<dependency>
    <groupId>org.apache.flink</groupId>
    <artifactId>flink-connector-jdbc</artifactId>
    <version>1.15.0</version>
</dependency>
```

**用法示例**：

```java
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env
.fromElements(...)
.addSink(JdbcSink.sink(
"insert into books (id, title, author, price, qty) values (?,?,?,?,?)",
(ps, t) -> {
    ps.setInt(1, t.id);
    ps.setString(2, t.title);
    ps.setString(3, t.author);
    ps.setDouble(4, t.price);
    ps.setInt(5, t.qty);
},
JdbcExecutionOptions
    .builder()
    .withBatchSize(5)
    .withBatchIntervalMs(500)
    .withMaxRetries(5)
    .build(),
new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
    .withUrl(getDbMetadata().getUrl())
    .withDriverName(getDbMetadata().getDriverClass())
    .build()));

env.execute();
```
### dorisSink
```java
Properties pro = new Properties();
pro.setProperty("format", "json");
pro.setProperty("strip_outer_array", "true");
env.fromElements( 
    "{\"longitude\": \"116.405419\", \"city\": \"北京\", \"latitude\": \"39.916927\"}"
    )
     .addSink(
     	DorisSink.sink(
            DorisReadOptions.builder().build(),
         	DorisExecutionOptions.builder()
                    .setBatchSize(3)
                    .setBatchIntervalMs(0l)
                    .setMaxRetries(3)
                    .setStreamLoadProp(pro).build(),
         	DorisOptions.builder()
                    .setFenodes("FE_IP:8030")
                    .setTableIdentifier("db.table")
                    .setUsername("root")
                    .setPassword("").build()
     	));
```


---
## Consistent semantics
### exactlyOnce
### atLeastOnce
### atMostOnce
### E2EExactlyOnce

---
## fault tolerance
### checkpoint
### savepoint
### restartStrategy

---
## transformationOperator

## stateBackEnd

## join
### coMap
### connector
### join
### broadcastJoin
### windowJoin
### intervalJoin
### regularJoin


**********
# table/sql



*****************
# flinkCDC


