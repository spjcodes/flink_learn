package source;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.utils.EnvUtil;
import com.mysql.cj.jdbc.Driver;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.junit.Test;

import java.sql.*;

public class Save2Mysql {

    static {
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void a() throws SQLException {
        try {
            Class.forName(Driver.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/movieInfo", "root", "root");
        PreparedStatement psmt = connection.prepareStatement("select * from movies limit 10");
        ResultSet resultSet = psmt.executeQuery();
        System.out.println(resultSet);

    }


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(1);

        JdbcConnectionOptions movieInfoMysqlDB = new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withUrl("jdbc:mysql://localhost:3306/" +
                        "movieInfo")
                .withDriverName(Driver.class.getName())
                .withUsername("root")
                .withPassword("root")
                .build();

        SinkFunction<MovieModule> sink = JdbcSink.<MovieModule>sink(
                " insert into movies values(?,?,?)",
                (ps, movie) -> {
                    ps.setInt(1, Integer.parseInt(movie.getMovieId().toString()));
                    ps.setString(2, movie.getMovieTitle().toString());
                    ps.setString(3, movie.getChildrens().toString());
                },
                movieInfoMysqlDB);


        SinkFunction<RatingModule> ratingSink = JdbcSink.<RatingModule>sink(
                " insert into ratings(userId, movieId, rating, timestamp) values((?,?,?,?)",
                (ps, rating) -> {
                    ps.setInt(1, Integer.parseInt(rating.getUserId().toString()));
                    ps.setInt(2, Integer.parseInt(rating.getItemId().toString()));
                    ps.setDouble(3, rating.getRating());
                },
                movieInfoMysqlDB
        );

        env
            .addSource(new MovieInfoSourceFunction())
            .addSink(sink);

        env.addSource(new RatingInfoSourceFunction())
                        .addSink(ratingSink);

        env.execute();
    }
}
