/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


/**
 * @작성자 wony
 * @작성일 2019. 11. 12.
 * @사용처 단어정렬
 * 
 */
public class a1181_단어정렬 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 12.
	 * @사용처 ArrayList Compareto 사용법
	 * 
	 * 1. 이중 정렬 재연습
	 * 
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
			
			ArrayList<WordSort> strList = new ArrayList<WordSort>();
			
			String inputData;
			for(int i = 0; i< testCount; i++){
				inputData = bufferedReader.readLine();
				if(strList.indexOf(inputData) == -1)
					strList.add(new WordSort(inputData));
			}
			
			Collections.sort(strList);
			
			for(WordSort data : strList){
				if(!"".equals(data.getName()))
					bufferedWriter.write(data.getName() + "\n");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				if(bufferedReader !=  null) bufferedReader.close();
				if(bufferedWriter != null){bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
	}

	/**
	 * 2020.07.26 Again Study
	 *
	 * public static void main(String[] args) {
	 *         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	 *
	 *         try {
	 *             int n = Integer.valueOf(bufferedReader.readLine());
	 *             Set<String> set = new HashSet<>();
	 *             for(int i = 0; i < n; i++){
	 *                 set.add(bufferedReader.readLine());
	 *             }
	 *             ArrayList<word> wordList = new ArrayList<>();
	 *             for(String setStr : set){
	 *                 wordList.add(new word(setStr));
	 *             }
	 *
	 *             Collections.sort(wordList, new word());
	 *
	 *             StringBuilder result = new StringBuilder();
	 *
	 *             for(word word : wordList){
	 *                 result.append(word.str + "\n");
	 *             }
	 *
	 *             System.out.println(result.toString());
	 *
	 *         } catch (Exception e){
	 *             e.printStackTrace();
	 *         } finally {
	 *             try {
	 *                 if(bufferedReader != null){ bufferedReader.close();}
	 *             }catch (Exception e2){
	 *                 e2.printStackTrace();
	 *             }
	 *         }
	 *     }
	 *
	 *     public static class word implements Comparator<word> {
	 *         String str;
	 *
	 *         public word() {
	 *         }
	 *
	 *         public word(String str) {
	 *             this.str = str;
	 *         }
	 *
	 *         @Override
	 *         public int compare(word o1, word o2) {
	 *             if(o1.str.length() < o2.str.length()){
	 *                 return -1;
	 *             }else if(o1.str.length() > o2.str.length()){
	 *                 return 1;
	 *             }else{
	 *                 return o1.str.compareTo(o2.str);
	 *             }
	 *         }
	 *     }
	 */

	static class WordSort implements Comparable<WordSort>{
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public WordSort(String name){
			this.name = name;
		}

		@Override
		public int compareTo(WordSort o) {
			// TODO Auto-generated method stub
			if(this.name.length() < o.getName().length()) //작은 값을 앞으로
				return -1;
			else if(this.name.length() > o.getName().length()) // 큰값을 뒤로
				return 1;
			else{
				return this.name.compareTo(o.getName()); //길이가 동일할 때 string 비교
			}
		}

	}
}

