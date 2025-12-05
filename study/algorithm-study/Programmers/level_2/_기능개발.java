package level_2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class _기능개발 {
    public static void main(String[] args) {
        System.out.println(solution(new int[]{93,30,55}, new int[]{1,30,5}));
        System.out.println(solution(new int[]{95,90,99,99,80,99}, new int[]{1,1,1,1,1,1}));
    }

    private static int[] solution(int[] progresses, int[] speeds) {
        int[] answer = {};

        Queue<Func> funcQueue = new LinkedList<>();

        /* 큐에 넣기 */
        for (int i = 0; i < progresses.length; i++) {
            int progress = progresses[i];
            int speed = speeds[i];
            funcQueue.add(new Func(progress, speed));
        }

        ArrayList<Integer> answerList = new ArrayList<>();

        /* 모든 기능이 완료 될때 까지*/
        while(!funcQueue.isEmpty()){
            Func func = funcQueue.peek();
            int progress = func.getProgress();
            int speed = func.getSpeed();

            /* 현재 기능이 완료될때까지 걸리는 시간 */
            int remainDevTime = getRemainDevTime(progress, speed);

            /* 완료될 때 까지 걸리는 시간을 모든 기능 시간에 더하기 */
            setDevTimePlus(funcQueue, remainDevTime);

            /* 완료된 기능 개수를 결과 리스트에 추가 */
            answerList.add(getFinishFunc(funcQueue));

            /* 완료된 기능 제거 */
            removeFinishFunc(funcQueue);
        }

        answer = new int[answerList.size()];
        for (int i = 0; i < answer.length; i++) {
            answer[i] = answerList.get(i);

        }
        return answer;
    }

    /* 완료된 Queue 제거 */
    private static void removeFinishFunc(Queue<Func> funcQueue) {
        while(!funcQueue.isEmpty()){
            Func func = funcQueue.peek();
            int progress = func.getProgress();
            if(progress >= 100)  funcQueue.poll();
            if(progress < 100) break;
        }
    }

    /* 완료될때까지 */
    private static int getFinishFunc(Queue<Func> funcQueue) {
        int deployCnt = 0;
        for (Func func : funcQueue) {
            if(func.getProgress() >= 100){
                deployCnt++;
            }else{
                break;
            }
        }
        return deployCnt;
    }

    /* 개발 시간 추가하기 */
    private static void setDevTimePlus(Queue<Func> funcQueue, int remainDevTime) {
        for (Func func : funcQueue) {
            int progress = func.getProgress();
            int speed = func.getSpeed();
            int updateProgress = progress + speed * remainDevTime;
            func.setProgress(updateProgress);
        }
    }

    /* 완료까지 남은시간 가져오기*/
    private static int getRemainDevTime(int progress, int speed) {

        int time = 0;

        while(progress < 100){
            progress += speed;
            time++;
        }

        return time;
    }

    /* 기능 */
    static class Func{
        int progress;
        int speed;

        public Func(int progress, int speed) {
            this.progress = progress;
            this.speed = speed;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }
    }
}

