package easy;

public class FrogJmp {

    public static void main(String[] args) {
        System.out.println(solution(10, 85, 30));
        System.out.println(solution(10, 70, 30));
    }

    public static int solution(int X, int Y, int D) {
        return (int) Math.ceil((double) (Y - X) / D);
    }
}
