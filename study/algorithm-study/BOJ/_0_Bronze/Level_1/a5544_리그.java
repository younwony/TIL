package _0_Bronze.Level_1;

import java.util.*;

public class a5544_리그 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int a,b,aScore,bScore;

        int[] teamArray = new int[n];
        for(int i = 0 ; i < (n*(n-1))/2; i++){
            a = scanner.nextInt();
            b = scanner.nextInt();
            aScore = scanner.nextInt();
            bScore = scanner.nextInt();

            if(aScore > bScore){
                teamArray[a-1] += 3;
            }else if(aScore < bScore){
                teamArray[b-1] += 3;
            }else{
                teamArray[a-1]++;
                teamArray[b-1]++;
            }
        }

        ArrayList<team> teamList = new ArrayList<>();

        for (int i = 0 ; i < n; i++){
            teamList.add(new team(i, teamArray[i]));
        }

        Collections.sort(teamList, new team());

        int rank = 1;
        int rankTemp = 1;
        int temp = 0;

        for(team team : teamList){

            if(team.getScore() == temp){
                team.setRank(rank);
            }else{
                rank = rankTemp;
                team.setRank(rank);
            }
            rankTemp++;
            temp = team.getScore();
        }

        teamList.sort(new Comparator<team>() {
            @Override
            public int compare(team o1, team o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });

        for(team team : teamList){
            System.out.println(team.getRank());
        }
    }
    static class team implements Comparator<team>{

        int index;
        int score;
        int rank;

        @Override
        public int compare(team o1, team o2) {

            return o2.getScore() - o1.getScore();
        }

        public team(int index, int score) {
            this.index = index;
            this.score = score;
        }

        public team() {
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
}

