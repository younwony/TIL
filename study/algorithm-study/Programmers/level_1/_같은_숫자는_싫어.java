package level_1;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class _같은_숫자는_싫어 {

    public static void main(String[] args) {
        solution(new int[]{1,1,3,3,0,1,1});
        solution(new int[]{4,4,4,3,3});
    }

    private static int[] solution(int []arr) {
        int[] answer = {};

        /* 입력 큐 생성 */
        Deque<Integer> queue = new LinkedList<>();
        /* 결과값 저장 큐 생성 */
        Deque<Integer> saveQueue = new LinkedList<>();

        for (Integer integer : arr) {
            queue.add(integer);
        }

        /* 첫번 째 값 가져오기 */
        saveQueue.add(queue.poll());
        /* 저장된값과 입력값이 같은지 비교하며 다를 경우 저장 */
        while(!queue.isEmpty()){
            Integer item = queue.poll();
            if(item != saveQueue.peekLast()) saveQueue.add(item);
        }

        /* 결과값 저장 */
        answer = new int[saveQueue.size()];
        int i =0;
        while(!saveQueue.isEmpty()){
            answer[i++] = saveQueue.poll();
        }

        return answer;
    }
}
