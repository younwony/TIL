package level_2;

import java.util.HashSet;

public class _방문_길이 {

    public static void main(String[] args) {
        System.out.println(solution("ULURRDLLU"));
        System.out.println(solution("LULLLLLLU"));
    }

    private static int solution(String dirs) {
        int answer = 0;
        HashSet<String> dirSet = new HashSet<>();
        int startX = 0;
        int endX = 0;
        int startY = 0;
        int endY = 0;
        for (int i = 0; i < dirs.length(); i++) {
            //given
            char dir = dirs.charAt(i);
            if(dir == 'U') {
                if(endY == 5) continue;
                endY++;
            }
            if(dir == 'D'){
                if(endY == -5) continue;
                endY--;
            }
            if(dir == 'R'){
                if(endX == 5) continue;
                endX++;
            }
            if(dir == 'L'){
                if(endX == -5) continue;
                endX--;
            }
            String startToEndStr = startToEndStr(startX, endX, startY, endY);
            String endToStartStr = endToStartStr(startX, endX, startY, endY);
            startX = endX;
            startY = endY;

            //when
            if(dirSet.contains(startToEndStr) || dirSet.contains(endToStartStr)) continue;
            answer++;
            dirSet.add(startToEndStr);
            dirSet.add(endToStartStr);
        }
        return answer;
    }


    private static String startToEndStr(int startX, int endX, int startY, int endY) {
        return startX
                + "," + endX
                + "," + startY
                + "," + endY;
    }
    private static String endToStartStr(int startX, int endX, int startY, int endY) {
        return endX
                + "," + startX
                + "," + endY
                + "," + startY;
    }
}
