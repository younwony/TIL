package level_1;

public class _옹알이_2 {
    public static void main(String[] args) {
        System.out.println(solution(new String[]{"aya", "yee", "u", "maa"}));
    }

    private static String[] joka = {"aya", "ye", "woo", "ma"};

    public static int solution(String[] babbling) {
        int answer = 0;

        for (String str : babbling) {

            while(true){
                String temp = str;
                for (String s : babbling) {
                    str = str.replaceAll(s, "");
                }

                if(temp.equals(str)) {
                    break;
                }
            }

            if(str.isEmpty()) {
                answer++;
            }
        }

        return answer;
    }
}
