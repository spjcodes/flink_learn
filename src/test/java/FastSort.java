import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class FastSort {

    int realPr = 0;

    @Test
    public void fastOrder() {

        Integer[] outOfOrder = new Integer[1000];
        System.out.println("OutOfOrder");
        for (int i = 0; i < outOfOrder.length; i++) {
            Random random = new Random();
            outOfOrder[i] = random.nextInt(1000);
        }
/*
        outOfOrder[0] = 7;
        outOfOrder[1] = 12;
        outOfOrder[2] = 18;
        outOfOrder[3] = 19;
        outOfOrder[4] = 1;
        outOfOrder[5] = 15;
        outOfOrder[6] = 5;
        outOfOrder[7] = 6;
        outOfOrder[8] = 18;
        outOfOrder[9] = 15;
        */
        System.out.println(Arrays.asList(outOfOrder) + "\n");
        // init outOfOrder[p] <= outOfOrder[pl], so pl can right move one
        partitionSort(outOfOrder, 1, outOfOrder.length-1);








    }

    public void partitionSort(Integer[] outOfOrder, int pl, int pr) {
        int p = pl;
        int temp = 0;
        realPr = pl == 0 && realPr != 0 ? 0 : realPr;

        if (outOfOrder.length > 1) {
            while (pr - pl > 0 ) {
                if (outOfOrder[pl] > outOfOrder[p] && outOfOrder[pr] < outOfOrder[pl]) {
                    temp = outOfOrder[pl];
                    outOfOrder[pl] = outOfOrder[pr];
                    outOfOrder[pr] =temp;
                }
                pl = outOfOrder[pl] <= outOfOrder[p] ? ++pl : pl ;
                pr = outOfOrder[pr] > outOfOrder[p] ? --pr : pr;
                if (pl >= pr) {
                    if (outOfOrder[pr] > outOfOrder[p]) {
                        temp = outOfOrder[pr-1];
                        outOfOrder[pr-1] = outOfOrder[p];
                        outOfOrder[p] = temp;
                    } else {
                        temp = outOfOrder[pr];
                        outOfOrder[pr] = outOfOrder[p];
                        outOfOrder[p] = temp;
                    }
                    realPr = realPr == 0 ? pr : realPr;
                    System.out.println("p:\t" + p + "\tpr:\t" + pr);
                    System.out.println("p:\t" + outOfOrder[p] + "\tpr:\t" + outOfOrder[pr]);
                    System.out.println(Arrays.asList(outOfOrder));
                    partitionSort(outOfOrder, pr+1, outOfOrder.length-1);
                    partitionSort(outOfOrder, 0, realPr-1);
                }
            }
        }

    }
}
