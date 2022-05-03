package cn.jiayeli.movieAnalyse.save2store;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.RatingModule;
import cn.jiayeli.movieAnalyse.source.MovieInfoSourceFunction;
import cn.jiayeli.movieAnalyse.source.RatingInfoSourceFunction;
import cn.jiayeli.movieAnalyse.util.EnvUtil;
import com.mysql.cj.jdbc.Driver;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Save2Mysql {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = EnvUtil.get();
        env.setParallelism(1);

        SinkFunction<MovieModule> mysqlSinkByMovie = JdbcSink.<MovieModule>sink(
            "INSERT INTO movieInfo.movies (" +
                    "movieId" +
                    ",movieTitle" +
                    ",releaseDate" +
                    ",videoReleaseDate" +
                    ",IMDbURL" +
                    ",`unknown`" +
                    ",`Action`" +
                    ",Adventure" +
                    ",Animation" +
                    ",Childrens" +
                    ",Comedy" +
                    ",Crime" +
                    ",Documentary" +
                    ",Drama" +
                    ",Fantasy" +
                    ",FilmNoir" +
                    ",Horror" +
                    ",Musical" +
                    ",Mystery" +
                    ",Romance" +
                    ",SciFi" +
                    ",Thriller" +
                    ",War" +
                    ",Western)" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE " +
                    "movieId=?" +
                    ",movieTitle=?" +
                    ",releaseDate=?" +
                    ",videoReleaseDate=?" +
                    ",IMDbURL=?" +
                    ",`unknown`=?" +
                    ",`Action`=?" +
                    ",Adventure=?" +
                    ",Animation=?" +
                    ",Childrens=?" +
                    ",Comedy=?" +
                    ",Crime=?" +
                    ",Documentary=?" +
                    ",Drama=?" +
                    ",Fantasy=?" +
                    ",FilmNoir=?" +
                    ",Horror=?" +
                    ",Musical=?" +
                    ",Mystery=?" +
                    ",Romance=?" +
                    ",SciFi=?" +
                    ",Thriller=?" +
                    ",War=?" +
                    ",Western=?" ,
                (statement, movie) -> {
                    statement.setLong(1, Long.parseLong(movie.getMovieId()));
                    statement.setString(2, movie.getMovieTitle());
                    statement.setString(3, movie.getReleaseDate());
                    statement.setString(4, movie.getVideoReleaseDate());
                    statement.setString(5, movie.getIMDbURL());
                    statement.setString(6, movie.getUnknown());
                    statement.setString(7, movie.getAction());
                    statement.setString(8, movie.getAdventure());
                    statement.setString(9, movie.getAnimation());
                    statement.setString(10, movie.getChildrens());
                    statement.setString(11, movie.getComedy());
                    statement.setString(12, movie.getCrime());
                    statement.setString(13, movie.getDocumentary());
                    statement.setString(14, movie.getDrama());
                    statement.setString(15, movie.getFantasy());
                    statement.setString(16, movie.getFilmNoir());
                    statement.setString(17, movie.getHorror());
                    statement.setString(18, movie.getMusical());
                    statement.setString(19, movie.getMystery());
                    statement.setString(20, movie.getRomance());
                    statement.setString(21, movie.getSciFi());
                    statement.setString(22, movie.getThriller());
                    statement.setString(23, movie.getWar());
                    statement.setString(24, movie.getWestern());
                    statement.setLong(25, Long.parseLong(movie.getMovieId()));
                    statement.setString(26, movie.getMovieTitle());
                    statement.setString(27, movie.getReleaseDate());
                    statement.setString(28, movie.getVideoReleaseDate());
                    statement.setString(29, movie.getIMDbURL());
                    statement.setString(30, movie.getUnknown());
                    statement.setString(31, movie.getAction());
                    statement.setString(32, movie.getAdventure());
                    statement.setString(33, movie.getAnimation());
                    statement.setString(34, movie.getChildrens());
                    statement.setString(35, movie.getComedy());
                    statement.setString(36, movie.getCrime());
                    statement.setString(37, movie.getDocumentary());
                    statement.setString(38, movie.getDrama());
                    statement.setString(39, movie.getFantasy());
                    statement.setString(40, movie.getFilmNoir());
                    statement.setString(41, movie.getHorror());
                    statement.setString(42, movie.getMusical());
                    statement.setString(43, movie.getMystery());
                    statement.setString(44, movie.getRomance());
                    statement.setString(45, movie.getSciFi());
                    statement.setString(46, movie.getThriller());
                    statement.setString(47, movie.getWar());
                    statement.setString(48, movie.getWestern());
                },
                JdbcExecutionOptions.builder()
                        .withBatchSize(1000)
                        .withBatchIntervalMs(200)
                        .withMaxRetries(5)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withUrl("jdbc:mysql://jiayeli:3306/movieInfo")
                        .withDriverName(Driver.class.getName())
                        .withUsername("kuro")
                        .withPassword("kuro.123")
                        .build()
        );

        env
            .addSource(new MovieInfoSourceFunction())
            .map(e -> {
                System.out.println(e);
                return e;
            })
            .addSink(mysqlSinkByMovie)
            ;

                SinkFunction<RatingModule> mysqlSinkByRating = JdbcSink
                .<RatingModule>sink(
                        "INSERT into ratings(userId, movieId, rating, `timestamp`)  values (?, ?, ?, ?) " +
                                "on duplicate key update userId = ?, movieId = ?, rating = ?, `timestamp` = ?",
                        new JdbcStatementBuilder<RatingModule>() {
                            @Override
                            public void accept(PreparedStatement psmt, RatingModule ratingModule) throws SQLException {
                                psmt.setInt(1, Integer.parseInt(ratingModule.getUserId().toString()));
                                psmt.setInt(2, Integer.parseInt(ratingModule.getItemId().toString()));
                                psmt.setDouble(3, Double.parseDouble(ratingModule.getRating().toString()));
                                psmt.setLong(4, Long.parseLong(ratingModule.getTimestamp().toString()));
                                psmt.setInt(5, Integer.parseInt(ratingModule.getUserId().toString()));
                                psmt.setInt(6, Integer.parseInt(ratingModule.getItemId().toString()));
                                psmt.setDouble(7, Double.parseDouble(ratingModule.getRating().toString()));
                                psmt.setLong(8, Long.parseLong(ratingModule.getTimestamp().toString()));
                            }
                        },
                        JdbcExecutionOptions.builder()
                                .withBatchIntervalMs(200)
                                .withBatchSize(1000)
                                .withMaxRetries(5)
                                .build(),
                        new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                                .withUsername("kuro")
                                .withPassword("kuro.123")
                                .withUrl("jdbc:mysql://jiayeli:3306/movieInfo")
                                .withDriverName(Driver.class.getName())
                                .withConnectionCheckTimeoutSeconds(10)
                                .build()


                );

        env
                .addSource(new RatingInfoSourceFunction())
                .addSink(mysqlSinkByRating);


        env.execute();
    }
}
