package _1_Silver.Level_1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class a1697_숨바꼭질 {

	static int n,k;
	static int result = Integer.MAX_VALUE;
	static boolean[] visited = new boolean[1000001];
	static Queue<Integer[]> queue = new LinkedList<>();
	
	public static void main(String[] args){
		// TODO Auto-generated method stub

		Scanner scanner = new Scanner(System.in);
		
		n = scanner.nextInt();
		k = scanner.nextInt();
		
		visited[n] = true;
		bfs(n, 0);
		
		
		System.out.println(n == k ? 0 : result);
	}
	
	public static void bfs(int index, int cnt){
		
		if(index == k){
			if(cnt < result){
				result = cnt;
			}
			return ;
		}
		
		if(!queue.isEmpty())queue.poll();
		
		if(index-1 >= 0 && index+1 <= 100000)if(!visited[index*2]){queue.offer(new Integer[]{index*2, cnt+1});visited[index*2] = true;}
		if(index-1 >= 0)if(!visited[index-1]){queue.offer(new Integer[]{index - 1, cnt+1}); visited[index-1] = true;}
		if(index+1 <= 100000)if(!visited[index+1]){queue.offer(new Integer[]{index + 1, cnt+1});visited[index+1] = true;}
		
		bfs(queue.peek()[0], queue.peek()[1]);
		
	}

}
