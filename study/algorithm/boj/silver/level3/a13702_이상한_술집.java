package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a13702_이상한_술집 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int K = Integer.parseInt(stringTokenizer.nextToken());
        int[] person = new int[N];
        int max = 0;
        for (int i = 0; i < person.length; i++) {
            person[i] = Integer.parseInt(bufferedReader.readLine());
            max = Math.max(person[i],max);
        }
        long start = 0;
        long end = max;
        while(end >= start){
            long mid = (start+end)/2;
            if(mid == 0) break;
            int cnt = getCnt(mid, person);
            if(cnt >= K){
                start = mid + 1;
            }else{
                end = mid - 1;
            }
        }
        System.out.println(end);
    }

    private static int getCnt(long mid, int[] person) {
        int sum = 0;
        for (int p : person) {
            sum += p/mid;
        }
        return sum;
    }
}
