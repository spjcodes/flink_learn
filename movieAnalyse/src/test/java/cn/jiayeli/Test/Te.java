package cn.jiayeli.Test;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class Te {
    public static void main(String[] args) {
        Arrays.asList("myThread1", "myThread2", "myThread3", "myThread4", "myThread5").stream().forEach(e -> {
            new Thread(() -> {
                int a = 1;
                ArrayList<Integer> list = new ArrayList<Integer>();
                while (true) {
                    a++;
                    list.add(a);
                }
            }, e).start();
        });


    }

    @Test
    public void t() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        AtomicInteger t = new AtomicInteger(1);

/*        new Thread(() -> {
            try {
                lock.lock();
                while (t.get() != 1) {
                    condition.await();
                }
                for (int i = 0; i < 10; i++) {
                    System.out.println("a"+i);
                }
                t.incrementAndGet();
//                condition1.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
                lock.unlock();
            }
        }).start();*/


        new Thread(() -> {
            try {

                for (int i = 0; i < 10; i++) {
                    System.out.println("a"+i);
                }
//                condition1.signal();
            }    catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("s");
            }
        }).start();

       /* new Thread(() -> {

            try {
                lock.lock();
                while (t.get() != 2) {
                    condition1.await();
                }
                t.incrementAndGet();
                System.out.println("b");
                condition2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }).start();

        new Thread(() -> {
            try {
                lock.lock();
                while (t.get() != 3) {
                    condition2.await();
                }
                System.out.println("c");
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();*/

    }


    @Test
    public void tt() {
        TreeMap<Tuple2<String, Integer>, String> treeMap = new TreeMap<Tuple2<String, Integer>, String>(new Comparator<Tuple2<String, Integer>>() {
            @Override
            public int compare(Tuple2<String, Integer> e, Tuple2<String, Integer> ee) {
                return e.f1 > ee.f1 ? e.f0 == ee.f0 ? 0 : 1 : -1;
//                return e.f1 > ee.f1 ? 1 : -1;
            }
        });

        LinkedHashMap<Tuple2<String, Integer>, String> collect = new LinkedHashMap<>(10, 0.1F, true);

        treeMap.put(Tuple2.of("a", 1), "a1");
        treeMap.put(Tuple2.of("a", 1), "a1");
        treeMap.put(Tuple2.of("a", 2), "a2");
        treeMap.put(Tuple2.of("aa", 2), "aa2    ");


        collect.put(Tuple2.of("a", 1),  "a");
        collect.put(Tuple2.of("aaa", 3), "a");
        collect.put(Tuple2.of("aaab", 4),  "a");
        collect.put(Tuple2.of("aa", 2),  "a");
        collect.put(Tuple2.of("aaa", 2),  "a");

        treeMap.forEach((k, v) -> System.out.println(k + "__" + v));

//        collect.forEach((k, v) -> System.out.println(k+"\t" + v));
    }

    @Test
    public void ttt() {
        LinkedHashSet<Tuple3<String, Long, String>> collect= new LinkedHashSet<>();
        collect.add(Tuple3.of("a", 16L, "A"));
        collect.add(Tuple3.of("aa", 19L, "B"));
        collect.add(Tuple3.of("a", 20L, "A"));
        collect.add(Tuple3.of("aa", 10L, "A"));
        collect.add(Tuple3.of("aaaa", 10L, "A"));
        collect.add(Tuple3.of("aa", 11L, "A"));
        collect.add(Tuple3.of("a", 12L, "A"));
        collect.add(Tuple3.of("a", 10L, "A"));
        collect.add(Tuple3.of("a", 11L, "A"));
        collect.add(Tuple3.of("a", 10L, "A"));
        collect.add(Tuple3.of("a", 12L, "A"));


        Stream<Tuple3<String, Long, String>> sorted = new ArrayList<Tuple3<String, Long, String>>(collect).stream().sorted(new Comparator<Tuple3<String, Long, String>>() {
            @Override
            public int compare(Tuple3<String, Long, String> e, Tuple3<String, Long, String> ee) {
                return e.f1.compareTo(ee.f1);
            }
        });

        sorted.forEach(e -> System.out.println(e));

    }

    @Test
    public void tttt() {
        TreeSet<Tuple2<String, Integer>> treeset = new TreeSet<>(new Comparator<Tuple2<String, Integer>>() {
            @Override
            public int compare(Tuple2<String, Integer> e, Tuple2<String, Integer> ee) {
                return e.f1.compareTo(ee.f1);
            }
        });
        treeset.add(Tuple2.of("a", 3));
        treeset.add(Tuple2.of("a", 4));
        treeset.add(Tuple2.of("a", 1));
        treeset.add(Tuple2.of("aa", 2));
        treeset.add(Tuple2.of("a", 2));
        treeset.add(Tuple2.of("aaa5", 5));

        treeset.forEach(System.out::println);
    }

    @Test
    public void ttttt() {
        TreeMap<Long, Tuple2<String, Long>> treeMap = new TreeMap<>((e, ee) -> e > ee ? -1 : 1);
        add(treeMap, Tuple2.of("a", 1L));
        add(treeMap, Tuple2.of("aa", 2L));
        add(treeMap, Tuple2.of("aaa", 3L));
        add(treeMap, Tuple2.of("a1", 1L));
        add(treeMap, Tuple2.of("a2", 1L));
        add(treeMap, Tuple2.of("a3", 3L));
        add(treeMap, Tuple2.of("a4", 4L));
        add(treeMap, Tuple2.of("a5", 5L));

        treeMap.forEach((k, v) -> System.out.println(v));

    }

    public boolean add(TreeMap<Long,  Tuple2<String, Long>> treeMap, Tuple2<String, Long> value) {
        AtomicBoolean addSuccessful = new AtomicBoolean(false);
        treeMap.values().forEach(e -> {
            if (e.f0.equals(value.f0)) {
                treeMap.remove(e.f1);
                addSuccessful.set(true);
            }
            treeMap.put(value.f1, value);
        });
        return addSuccessful.get();
    }

}
