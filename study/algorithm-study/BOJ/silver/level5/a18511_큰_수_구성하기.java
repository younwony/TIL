package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class a18511_큰_수_구성하기 {

    static ArrayList<Integer> kList = new ArrayList<>();
    static int result;
    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        N = Integer.parseInt(stringTokenizer.nextToken());
        int K = Integer.parseInt(stringTokenizer.nextToken());
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int[] kArray = new int[K];
        for(int i = 0 ; i < K; i++){
            kArray[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        for (int k : kArray) {
            dfs(kArray,k);
        }

        System.out.println(result);

    }

    private static void dfs(int[] kArray, int num) {
        if(num > N){
            return;
        }

        result = Math.max(result,num);

        for (int k : kArray) {
            dfs(kArray,num*10 + k);
        }
    }
}
