package _2_Gold.Level_4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class a9466_텀프로젝트 {

	static int count = 0; // TestCase 마다 팀을 이룬 학생 수
	static int[] field;
	static boolean[] visited; // 학생 방문 체크
	static boolean[] cycle; // 학생 사이클 체크
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		int testCase = Integer.valueOf(bufferedReader.readLine());
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < testCase; i++){
			count = 0; // TestCase마다 Count 초기화
			int personCnt = Integer.valueOf(bufferedReader.readLine());
			field = new int[personCnt+1];
			StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine(), " ");
			for(int x = 1; x <= personCnt; x++){
				field[x] = Integer.valueOf(stringTokenizer.nextToken());
			}
			
			visited = new boolean[personCnt+1];
			cycle = new boolean[personCnt+1];
			
			for(int x = 1; x <= personCnt; x++){
				if(!visited[x] && !cycle[x]) dfs(x);
			}
			
			// count 는 그룹을 이룬 학생 수 이다.
			result.append(personCnt - count).append("\n");
			
		}
		
		System.out.println(result.toString());

	}
	
	public static void dfs(int x){
		
		visited[x] = true;
		
		int nextX = field[x];
		
		// 현재 학생이 가르키는 학생이 방문 된지 체크
		if(!visited[nextX]){
			dfs(nextX);
		}else{
			// 현재 학생이 가르키는 학생이 싸이클을 형성하고 있는지 체크 
			if(!cycle[nextX]){
				while(x != nextX){
					nextX = field[nextX];
					count++;
				}
				count++;
			}
		}
		
		/**
		 * 현재 학생 사이클 체크 확인
		 * 
		 * 이미 싸이클을 이루고있는 학생들을 다른 학생이 가르킬 경우 방지
		 * ex) (2,3,5) 그룹에서 4번째 학생이 5를 가르킬경우 방지 -> 없을 경우 위의 while문을 다시 타게된다. 
		 */
		cycle[x] = true;
		
	}

}
