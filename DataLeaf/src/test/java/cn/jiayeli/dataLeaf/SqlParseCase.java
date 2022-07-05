package cn.jiayeli.dataLeaf;

import cn.jiayeli.dataLeaf.common.constant.FileConstant;
import cn.jiayeli.dataLeaf.common.sql.SqlFileParse;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SqlParseCase {
    @Test
    public void fileParse2Sql() throws IOException {
        String filePath = "/home/kuro/workspace/bigdata/FLINK_LEARN/DataLeaf/src/test/resources/sqlFile.sql";
        List<String> strings = SqlFileParse.parseFile2Sql(filePath);
        System.out.println(strings.toString());

    }
}
