package dev_matching_2022_2;

public class Solved_1 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{2,1,3}));
        System.out.println(solution(new int[]{1,2,3}));
        System.out.println(solution(new int[]{3,2,3,6,4,5}));
    }

    public static int solution(int[] grade) {
        int answer = 0;

        for (int i = 0; i < grade.length - 1; i++) {
            int now = grade[i];
            int min = grade[i];
            for (int j = i + 1; j < grade.length; j++) {
                min = Math.min(min, grade[j]);
            }
            if(now > min){
                answer += now - min;
            }
        }
        return answer;
    }
}
