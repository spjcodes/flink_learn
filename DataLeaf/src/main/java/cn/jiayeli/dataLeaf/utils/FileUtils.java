package cn.jiayeli.dataLeaf.utils;

import java.io.File;

public class FileUtils {

    public static boolean fileExists(String path) {
        return new File(path).exists();
    }
}
