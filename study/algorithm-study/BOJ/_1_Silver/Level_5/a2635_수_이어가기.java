package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class a2635_수_이어가기 {
    private static ArrayList<Integer> LIST = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(bufferedReader.readLine());
        int maxSize = 0;
        StringBuilder result = new StringBuilder();
        for(int i = 0; i <= n; i++){
            LIST.add(n);
            LIST.add(i);
            recursive(n, i);

            if(LIST.size() > maxSize){
                maxSize = LIST.size();
                result.setLength(0);
                result
                        .append(maxSize)
                        .append("\n");
                for (Integer number : LIST) {
                    result
                            .append(number)
                            .append(" ");
                }
            }

            LIST.clear();
        }
        System.out.println(result.toString());
    }

    private static void recursive(int pre, int now) {
        int next = pre - now;
        if(next < 0){
            return;
        }

        LIST.add(next);

        recursive(now, next);
    }
}
