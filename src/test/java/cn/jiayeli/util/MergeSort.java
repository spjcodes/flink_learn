package cn.jiayeli.util;

import org.apache.flink.connector.kafka.sink.KafkaSinkBuilder;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MergeSort {

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 3, 4, 6, 9, 10, 2);
        Integer[] array = (Integer[]) integers.toArray();

        int m = array.length / 2;

        split(array, 0, array.length-1);

    }

    /**
     *splitPoint  =  (left + right ) / 2
     * 1 3 4 6 9 10 2
     * 1 3 4 6 | 9 10 2
     * 1 3 4 | 6 | 9 10 | 2
     * 1 3 | 4 | 6 | 9 | 10 | 2
     * 1 | 3 | 4 | 6 | 9 | 10 | 2
     *
     *  1 - 3 | 4 - 6 | 9 - 10 | 2
     *  1 - 3 - 4 - 6 | 9 - 10 - 2
     *  1 - 3 - 4 - 6 | 9 - 10 - 2
     *
     * m = (left + right) / 2 -1
     *
     * @param a
     * @param left
     * @param right
     */
    public static void split(Integer[] a, int left, int right) {
        if (left != right) {
            split(a, left, (left + right)/2);
            split(a, (left + right)/2 + 1, right);
        }
    }

    private static void merge(Integer[] a, int left, int right) {
    }

}
