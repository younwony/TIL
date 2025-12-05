package level_1;

import java.util.ArrayList;

public class _모의고사 {

    public static void main(String[] args) {
        int[] solution1 = solution(new int[]{1,2,5,5,2});

        for (int i : solution1) {
            System.out.print(i);
        }
    }

    private static int[] solution(int[] answers) {
        /* 수포자 1,2,3의 답안지 패턴 저장 */
        int[] person1 = {1,2,3,4,5};
        int[] person2 = {2,1,2,3,2,4,2,5};
        int[] person3 = {3,3,1,1,2,2,4,4,5,5};

        /* 수포자 1,2,3 의 정답 개수 변수 */
        int person1Cnt = 0;
        int person2Cnt = 0;
        int person3Cnt = 0;
        
        /* 답안 측정 */
        for (int i = 0; i < answers.length; i++) {
            int answer1 = answers[i];

            if(person1[i%5] == answer1) person1Cnt++;
            if(person2[i%8] == answer1) person2Cnt++;
            if(person3[i%10] == answer1) person3Cnt++;
        }

        /* 값이 동일할 경우 측정 */
        if(person1Cnt == person2Cnt && person2Cnt == person3Cnt) return new int[]{1,2,3};
        if(person1Cnt == person2Cnt && person1Cnt > person3Cnt) return new int[]{1,2};
        if(person1Cnt == person3Cnt && person1Cnt > person2Cnt) return new int[]{1,3};
        if(person2Cnt == person3Cnt && person2Cnt > person1Cnt) return new int[]{2,3};

        /* 가장 많이 맞은사람이 한명일 경우 측정*/
        int max = Math.max(person1Cnt, Math.max(person2Cnt, person3Cnt));

        if(max == person1Cnt) return new int[]{1};
        if(max == person2Cnt) return new int[]{2};
        if(max == person3Cnt) return new int[]{3};

        return new int[]{};
    }
}
