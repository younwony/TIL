/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @작성자 wony
 * @작성일 2019. 12. 26.
 * @사용처
 * 
 */
public class a10814_나이순정렬 {

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
			String[] inputData;
			ArrayList<Person> arrayList = new ArrayList<Person>();
			for(int i = 0; i < testCount; i++){
				inputData = bufferedReader.readLine().split(" ");
				arrayList.add(new Person(Integer.parseInt(inputData[0]), inputData[1], i));
			}
			Collections.sort(arrayList);
			
			for(Person outData : arrayList)
				System.out.println(outData.getAge() + " " + outData.getName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null){bufferedReader.close();}
				if(bufferedWriter != null){bufferedWriter.flush(); bufferedWriter.close();}
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	static class Person implements Comparable<Person>{
		int age;
		int count;
		String name;

		public Person(int age, String name, int count) {
			// TODO Auto-generated constructor stub
			this.age = age;
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public int compareTo(Person o) {
			// TODO Auto-generated method stub
			if(this.age < o.getAge()){
				return -1;
			}else if(this.age > o.getAge()){
				return 1;
			}else{
				return Integer.compare(this.count, o.getCount());
			}
		}

	}
}

