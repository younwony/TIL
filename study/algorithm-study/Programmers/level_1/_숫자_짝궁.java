package level_1;

public class _숫자_짝궁 {
    public static void main(String[] args) {
        System.out.println(solution("100","2345"));
        System.out.println(solution("100","203045"));
        System.out.println(solution("100","123450"));
        System.out.println(solution("12321","42531"));
        System.out.println(solution("5525","1255"));
    }

    public static String solution(String X, String Y) {
        StringBuilder answer = new StringBuilder();
        // 0~9 까지 숫자
        int[] xDuplicate = new int[10];
        int[] yDuplicate = new int[10];

        // x 와 y 중 동일한게 있는지 구분
        // x 값 있을 경우 ++
        for (char c : X.toCharArray()) {
            xDuplicate[c - '0']++;
        }

        // y 값 있을 경우 ++
        for (char c : Y.toCharArray()) {
            yDuplicate[c - '0']++;
        }

        // 동일한 문자 최댓값 구하기
        for(int i = 9; i >= 0; i--){
            int duplicate = Math.min(xDuplicate[i], yDuplicate[i]);
            if(duplicate > 0){
                for(int j = 0; j < duplicate; j++){
                    answer.append(i);
                }
            }
        }

        // 0 시작 할 경우 0으로 반환
        String answerStr = answer.toString();
        if(answerStr.startsWith("0")) {
            return "0";
        }

        // 동일한 문자로 최댓값 생성
        return answerStr.isEmpty() ? "-1" : answerStr;
    }
}
