package exam.interpark;

public class Solve_2 {
    public static void main(String[] args) {
        System.out.println(solution(6,1));
        System.out.println(solution(6,25));
    }

    public static int[] solution(int day, int k) {
        int[] months =  {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] answer = new int[12];

        int sum = day + k - 1;
        for(int i = 0; i < 12; i++){
            answer[i] = sum%7 == 5 ? 1 : sum%7 == 6 ? 1 : 0;
            sum += months[i];
        }

        return answer;
    }
}
