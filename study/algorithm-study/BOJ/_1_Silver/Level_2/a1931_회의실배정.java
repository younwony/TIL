/**
 * @작성자 wony
 * @작성일 2020. 1. 2.
 * @사용처
 *
 */
package _1_Silver.Level_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 1. 2.
 * @사용처
 * 
 */
public class a1931_회의실배정 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 1. 2.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		int testCount = scanner.nextInt();
		ArrayList<MettingRoom> arrayList = new ArrayList<MettingRoom>();
		for(int i = 0; i< testCount; i++){
			arrayList.add(new MettingRoom(scanner.nextInt(), scanner.nextInt()));
		}
		scanner.close();
		Collections.sort(arrayList);
		
		int nowTime = 0;
		int count = 0;
		for(MettingRoom result : arrayList){
			if(nowTime <= result.startTime){
				count++;
				nowTime = result.endTime; 
			}
		}
		System.out.println(count);
	}
	static class MettingRoom implements Comparable<MettingRoom>{
		int startTime;
		int endTime;

		public MettingRoom(int startTime, int endTime) {
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public int getStartTime() {
			return startTime;
		}

		public void setStartTime(int startTime) {
			this.startTime = startTime;
		}

		public int getEndTime() {
			return endTime;
		}

		public void setEndTime(int endTime) {
			this.endTime = endTime;
		}

		/**
		 * @작성자 wony
		 * @작성일 2020. 1. 8.
		 * @사용처
		 * @param o
		 * @return
		 *
		 */
		@Override
		public int compareTo(MettingRoom o) {
			// TODO Auto-generated method stub
			if(this.endTime < o.endTime){
				return -1;
			}else if(this.endTime == o.endTime){
				return Integer.compare(this.startTime, o.startTime);
			}
			return 1;
		}
	}
}

