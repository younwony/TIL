package _1_Silver.Level_3;

import java.util.Scanner;

public class a1654_랜선_자르기 {

    public static void main(String[] args) {
        //given
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int requiredCnt = scanner.nextInt();
        long[] lineArray = new long[n];
        long max = 0;
        for (int i = 0; i < lineArray.length; i++) {
            lineArray[i] = scanner.nextInt();
            max = Math.max(max,lineArray[i]);
        }

        //then
        long start = 0;
        long end = max;
        while(end >= start){
            long middle = (start+end) >> 1;
            if(middle == 0) break;
            int lineCnt = 0;
            for (long line : lineArray) {
                lineCnt += line/middle;
            }

            if(lineCnt >= requiredCnt){
                start = middle + 1;
            }else{
                end = middle - 1;
            }
        }
        System.out.println(end);
    }
}
