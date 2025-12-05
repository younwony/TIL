package Problem_2021_Intern;

public class Problem_1 {
    public static void main(String[] args) {
        System.out.println(solution("one4seveneight"));
    }

    private static int solution(String s) {
        String[] number = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        for (int i = 0; i < number.length; i++) {
            s = s.replace(number[i], i+"");
        }
        return Integer.parseInt(s);
    }
}
