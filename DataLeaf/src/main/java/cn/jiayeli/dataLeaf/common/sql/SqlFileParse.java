package cn.jiayeli.dataLeaf.common.sql;

import cn.jiayeli.dataLeaf.common.constant.FileConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlFileParse {

    private static Logger logger = LoggerFactory.getLogger(SqlFileParse.class);

    public static List<String> parseFile2Sql(String fileName) {

        logger.debug("sql parse file name: [%s]", fileName);

        List<String> sqlList = new ArrayList<String>();

        StringBuilder strBuild = new StringBuilder();

        try {

            List<String> lines = Files.readAllLines(Paths.get(fileName));

            for (String line : lines) {
                line = line.trim();
                // --
                if (line.startsWith(FileConstant.COMMENT_SYMBOL))
                    continue;
                // ;
                strBuild.append(line).append(FileConstant.LINE_FEED);
                if (line.endsWith(FileConstant.SEMICOLON)) {
                    sqlList.add(strBuild.toString());
                    logger.debug("parse sql is: [%s]", strBuild.toString());
                    strBuild.setLength(0);
                }
            }
        } catch (IOException e) {
            logger.error("sqlFile parse error!!!");
/*            logger.error("message ====" + e.getCause().getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));*/
            e.printStackTrace();
        }

        if (strBuild.length() > 0) {
            logger.error("file have sql not end!!! by sql:\n" + strBuild.toString());
            strBuild.setLength(0);
        }
        return sqlList;
    }
}
