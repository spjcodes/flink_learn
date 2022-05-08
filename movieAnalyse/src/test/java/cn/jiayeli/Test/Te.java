package cn.jiayeli.Test;

import org.apache.flink.api.java.tuple.Tuple2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
            } catch (Exception e) {
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
}
