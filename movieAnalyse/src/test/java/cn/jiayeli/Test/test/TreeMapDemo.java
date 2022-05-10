package cn.jiayeli.Test.test;

import org.apache.flink.api.java.tuple.Tuple2;
import org.junit.Test;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class TreeMapDemo {

    public static void main(String[] args) {
        new TreeMapDemo().ttttt();
    }

    public void ttttt() {

        TreeMap<String, String> treeMap1 = new TreeMap<>();
        TreeMap<Long, Tuple2<String, Long>> treeMap = new TreeMap<>(Long::compareTo);
        Tuple2<String, Long> a = Tuple2.of("a", 1L);

/*        add(treeMap, Tuple2.of("a", 1L));
        add(treeMap, Tuple2.of("a", 2L));
        add(treeMap, Tuple2.of("aa", 2L));
        add(treeMap, Tuple2.of("aa", 3L));
        add(treeMap, Tuple2.of("aaa", 3L));
        add(treeMap, Tuple2.of("a1", 1L));
        add(treeMap, Tuple2.of("a2", 1L));
        add(treeMap, Tuple2.of("a3", 3L));
        add(treeMap, Tuple2.of("a4", 4L));
        add(treeMap, Tuple2.of("a5", 4L));
        add(treeMap, Tuple2.of("a5", 5L));
        add(treeMap, Tuple2.of("a5", 6L));*/

        treeMap1.put("1a", "a");
        treeMap1.put("1b", "a");
        treeMap1.put("1c", "a");
        treeMap1.put("2a", "a");

        treeMap1
                .forEach((k, v) -> System.out.println(k + "\t" + v));

    }

    public boolean add(TreeMap<Long,  Tuple2<String, Long>> treeMap, Tuple2<String, Long> value) {
        AtomicBoolean addSuccessful = new AtomicBoolean(false);
        treeMap.values().forEach(e -> {
            if (e.f0.equals(value.f0)) {
                treeMap.replace(e.f1, e, value);
                addSuccessful.set(true);
            }
        });
        treeMap.put(value.f1, value);
        return addSuccessful.get();
    }
}
