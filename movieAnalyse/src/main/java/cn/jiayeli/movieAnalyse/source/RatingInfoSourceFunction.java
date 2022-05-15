package cn.jiayeli.movieAnalyse.source;

import cn.jiayeli.movieAnalyse.module.RatingModule;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RatingInfoSourceFunction extends RichParallelSourceFunction<RatingModule> implements CheckpointedFunction {


    private static boolean isRunning;

    private static ListState<Long> fileOffsetListState;

    private static long fileOffset = 0;

    private  static final String filePath = "src/main/resources/dataSet/u.data";

    private static FileReader filereader = null;

    private static BufferedReader bufferedReader = null;

    private static Logger logger = null;


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        isRunning = true;
        logger = LoggerFactory.getLogger(RatingInfoSourceFunction.class.getName());
        filereader = new FileReader(filePath);
        bufferedReader = new BufferedReader(filereader);
    }

    @Override
    public void run(SourceContext<RatingModule> ctx) throws Exception {

        String line = null;

        Random random = new Random();

        if (fileOffset != 0) {
            bufferedReader.skip(fileOffset);
        }

        Object checkpointLock = ctx.getCheckpointLock();

        while (isRunning && (line = bufferedReader.readLine()) != null) {


           /* for (Long offset : fileOffsetListState.get()) {
                fileOffset = offset;
            }*/

            String[] ratingInfo = line.split("\\s+");

            if (ratingInfo.length != 4) {
                logger.error("parse rating info error by line :\t[" + line + "]" + "\toffset:\t" + fileOffset);
                continue;
            }

            int randomNum = Math.abs(random.nextInt());

            if (randomNum % 1000 == 0) {
                throw new RuntimeException("throw a exception by myself, current process line is :\t[" + line + "]\toffset:\t" + fileOffset);
            }

            TimeUnit.MILLISECONDS.sleep(randomNum%10000);

            synchronized (checkpointLock) {
                RatingModule ratingModule = new RatingModule(
                        ratingInfo[0],
                        ratingInfo[1],
                        Integer.valueOf(ratingInfo[2]),
                        ratingInfo[3]
                );
                ctx.collect(ratingModule);
                fileOffset += line.length() + 1;
                fileOffsetListState.update(List.of(fileOffset));
                logger.info("output ratingModule:\t[" + ratingModule + "]\toffset__" + fileOffset);
            }

        }
    }

    @Override
    public void cancel() {
        isRunning = false;
        try {
            if (filereader != null) {
                filereader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        fileOffsetListState.update(List.of(fileOffset));
        logger.info("snapshotState:\t" + fileOffset);
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        fileOffsetListState = context.getOperatorStateStore().getListState(new ListStateDescriptor<Long>("ratingInfoOffsetState",
                TypeInformation.of(new TypeHint<Long>() {})));

        for (Long offset : fileOffsetListState.get()) {
            fileOffset = offset;
        }
    }

}
