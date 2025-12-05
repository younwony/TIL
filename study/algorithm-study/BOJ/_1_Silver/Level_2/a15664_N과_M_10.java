package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class a15664_N과_M_10 {
    static int M;
    static int[] nArray;
    static int[] result;
    static ArrayList<String> resultList = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int N = Integer.parseInt(stringTokenizer.nextToken());
        M = Integer.parseInt(stringTokenizer.nextToken());
        stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        nArray = new int[N];
        result = new int[M];
        for (int i = 0; i < nArray.length; i++) {
            nArray[i] = Integer.parseInt(stringTokenizer.nextToken());
        }

        Arrays.sort(nArray);

        dfs(0, 0);

        for (String s : resultList) {
            System.out.println(s);
        }
    }

    private static void dfs(int target, int index) {
        if(target == result.length){
            StringBuilder stringBuilder = new StringBuilder();
            for (int number : result) {
                stringBuilder
                        .append(number)
                        .append(" ");
            }
            String s = stringBuilder.toString();
            if(!resultList.contains(s)){
                resultList.add(s);
            }
            return;
        }

        for (int i = index; i < nArray.length; i++) {
            result[target] = nArray[i];
            dfs(target + 1, i + 1);
        }
    }

}
