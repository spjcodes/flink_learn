package source;

public class QuickSort {
    public static void quickSort(int[] a,int l,int r){
        if (l<r){
            int temp=a[l];
            while (l<r){
                while (l<r && a[r]>temp){
                    r--;
                }
                if (l<r){
                    a[l++]=a[r];
                }
                while (l<r && a[l]<=temp){
                    l++;
                }
                if (l<r){
                    a[r--]=a[l];
                }
            }
            a[l]=temp;
            quickSort(a,l,temp-1);
            quickSort(a,temp+1,r);
        }
    }
}

