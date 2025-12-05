package me.wony.level_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class _H_index {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{3,0,6,1,5}));
        System.out.println(solution(new int[]{4,32,23,9,2,4,53}));
    }
    private static int solution(int[] citations) {

        ArrayList<Integer> citationList = new ArrayList<>();
        for (int citation : citations) {
            citationList.add(citation);
        }

        Collections.sort(citationList, Collections.reverseOrder());

        int maxH = citations.length;
        int cnt = 0;
        while(maxH > 0){
            cnt = 0;
            /*논문이 maxH 이상 인용된거면 더하기*/
            for (int citation : citationList) {
                if(citation >= maxH) cnt++;
            }

            /*인용된 논문 개수가 현재 맥스 H 이상이면 종료*/
            if(cnt >= maxH) break;

            maxH--;
        }

        return maxH;
    }
}
