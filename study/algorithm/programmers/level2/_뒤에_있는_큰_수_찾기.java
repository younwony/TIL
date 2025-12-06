package level_2;

import java.util.Stack;

public class _뒤에_있는_큰_수_찾기 {
    public static void main(String[] args) {
        int[] solution = solution(new int[]{2, 3, 3, 5});
        for (int i : solution) {
            System.out.println("i = " + i);
        }
    }

    public static int[] solution(int[] numbers) {
        int[] result = new int[numbers.length];
        Stack<Integer> numberIndexStack = new Stack<>();

        numberIndexStack.push(0);
        for (int i = 1; i < numbers.length; i++) {
            while (!numberIndexStack.isEmpty() && numbers[numberIndexStack.peek()] < numbers[i]) {
                result[numberIndexStack.pop()] = numbers[i];
            }
            numberIndexStack.push(i);
        }

        while (!numberIndexStack.isEmpty()) {
            result[numberIndexStack.pop()] = -1;
        }

        return result;
    }
}
