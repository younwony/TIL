package level_2;

import java.util.Arrays;
import java.util.stream.Collectors;

public class _연속된_부분_수열의_합 {

    public static void main(String[] args) {
        System.out.println(
        Arrays.stream(solution(new int[]{1, 2, 3, 4, 5}, 7))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","))
        );

        System.out.println(
        Arrays.stream(solution(new int[]{1, 1, 1, 2, 3, 4, 5}, 5))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","))
        );

        System.out.println(
        Arrays.stream(solution(new int[]{2, 2, 2, 2, 2}, 6))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","))
        );
    }

    /**
     * 비내림차순으로 정렬된 수열
     * 합이 k인 부분 수열이 여러개인 경우 길이가 짧은 수열 찾기
     * 짧은 수열이 여러개인 경우 시작 인덱스가 작은 수열
     * @param sequence - 수열을 나타내는 정수 배열
     * @param k - 부분 수열의 합
     * @return
     */
    public static int[] solution(int[] sequence, int k) {
        int start = 0, end = 0;
        int sum = 0;
        int answerStart = -1, answerEnd = -1;
        int answerLength = Integer.MAX_VALUE;

        while (end < sequence.length) {
            // 현재 구간에 수를 더함
            sum += sequence[end];

            // 합이 k 이상일 경우
            while (sum >= k) {
                if (sum == k && end - start < answerLength) {
                    answerStart = start;
                    answerEnd = end;
                    answerLength = end - start;
                }
                // 구간에서 첫 번째 숫자를 뺌 (왼쪽 포인터를 오른쪽으로 이동)
                sum -= sequence[start];
                start++;
            }
            // 끝 포인터를 오른쪽으로 이동
            end++;
        }

        return new int[]{answerStart, answerEnd};
    }
}
