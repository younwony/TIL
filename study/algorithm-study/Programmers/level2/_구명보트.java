package level_2;

import java.util.ArrayList;
import java.util.Arrays;

public class _구명보트 {

    public static void main(String[] args) {
        System.out.println(solution(new int[]{70,50,80,50}, 100));
        System.out.println(solution(new int[]{70,80,50}, 100));
        System.out.println(solution(new int[]{40, 40, 80}, 160));
    }

    /**
     * 각각 왼쪽(가벼운 사람), 오른쪽(무거운 사람) 에서 오면서 체크
     */
    private static int solution(int[] people, int limit) {
        int answer = 0;
        Arrays.sort(people);
        int left = 0;
        int right = people.length-1;
        
        /* 오른쪽 인덱스가 왼쪽이상일 때까지*/
        while(right >= left){
            answer++;
            int lowWeight = people[left];
            
            /* 가장 무거운 사람부터 가벼운 사람 합이 한계값(limit)보다 작을때까지*/
            while(right > left){
                int highWeight = people[right--];
                if(lowWeight + highWeight <= limit) break;
                /* 현재 가장무거운사람은 현재 가장 가벼운사람보다 가벼운사람이 없기 떄문에 혼자가야하므로 따로 체크해주지 않고 결과값에 추가한다.*/
                answer++;
            }
            left++;
        }
        return answer;
    }

    /**
     * 효율성 테스트 실패
     */
    private static int solution2(int[] people, int limit) {
        /* 최대 2명,, 무게제한 */
        /* 구명보트 최대한 적게 사용 값 구하기*/
        int answer = 0;
        Arrays.sort(people);
        for (int i = 0; i < people.length; i++) {
            /* 가장 가벼운 무게순 */
            int lowWeight = people[i];
            /* 무거운 무게부터 같이 타서 제거(limit+1)*/
            for (int j = people.length -1; j > i; j--) {
                if(lowWeight + people[j] <= limit){
                    people[j] = limit + 1;
                    break;
                }
            }
            /* 이미 제거된 사람일경우 결과 증가 x*/
            if(lowWeight != limit + 1)
                answer++;
        }

        return answer;
    }
}
