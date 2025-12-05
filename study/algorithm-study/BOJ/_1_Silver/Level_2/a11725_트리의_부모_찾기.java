package _1_Silver.Level_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class a11725_트리의_부모_찾기 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        int[] answer = new int[n];
        ArrayList<ArrayList<Integer>> field = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            field.add(new ArrayList<>());
        }

        for(int i = 0; i < n-1; i++){
            String[] ab = bufferedReader.readLine().split(" ");
            int a = Integer.parseInt(ab[0])-1;
            int b = Integer.parseInt(ab[1])-1;
            field.get(a).add(b);
            field.get(b).add(a);
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        while(!queue.isEmpty()){
            int now = queue.poll();
            for(int num : field.get(now)){
                if(answer[num] == 0) {
                    answer[num] = now+1;
                    queue.add(num);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 1; i < answer.length; i++){
            stringBuilder.append(answer[i]).append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
