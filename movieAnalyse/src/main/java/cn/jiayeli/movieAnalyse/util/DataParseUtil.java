package cn.jiayeli.movieAnalyse.util;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import cn.jiayeli.movieAnalyse.module.UserModule;
import org.apache.flink.api.java.tuple.Tuple3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DataParseUtil {

    public  HashMap<String, UserModule> getUserDataSetByFile(File file) {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        HashMap<String, UserModule> map = new HashMap<>();
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userInfo = line.split("\\|");
                if (userInfo.length == 5) {
                    UserModule userModule = new UserModule(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4]);
                    map.put(userModule.getUserId().toString(), userModule);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        }
        return map;
    }

    public static HashMap<String, Tuple3<String, String, String>> getMovieInfoDataSet(String fileName)  {

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        HashMap<String, Tuple3<String, String, String>> movieInfos = new HashMap<>();

        try {
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] movieArray = line.split("\\|");
                if (movieArray.length == 24) {
                    movieInfos.put(movieArray[0], Tuple3.of(movieArray[0], movieArray[1], movieArray[4]));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        }

        return movieInfos;
    }

    public  HashMap<String, MovieModule> getMovieModuleDataSet(File file)  {

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        HashMap<String, MovieModule> movieInfos = new HashMap<>();

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] movieInfoArray = line.split("\\|");
                if (movieInfoArray.length == 24) {
                    movieInfos.put(movieInfoArray[0],
                            new MovieModule(
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
                    ));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        }

        return movieInfos;
    }
}
