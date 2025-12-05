package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a7795_먹을_것인가_먹힐_것인가 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bufferedReader.readLine());

        StringBuilder stringBuilder = new StringBuilder();
        StringTokenizer stringTokenizer;
        while(T-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int n = Integer.parseInt(stringTokenizer.nextToken());
            int m = Integer.parseInt(stringTokenizer.nextToken());

            int[] aArray = new int[n];
            int[] bArray = new int[m];

            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int i = 0; i < n; i++) {
                aArray[i] = Integer.parseInt(stringTokenizer.nextToken());
            }
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            for (int i = 0; i < m; i++) {
                bArray[i] = Integer.parseInt(stringTokenizer.nextToken());
            }

            stringBuilder
                    .append(solved(aArray,bArray))
                    .append("\n");
        }

        System.out.println(stringBuilder.toString());
    }

    private static int solved(int[] aArray, int[] bArray) {

        int cnt = 0;

        Arrays.sort(aArray);
        Arrays.sort(bArray);

        for (int i = aArray.length - 1; i >= 0; i--) {
            for (int j = bArray.length - 1; j >= 0; j--) {
                if(aArray[i] > bArray[j]){
                    cnt += j+1;
                    break;
                }
            }
        }

        return cnt;
    }

    private static int getIndex(ArrayList<Integer> bList, Integer a) {
        int left = 0;
        int right = bList.size() - 1;
        int middle = 0;
        while(left <= right){
            middle = (left + right)/2;
            Integer b = bList.get(middle);
            if(b < a){
                left = middle + 1;
            }else{
                right = middle - 1;
            }
        }
        return left;
    }
}
