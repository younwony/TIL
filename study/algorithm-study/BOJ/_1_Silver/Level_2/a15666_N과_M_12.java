package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class a15666_N과_M_12 {

    static int[] inputs;
    static int[] saves;
    static StringBuilder stringBuilder = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = bufferedReader.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        inputs = new int[n];
        saves = new int[m];

        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
        int index = 0;
        while(stringTokenizer.hasMoreTokens()){
            inputs[index++] = Integer.parseInt(stringTokenizer.nextToken());
        }


        Arrays.sort(inputs);

        for(int i = 0; i < n; i++){
            saves[0] = inputs[i];
            dfs(1);
        }

        System.out.println(stringBuilder.toString());
    }

    private static void dfs(int depth) {
        if(depth == saves.length){
            StringBuilder inputWait = new StringBuilder();
            for (int save : saves) {
                inputWait.append(save + " ");
            }
            if(stringBuilder.indexOf(inputWait.toString()) == -1)
                stringBuilder.append(inputWait.toString()).append("\n");
            return;
        }

        for(int i = 0; i < inputs.length; i++){
            if(inputs[i] < saves[depth-1]) continue;
            saves[depth] = inputs[i];
            dfs(depth+1);
        }
    }
}
