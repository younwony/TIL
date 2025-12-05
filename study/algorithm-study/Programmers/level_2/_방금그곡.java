package level_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class _방금그곡 {

    /**
     * 제목, 시작 시간, 끝난 시각, 악보
     * 음은 1분에 1개씩 재생
     * 반드시 처음부터 재생, 길이보다 시간이 길때는 처음부터 다시 재생
     * 길이보다 짧을 경우 재생시간까지 재생
     * 00:00 을 넘어서 재생되는 일 없다.
     * 중복 일경우 재생된 시간이 제일 긴음악 제목 반환, 재생된 시간도 같을 경우 먼저 입력된 음악 제목 반환
     * 없을 때는 (None) 반환
     * 1 <= m <= 1439;
     * musicinfos.length <= 100
     * 시작 시각, 끝난 시각, 음악제목, 악보정보
     * 시각 : 24시간 형식(HH:MM)
     * 음악 제목 (',')문자 제외 1 <= 제목 <= 64
     * 악보정보 음  1 <= 음 개수 <= 1439
     */
    public static void main(String[] args) {
        System.out.println(solution("ABCDEFG", new String[]{"12:00,12:14,HELLO,CDEFGAB", "13:00,13:05,WORLD,ABCDEF"}));
        System.out.println(solution("CC#BCC#BCC#BCC#B", new String[]{"03:00,03:30,FOO,CC#B", "04:00,04:08,BAR,CC#BCC#BCC#B"}));
        System.out.println(solution("ABC", new String[]{"11:50,12:00,HELLO,C#DEFGAB", "13:00,13:05,WORLD,ABCDEF"}));
        System.out.println(solution("CCB", new String[]{"03:00,03:10,FOO,CCB#CCB", "04:00,04:08,BAR,ABC"}));
    }

    private static String solution(String m, String[] musicinfos) {
        ArrayList<Music> musicList = new ArrayList<>();
        for (int i = 0; i < musicinfos.length; i++) {
            String musicinfo = musicinfos[i];
            String[] info = musicinfo.split(",");
            String startTime = info[0];
            String endTime = info[1];
            String musicTitle = info[2];
            String melody = info[3];

            int playTime = getPlayTime(startTime, endTime);
            String totalMelody = getPlayMelody(melody, playTime);
            if(isMelody(m, totalMelody)) musicList.add(new Music(i, playTime, musicTitle));
        }
        if(musicList.size() == 0) return "(None)";

        Collections.sort(musicList, new Comparator<Music>() {
            @Override
            public int compare(Music o1, Music o2) {
                if(o2.playTime == o1.playTime){
                    return o1.index - o2.index;
                }
                return o2.playTime - o1.playTime;
            }
        });

        return musicList.get(0).musicTitle;
    }

    private static boolean isMelody(String m, String totalMelody) {
        int mIndex = totalMelody.indexOf(m);
        if(mIndex == -1) return false;
        HashSet<Integer> shapMelodySet =new HashSet<>();
        String shapM = m+"#";
        int mShapIndex = -1;
        while(true){
            mShapIndex = totalMelody.indexOf(shapM, mShapIndex+1);
            if(mShapIndex == -1) break;
            shapMelodySet.add(mShapIndex);
        }

        while(true){
            if(!shapMelodySet.contains(mIndex)) break;
            mIndex = totalMelody.indexOf(m, mIndex+1);
            if(mIndex == -1) return false;
        }
        return true;
    }

    private static String getPlayMelody(String melody, int playTime) {
        StringBuilder stringBuilder = new StringBuilder();
        /* 전방 탐색 사용 (?=)*/
        String[] melodyArray = melody.split("(?=[A-Z])");
        for(int i = 0; i < playTime; i++){
            int melodyIndex = i % melodyArray.length;
            String nowMelody = String.valueOf(melodyArray[melodyIndex]);
            stringBuilder.append(nowMelody);
        }

        return stringBuilder.toString();
    }

    private static int getPlayTime(String startTime, String endTime) {
        String[] startTimes = startTime.split(":");
        String[] endTimes = endTime.split(":");
        int startHour = Integer.parseInt(startTimes[0]);
        int startMin = Integer.parseInt(startTimes[1]);
        int endHour = Integer.parseInt(endTimes[0]);
        int endMin = Integer.parseInt(endTimes[1]);
        int start = startHour*60 + startMin;
        int end = endHour*60 + endMin;
        return end - start;
    }

    static class Music{
        int index;
        int playTime;
        String musicTitle;

        public Music(int index, int playTime, String musicTitle) {
            this.index = index;
            this.playTime = playTime;
            this.musicTitle = musicTitle;
        }
    }
}
