package cn.jiayeli.dataLeaf;

import cn.jiayeli.dataLeaf.common.sql.SqlFileParse;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class SqlParseCase {

    @Test
    public void fileParse2Sql() throws IOException {
        String filePath = "src/test/resources/sqlFile.sql";
        List<String> strings = SqlFileParse.parseFile2Sql(filePath);
        System.out.println(strings.toString());

    }

    @Test
    public void t() {
        tt("a", 1);
        tt("a", 1.1F);
        tt("a", 1.2D);
        tt("a", "str");
        tt("a", true);
    }

    public void tt(String s, Object o) {
        if (o instanceof Integer) {
            System.out.println("int");
        }
        if (o instanceof String) {
            System.out.println("String");
        }
        if (o instanceof Double) {
            System.out.println("Double");
        }
        if (o instanceof Float) {
            System.out.println("Float");
        }
        if (o instanceof Boolean) {
            System.out.println("Boolean");
        }
    }
}
