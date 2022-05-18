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
SourceFunction
ParallelSourceFunction
RichParallelSourceFunction



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
new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
.withUrl(getDbMetadata().getUrl())
.withDriverName(getDbMetadata().getDriverClass())
.build()));
env.execute();
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


