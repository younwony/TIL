package me.wony.level_2;

import java.util.*;

public class _순위_검색 {

    /*
    * info : 개발언어 직군 경력 소울푸드 점수
    *
    * 개발언어 : cpp, java, python
    * 직군 : backend, frontend
    * 경력 : junior, senior
    * 소울푸드 : chicken, pizza
    * 점수 : 1 ~ 100_000
    *
    * query :개발언어 and 직군 and 경력 and 소울푸드
    *
    * 개발언어 : cpp, java, python, -
    * 직군 : backend, frontend, -
    * 경력 : junior, senior, -
    * 소울푸드 : chicken, pizza, -
    * * - 은 조건 제외
    * */
    public static void main(String[] args) {
        String[] info = new String[]{"java backend junior pizza 150","python frontend senior chicken 210","python frontend senior chicken 150","cpp backend senior pizza 260","java backend junior chicken 80","python backend senior chicken 50"};
        String[] query = new String[]{"java and backend and junior and pizza 100","python and frontend and senior and chicken 200","cpp and - and senior and pizza 250","- and backend and senior and - 150","- and - and - and chicken 100","- and - and - and - 150"};
        System.out.println(solution(info, query));
    }

    private static int[] solution(String[] info, String[] query) {
        //given
        int[] answer = new int[query.length];
        for (int i = 0; i < query.length; i++) {
            query [i] = query[i].replace(" and", "");
        }

        /* qeury, score 맵 생성*/
        HashMap<String, ArrayList<Integer>> infoAndScoreMap = new HashMap<>();
        for (String applicant : info) {
            String[] dataArray = applicant.split(" ");
            insertInfoToMap(dataArray,0,"", infoAndScoreMap);
        }

        /* Map BinarySearch를 위해 모두 정렬*/
        for (ArrayList<Integer> value : infoAndScoreMap.values()) {
            Collections.sort(value);
        }

        //when
        for (int i = 0; i < query.length; i++) {
            String queryString = query[i];
            int scoreIndex = queryString.lastIndexOf(" ");
            Integer score = Integer.valueOf(queryString.substring(scoreIndex+1));
            String infoString = queryString.substring(0, scoreIndex);
            /* 해당 쿼리문에 해당하는 Key가 없으면 0*/
            if(!infoAndScoreMap.containsKey(infoString)) {
                answer[i] = 0;
                continue;
            }

            ArrayList<Integer> scoreList = infoAndScoreMap.get(infoString);
            answer[i] = getScoreCnt(scoreList, score);
        }

        return answer;
    }

    /* Binary Search 로 값 구하기*/
    private static int getScoreCnt(ArrayList<Integer> scoreList, Integer score) {
        int start = 0;
        int end = scoreList.size()-1;

        while(start<=end){
            int mid = (start+end)/2;
            /*주어진 스코어이상인 값 중 스코어 와 가장 차이가 적은 인덱스에 Start가 위치할때까지
            *
            * ex. 50(0), 80(1), 150(2), 150(3), 210(4), 260(5) / Score : 150
            * Start = 2에 위치하게 된다.
            * 따라서 결과값 공식은 주어진 리스트 크기에서 start를 뺴주면 되겟다.
            * */
            if(scoreList.get(mid)<score){
                start=mid+1;
            }else{
                end=mid-1;
            }
        }

        return scoreList.size()-start;
    }

    private static void insertInfoToMap(String[] dataArray, int index, String info, HashMap<String, ArrayList<Integer>> infoAndScoreMap) {
        if(index == 4){
            info = info.substring(1);
            ArrayList<Integer> scoreList = infoAndScoreMap.getOrDefault(info, new ArrayList<>());
            scoreList.add(Integer.valueOf(dataArray[4]));
            infoAndScoreMap.put(info, scoreList);
            return ;
        }

        insertInfoToMap(dataArray, index+1, info + " " + dataArray[index], infoAndScoreMap);
        insertInfoToMap(dataArray, index+1, info + " -", infoAndScoreMap);
    }

    /**
     * 효율성 실패
     */
    private static int[] solution2(String[] info, String[] query) {
        int[] answer = new int[query.length];

        ArrayList<Applicant> applicantArrayList = new ArrayList<>();

        for (String person : info) {
            String[] data = person.split(" ");
            applicantArrayList.add(new Applicant(data[0], data[1], data[2], data[3], Integer.valueOf(data[4])));
        }

        Collections.sort(applicantArrayList, new Comparator<Applicant>() {
            @Override
            public int compare(Applicant o1, Applicant o2) {
                return o2.data.compareTo(o1.data);
            }
        });


        for (int i = 0; i < query.length; i++) {
            String request = query[i];
            String[] requestQuery = request.split(" and ");
            String lang = requestQuery[0];
            String position = requestQuery[1];
            String career = requestQuery[2];
            String[] foodScore = requestQuery[3].split(" ");
            String food = foodScore[0];
            Integer score = Integer.valueOf(foodScore[1]);

            int requestCnt = 0;
            for (Applicant applicant : applicantArrayList) {
                if(applicant.score < score) break;
                if(!isCondition(lang, applicant.lang))continue;
                if(!isCondition(position, applicant.position))continue;
                if(!isCondition(career, applicant.career))continue;
                if(!isCondition(food, applicant.food))continue;
                requestCnt++;
            }
            answer[i] = requestCnt;
        }

        return answer;
    }

    private static boolean isCondition(String requestData , String applicantData) {
        if("-".equals(requestData)) return true;
        return applicantData.equals(requestData);
    }

    static class Applicant{

        String lang;
        String position;
        String career;
        String food;
        int score;
        String data;

        public Applicant(String lang, String position, String career, String food, int score) {
            this.lang = lang;
            this.position = position;
            this.career = career;
            this.food = food;
            this.score = score;
            this.data = lang + position + career + food + score;
        }
    }
}
