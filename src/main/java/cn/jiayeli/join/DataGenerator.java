package cn.jiayeli.join;

import lombok.Data;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Data
public class DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class.getName());

   private static List<Tuple2<Integer, String>> movies = Arrays.asList(
           Tuple2.of(1, "足迹（Sleuth）"),
           Tuple2.of(2, "记忆碎片（Memento）"),
           Tuple2.of(3, "低俗小说（Pulp Fiction）"),
           Tuple2.of(4, "科洛弗档案（Cloverfield）"),
           Tuple2.of(5, "熊的故事（Ours, L'）"),
           Tuple2.of(6, "IF ONLY（如果能再爱一次）"),
           Tuple2.of(7, "空房间（3-iron）"),
           Tuple2.of(8, "车逝（Twilight Dancing）"),
           Tuple2.of(9, "穆赫兰道（Mulholland Drive）"),
           Tuple2.of(10, "大鱼（Big Fish）"),
           Tuple2.of(11, " 猜火车（Trainspotting）"),
           Tuple2.of(12, "楚门的世界（The Truman Show）"),
           Tuple2.of(13, "火柴人（Matchstick Men）"),
           Tuple2.of(14, "迷雾（The Mist）"),
           Tuple2.of(15, "拉快跑（Lola Rennt）"),
           Tuple2.of(16, "海上钢琴师（The Legend of 1900）"),
           Tuple2.of(17, "搏击俱乐部（Fight Club）"),
           Tuple2.of(18, "狙击电话亭（Phone Booth）"),
           Tuple2.of(19, "返老还童（The Curious Case Of Benjamin Button"),
           Tuple2.of(20, "居家男人（The Family Man）"),
           Tuple2.of(21, "这个男人来自地球（The Man From Earth）"),
           Tuple2.of(22, "1408幻影凶间（Chambre 1408）"),
           Tuple2.of(23, "神奇遥控器（Click）"),
           Tuple2.of(24, "芳芳（Fanfan）"),
           Tuple2.of(25, "魔幻时刻（The Magic Hour）"),
           Tuple2.of(26, "巴黎拜金女（Hors de prix ）"),
           Tuple2.of(27, "飞越疯人院（One Flew Over the Cuckoo's Nest ）"),
           Tuple2.of(28, "少数派报告(Minority Report)"),
           Tuple2.of(29, "未来水世界（Water World）"),
           Tuple2.of(30, "触不到的恋人（The Lake House")
   );

   private static List<Tuple2<Integer, String>> desc = Arrays.asList(
            Tuple2.of(1, "这部电影从始至终只出现了2个人。这是一部悬疑片"),
            Tuple2.of(2, "这部电影是采用倒叙的手法。我们看到的电影开头其实是电影故事的结局。"),
            Tuple2.of(3, "这是一部采用插叙由几个小故事组成的电影。看了开头5分钟你肯定会看下去。"),
            Tuple2.of(4, "电影中的人用摄像机拍到的就是我们所看到的。这是一部灾难片。"),
            Tuple2.of(5, "这是一部以动物视角出发的电影。动物是绝对的主角。"),
            Tuple2.of(6, "我更喜欢用这个英文名字。为了你的爱人你会上车么。"),
            Tuple2.of(7, "男主角没有一句台词；女主角在片尾说了两句话：我爱你；吃早饭了。"),
            Tuple2.of(8, "一部没有对白的意识流电影。这是一部中国艺术片。"),
            Tuple2.of(9, "一部世界上最难懂的电影。梦境和现实的艺术表达。"),
            Tuple2.of(10, "一部奇幻的现实电影。我也想找到那种世外桃园。"),
            Tuple2.of(11, "一部典型的青春电影。现实和梦想的碰撞。"),
            Tuple2.of(12, "当整个世界都在和你开玩笑，你还会相信它吗?"),
            Tuple2.of(13, "骗人能到这种地部，编剧花了不少心思呀。"),
            Tuple2.of(14, "导演说这是一部恐怖片：人的心才是最恐怖的东西。"),
            Tuple2.of(15, "起点不同，终点自然不同。这是北影教授推荐的一部德国电影。"),
            Tuple2.of(16, "坚持信仰，一生不变。"),
            Tuple2.of(17, "这是一部需要一看再看才能完全理解和欣赏的电影。"),
            Tuple2.of(18, ":一个电话亭，一个狙击手，一份对自己的真诚。"),
            Tuple2.of(19, "如果人生会倒着走，我们是否会珍惜呢？"),
            Tuple2.of(20, "更喜欢那种另类的温馨与浪漫，太现实的爱情似乎很难让人接受。"),
            Tuple2.of(21, "只有对话的电影，你会去看么？"),
            Tuple2.of(22, "如果你认为这个世界上没有鬼，你应该好好去看一下这部电影。"),
            Tuple2.of(23, "人生需要我们好好享受，不能错过每一个细节。"),
            Tuple2.of(24, "每个男人心中都有一个属于自己的梦中情人—芳芳。"),
            Tuple2.of(25, "看看电影中的电影，也许你会更加热爱电影了。"),
            Tuple2.of(26, "都说法国人浪漫，也许疯狂也是其中一种。"),
            Tuple2.of(27, "看似平淡，确是现实社会的映照。"),
            Tuple2.of(28, "You can change your future!"),
            Tuple2.of(29, "一直以为这是一部纪录片，看了让我大吃一惊。"),
            Tuple2.of(30, "心中默默地。")
            );

    private static final Random random = new Random();
    public static Tuple3<Integer, Integer, Long> generatorRating() {
      return Tuple3.of(random.nextInt(movies.size()), random.nextInt(10), System.currentTimeMillis()+(random.nextInt()%100));
    }

    public static List<Tuple2<Integer, String>> getMovies() {
        return movies;
    }

    //(movieId, rating, timestamp)
    public static DataStreamSource<Tuple3<Integer, Integer, Long>> getRatingInfoStream(StreamExecutionEnvironment env) {
        return  env
                    .addSource(new SourceFunction<Tuple3<Integer, Integer, Long>>() {
                        boolean isRunning = true;
                        Tuple3<Integer, Integer, Long> ratingInfo = null;

                        @Override
                        public void run(SourceContext<Tuple3<Integer, Integer, Long>> ctx) throws Exception {
                            while (isRunning) {
                                TimeUnit.MILLISECONDS.sleep(500);
                                ratingInfo = DataGenerator.generatorRating();
                                ctx.collect(ratingInfo);
                                logger.info("generator rating info:\t[" + ratingInfo + "]");
                            }
                        }

                        @Override
                        public void cancel() {
                            this.isRunning = false;
                        }
                    });
    }


    //(userid, movieId, rating, timestamp)
    public static DataStreamSource<Tuple3<Integer, Integer, Long>> getUserRatingStream(StreamExecutionEnvironment env) {
        return env.addSource(new SourceFunction<Tuple3<Integer, Integer, Long>>() {
            @Override
            public void run(SourceContext<Tuple3<Integer, Integer, Long>> ctx) throws Exception {

            }

            @Override
            public void cancel() {

            }
        });
    }

}
