package _1_Silver.Level_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a6236_용돈_관리 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        int M = Integer.parseInt(stringTokenizer.nextToken());

        int[] moneys = new int[N];
        int start = 0;
        int end = 0;
        for (int i = 0; i < moneys.length; i++) {
            moneys[i] = Integer.parseInt(bufferedReader.readLine());
            start = Math.max(start,moneys[i]);
            end += moneys[i];
        }

        int mid = 0;
        while(end >= start){
            mid = (start+end)/2;
            int cnt = getCnt(moneys,mid);
            if(cnt > M){
                start = mid + 1;
            }
            if(cnt <= M){
                end = mid - 1;
            }
        }

        System.out.println(mid);
    }

    private static int getCnt(int[] moneys, int mid) {
        int cnt = 1;
        int mod = mid;
        for (int money : moneys) {
            if(mod < money){
                cnt++;
                mod = mid;
            }
            mod -= money;
        }
        return cnt;
    }
}
