package source;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.scala.typeutils.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 一个定期checkpoint的电影数据源，
 * 发生异常数据会从上次checkpoint snapshot的state中恢复，数据会重复消费
 */
public class MovieInfoSourceFunction extends RichParallelSourceFunction<MovieModule> implements CheckpointedFunction {

    private static boolean isRunning;

    private static final String movieInfoFilePath = "src/main/resources/dataSet/u.item";

    private static BufferedReader bufferedReader = null;

    private static FileReader fileReader = null;

    private static long fileOffset = 0;

    private static ListState<Long> fileOffsetListState;

    private static boolean resetOffset = false;

    private static Logger logger;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        isRunning = true;
        logger = LoggerFactory.getLogger(MovieInfoSourceFunction.class.getName());
        File movieInfoFile = new File(movieInfoFilePath);
        fileReader = new FileReader(movieInfoFile);
        bufferedReader = new BufferedReader(fileReader);

    }

    @Override
    public void run(SourceContext<MovieModule> ctx) throws Exception {

        Object checkpointLock = ctx.getCheckpointLock();
        String line = null;
        Random random = new Random();

        if (resetOffset) {
            bufferedReader.skip(fileOffset);
        }

        while (isRunning) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] movieInfoArray = line.split("\\|");
                if (movieInfoArray.length != 24) {
                    logger.info("parse movie info exception by info:\t[" + line + "]");
                    continue;
                }

       /*     if (Math.abs(random.nextInt()) % 1000 == 0) {
                logger.info("exception line :\t[" + line + "]");
                logger.info("offset is:\t[" + fileOffset + "]");
                throw new RuntimeException("========================== 0_o throw a exception by myself， current file offset is:\t" + fileOffset
                        + "\texception line :\t[" + line + "]");
            }*/

                TimeUnit.MILLISECONDS.sleep(Math.abs(random.nextInt()) % 200);

                synchronized (checkpointLock) {
                    MovieModule movieModule = new MovieModule(
                            movieInfoArray[0],
                            movieInfoArray[1],
                            movieInfoArray[2],
                            movieInfoArray[3],
                            movieInfoArray[4],
                            movieInfoArray[5],
                            movieInfoArray[6],
                            movieInfoArray[7],
                            movieInfoArray[8],
                            movieInfoArray[9],
                            movieInfoArray[10],
                            movieInfoArray[11],
                            movieInfoArray[12],
                            movieInfoArray[13],
                            movieInfoArray[14],
                            movieInfoArray[15],
                            movieInfoArray[16],
                            movieInfoArray[17],
                            movieInfoArray[18],
                            movieInfoArray[19],
                            movieInfoArray[20],
                            movieInfoArray[21],
                            movieInfoArray[22],
                            movieInfoArray[23]
                    );
                    ctx.collect(
                            movieModule
                    );
                    //换行符offset=1
                    fileOffset += line.length() + 1;
                    logger.info("output data :\t" + movieModule + "\t--offset [" + fileOffset + "]");
                }
            }
        }
    }

    @Override
    public void cancel() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
//        fileOffsetListState.update(Arrays.asList(fileOffset));
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        fileOffsetListState = context.getOperatorStateStore().getListState(new ListStateDescriptor<Long>("movieInfoFileReadListState", Types.LONG()));

        Iterator<Long> iterator = fileOffsetListState.get().iterator();
        if (iterator.hasNext()) {
            fileOffset = iterator.next();
            resetOffset = true;
        }
        System.out.println("initialize cn.jiayeli.state :\t" + fileOffset);
    }

  }
