package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class a15970_화살표_그리기 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bufferedReader.readLine());

        ArrayList<Integer>[] colorList = new ArrayList[n + 1];

        for (int i = 0; i < colorList.length; i++) {
            colorList[i] = new ArrayList<>();
        }

        StringTokenizer stringTokenizer;

        while(n-- > 0){
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int x = Integer.parseInt(stringTokenizer.nextToken());
            int color = Integer.parseInt(stringTokenizer.nextToken());

            colorList[color].add(x);
        }

        int lenSum = 0;
        for (ArrayList<Integer> list : colorList) {
            Collections.sort(list);
            lenSum += getLen(list);
        }

        System.out.println(lenSum);
    }

    private static int getLen(ArrayList<Integer> list) {

        if(list.size() == 0){
            return 0;
        }

        int lenSum = 0;

        lenSum += list.get(1) - list.get(0);
        lenSum += list.get(list.size() - 1) - list.get(list.size() - 2);

        for (int i = 1; i < list.size() - 1; i++) {
            int x = list.get(i);
            int pre = list.get(i - 1);
            int post = list.get(i + 1);
            lenSum += Math.min(x - pre, post - x);
        }

        return lenSum;
    }
}
