package _1_Silver.Level_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class a2667_단지번호붙이기 {

	static char[][] field;
	static int dangi = 1;
	static Map<Integer, Integer> resultMap = new HashMap<>();
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		int n = Integer.valueOf(bufferedReader.readLine());
		
		field = new char[n][n];
		
		for(int i =0 ; i< n; i++){
			field[i] = bufferedReader.readLine().toCharArray();
		}
		
		for(int i =0 ; i < n; i++){
			for(int j = 0; j < n; j++){
				if(field[i][j]  == '1'){
					bfs(i,j);
					dangi++;
				}
			}
		}
		
		System.out.println(--dangi);
		ArrayList<Integer> keyList = new ArrayList<>(resultMap.values());
		Collections.sort(keyList);
		
		for(int key : keyList){
			System.out.println(key);
		}
		
		
	}
	
	public static void bfs(int x, int y){
		field[x][y] = '0';
		
		if(resultMap.containsKey(dangi)){
			resultMap.put(dangi,resultMap.get(dangi)+1);
		}else{
			resultMap.put(dangi,1);
			
		}
		
		if(x - 1 >= 0){
			if(field[x-1][y] == '1'){
				bfs(x-1, y);
			}
		}
		if(y - 1 >= 0){
			if(field[x][y-1] == '1'){
				bfs(x, y-1);
			}
		}
		if(x + 1 < field.length){
			if(field[x+1][y] == '1'){
				bfs(x+1, y);
			}
		}
		if(y + 1 < field.length){
			if(field[x][y+1] == '1'){
				bfs(x, y+1);
			}
		}
	}

}
