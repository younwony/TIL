package Problem_2021_Intern;

import java.util.ArrayList;
import java.util.HashSet;

public class Demo {

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("a");
        strings.add("b");
        strings.add("c");
        strings.add(1, "D");
        for (String string : strings) {
            System.out.println(string);
        }

        /*System.out.println(solution(new int[][]{{1,4},{3,4},{3,10}}));
        System.out.println(solution(new int[][]{{1,1},{1,2},{2,2}}));*/
    }

    private static int[] solution(int[][] v) {
        ArrayList<Integer> xSet = new ArrayList<>();
        ArrayList<Integer> ySet = new ArrayList<>();
        for (int i = 0; i < v.length; i++) {
            if(xSet.contains(v[i][0])) xSet.remove((Object)v[i][0]);
            else xSet.add(v[i][0]);

            if(ySet.contains(v[i][1])) ySet.remove((Object)v[i][1]);
            else ySet.add(v[i][1]);

        }
        int[] answer = new int[2];
        answer[0] = xSet.get(0);
        answer[1] = ySet.get(0);

        return answer;
    }
}
