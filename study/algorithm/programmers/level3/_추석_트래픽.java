package level_3;

import java.util.Arrays;

public class _추석_트래픽 {

    public static void main(String[] args) {
        System.out.println(solution(new String[]{"2016-09-15 01:00:04.001 2.0s","2016-09-15 01:00:07.000 2s"}));
        System.out.println(solution(new String[]{"2016-09-15 01:00:04.002 2.0s","2016-09-15 01:00:07.000 2s"}));
        System.out.println(solution(new String[]{"2016-09-15 20:59:57.421 0.351s",
                "2016-09-15 20:59:58.233 1.181s",
                "2016-09-15 20:59:58.299 0.8s",
                "2016-09-15 20:59:58.688 1.041s",
                "2016-09-15 20:59:59.591 1.412s",
                "2016-09-15 21:00:00.464 1.466s",
                "2016-09-15 21:00:00.741 1.581s",
                "2016-09-15 21:00:00.748 2.31s",
                "2016-09-15 21:00:00.966 0.381s",
                "2016-09-15 21:00:02.066 2.62s"}));
        System.out.println(solution(new String[]{
                "2016-09-15 20:59:58.233 1.181s",
                "2016-09-15 20:59:58.299 0.8s",
                "2016-09-15 20:59:58.688 1.041s",
                "2016-09-15 20:59:59.591 1.412s",
                "2016-09-15 21:00:00.464 1.466s",
                "2016-09-15 21:00:00.741 1.581s",
                "2016-09-15 21:00:00.748 2.31s"
                }));
    }

    private static int solution(String[] lines) {
        /* Time 필드 생성 0부터 24:00:03 초까지 (Time의길이 3초)*/
        int[] timeArray = new int[24 * 3603 * 1000 ];

        /* 주어진 입력 */
        for (String line : lines) {
            String[] lineArray = line.split(" ");
            /* 완료 시간 */
            String time = lineArray[1];
            /* 걸리는 시간 */
            String processingTime = lineArray[2];
            /* 초단위로 일괄 변환 */
            double timeToSecond = convertSec(time);
            double processingtimeToSecond = convertProcessingSec(processingTime);
            /* 시작 시간*/
            double start = getDouble(timeToSecond - processingtimeToSecond+0.001);
            /* 종료 시간*/
            double end = timeToSecond;
            /* Time 영역에 현재 프로세스 앞뒤로 0.5초간격 체크*/
            setTimeLog(timeArray, start, end);
        }

        /* max 값 */
        Arrays.sort(timeArray);

        return timeArray[timeArray.length-1];
    }

    private static void setTimeLog(int[] timeLog, double start, double end) {
        /* 시작 지점 보다 0.5초 앞에까지*/
        int startToInt = (int) (start*1000) - 500;
        startToInt = Math.max(0, startToInt);
        /* 종료 지점보다 0.499초 뒤까지*/
        int endToInt = (int) (end*1000) + 499;
        endToInt = Math.min(timeLog.length-1, endToInt);

        /* 해당 프로세스 영역 포인트 값 증가*/
        for(;startToInt <= endToInt; startToInt++){
            timeLog[startToInt]++;
        }
    }

    /* Double 연산 과정중 소숫점 자리수가 변동이 일어날 수 있기 떄문에 3자리로 고정*/
    private static double getDouble(double start) {
        return Double.parseDouble(String.format("%.3f",start));
    }
    /* todo: 위의 getDouble과 공용으로 처리 가능*/
    private static double convertProcessingSec(String time) {
        return Double.parseDouble(time.substring(0, time.lastIndexOf("s")));
    }

    /* HH:MM:SS -> second 변환*/
    private static double convertSec(String time) {
        double result = 0;

        String[] timeArray = time.split(":");
        int hour = Integer.parseInt(timeArray[0]);
        int min = Integer.parseInt(timeArray[1]);
        result = Double.parseDouble(timeArray[2]);
        result += hour*60*60;
        result += min*60;

        return result;
    }
}
