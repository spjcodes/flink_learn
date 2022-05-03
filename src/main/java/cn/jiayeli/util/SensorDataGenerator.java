package cn.jiayeli.util;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 生成温度传感器数据
 * 时间15分钟内乱序
 *
 * sate： ts
 * sensor: tc
 * ts == null ? tc : ts
 * 1. checkpoint: [tc --> statebackEnd ]
 * 2. initializer: [ts --> tc ]
 * 3. checkpoint 时要阻塞source context.collect(sensorData)
 */
public class SensorDataGenerator extends RichParallelSourceFunction<Tuple3<String, Long, Double>> implements CheckpointedFunction {

    private boolean isRunning = true;

    private  static final Random random = new Random();

    private static final Logger logger = LoggerFactory.getLogger(SensorDataGenerator.class.getName());

    //传感器列表
    List<String> sensorList = Arrays.asList("s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10");

    //传感器数据
    Tuple3<String, Long, Double> sensorData;

    //传感器时间戳
    private static Long timestamp;

    //保存当前时间戳，用于持久化后容错
    private ListState<Long> listState;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void run(SourceContext<Tuple3<String, Long, Double>> ctx) throws Exception {
        Object lock = ctx.getCheckpointLock();

        while (isRunning) {

            int randomNum = Math.abs(this.random.nextInt());

            String sensorId = sensorList.get(randomNum % sensorList.size());
            //模拟15minter内乱序的时间
            timestamp = timestamp == null ? System.currentTimeMillis() - (this.random.nextInt() % (1000 * 60 * 15)) : timestamp - (this.random.nextInt() % (1000 * 10));

            double temperature = this.random.nextGaussian();

            if (randomNum % 100 == 0) {
                logger.debug("An exception occurred in run(..) function" + "current sensor timestamp is:\t" + timestamp);
                System.out.println("eeeeeeeeeeeeeeeeeeeeee\terror: \tAn exception occurred in run(..) function" + "current sensor timestamp is:\t" + timestamp);
                throw new RuntimeException("An unstoppable error has occurred in SensorDataGenerator by\t" + DateFormat.getDateTimeInstance().format(System.currentTimeMillis()));
            }

            try {
                TimeUnit.SECONDS.sleep(randomNum % 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sensorData = Tuple3.of(sensorId, timestamp, temperature);
            logger.debug(">>>>>>>>>>>>>>>>>>>>>>>> task: " +  getRuntimeContext().getIndexOfThisSubtask() + "\tcollect sensor data\t[" + sensorData + "]");
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> task: " +  getRuntimeContext().getIndexOfThisSubtask() + "\tcollect sensor data\t[" + sensorData + "]");
            //
            synchronized (lock) {
                ctx.collect(sensorData);
            }
        }

    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {

        System.out.println("sssssssssssssssssssssssssssssssssssss:\ttask: " +  getRuntimeContext().getIndexOfThisSubtask() + "\tsnapshot cn.jiayeli.state by\t"
                + DateFormat.getDateTimeInstance(1,0).format(System.currentTimeMillis())
                + "cn.jiayeli.state content:\t" + timestamp);
        System.out.println("sssssssssssssssssssssssssssssssssssss:\tcheckpointId:\t" + context.getCheckpointId() + "\t" + "CheckpointTimestamp:\t" + context.getCheckpointTimestamp());
        listState.update(Arrays.asList(timestamp));

    }


    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        listState = context.getOperatorStateStore().getListState(new ListStateDescriptor<Long>("sensor timestamp", Types.LONG()));

        while (listState.get().iterator().hasNext()) {
            timestamp = listState.get().iterator().next();
        }

        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii:\ttask id : " + getRuntimeContext().getIndexOfThisSubtask() + "\tinitializer:\t[" + timestamp + "]");
    }
}
