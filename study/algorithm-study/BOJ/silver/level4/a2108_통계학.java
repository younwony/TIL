/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a2108_통계학 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 26.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
		
		try {
			int testCount = Integer.parseInt(bufferedReader.readLine());
			int[] countArray = new int[8001];
			int avg,sum,middle,countNum,count,highRowNum;
			sum = 0;
			countNum = 0;
			count = 0;
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			ArrayList<Integer> countList = new ArrayList<Integer>();
			for(int i = 0; i < testCount; i++){
				arrayList.add(Integer.parseInt(bufferedReader.readLine()));
			}
			/*오름차순 정렬*/
			arrayList.sort(null);
			
			/* 음수포함 Max 절대값이 4000이여서 4000을 더한 index에 카운팅*/
			for(int num : arrayList){
				countArray[num+4000]++;
				sum+=num;
			}
			
			/* 산술평균 소숫점 첫째 자리 반올림 */
			avg = (int)Math.round((double)sum/testCount);
			
			/* 중간값 */
			middle = arrayList.get(arrayList.size()/2);
			
			/* 최대 카운트 값 구하기 */
			for(int i = 0; i < countArray.length; i++){
				if(countArray[i] > count){
					countNum = i;
					count = countArray[i];
				}
			}
			
			/* 최대 카운트 값 중복일 경우 두번째 값을 구하기위해 최대 카운트값이 공통인 값들을 ArrayList에 담아준다 */
			for(int i = 0; i < countArray.length; i++){
				if(countArray[i] == count){
					countList.add(i-4000);
				}
			}
			
			/* ArrayList 사이즈가 2보다 크면 두번째값 아닐경우 기존에 구한 값 */
			if(countList.size() >= 2) {countNum = countList.get(1);}
			else{countNum -= 4000;}
			
			/* 주어진 값의 범위 */
			highRowNum = arrayList.get(arrayList.size()-1) - arrayList.get(0);
			System.out.println(avg);
			System.out.println(middle);
			System.out.println(countNum);
			System.out.println(highRowNum);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally{
			try {
				if(bufferedReader != null){ bufferedReader.close();}
				if(bufferedWriter != null){ bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}
