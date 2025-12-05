package level_2;

public class _124_나라의_숫자 {
    public static void main(String[] args) {
        int i = 1;
        while(true){
            String s = solution2(i);
            String solution = solution(i);
            System.out.println(s + "\t" + solution);
            if(!s.equals(solution)) break;
            i++;
            if(i == 500_000_000) break;
        }
    }

    /**
     * 1 안
     * 답안 100% 성공
     * 효율성 0% 실패
     *
     * 3진법과 비교하면서 답안풀었지만 시간이 평균 18ms로 효율성에서 시간초과가 낫다.
     */
    private static String solution(int n) {

        String answer = "";

        int[] oneTwoFour = new int[]{4,1,2};

        while(n > 0){
            answer = oneTwoFour[n%3] + answer;
            n = (n-1)/3;
        }

        return answer;
    }

    /**
     * 2안
     * 답 100%
     * 효율성 100%
     * 1안 이 효율성으로 실패하면서 좀더 규칙성을 찾으면서 구현한 결과값
     * 다소 무식한 방법으로 약 10~15까지의 값을 3진법과 (1,2,4)진법을 비교하면서 규칙성을 파악해 구현하였다.
     */
    private static String solution2(int n) {

        String answer = Integer.toString(n, 3);

        /**
         * 1,2,4 3개의 수만을 가진 진법이므로
         * 3진법과 결과값을 비교환결과
         * '10' 은 '04'로 '20'은 '14'로 '40'은 '24'로 변환되는 규칙을 발견하였다.
         * 10의 경우 04로 변경할 때 예로 110 일경우 104로 10이 재차 반복 되어 나타낼 경우가 있어 없을 떄 까지 while문으로 제거하였다.
         */
        while(answer.indexOf("10") > -1){
            answer = answer.replace("10", "04");
        }
        answer = answer.replace("20", "14");
        /**
         * 40의 경우도 역시 1000일 경우 0400 -> 02400 -> 240 으로 변경되면서 뒤에 40 이 반복될 수 가 있기 떄문에 없을 떄 까지 while문으로 제거하였다.
         */
        while(answer.indexOf("40") > -1){
            answer = answer.replace("40", "24");
        }

        if(answer.charAt(0) == '0') answer = answer.substring(1, answer.length());

        return answer;
    }
}
