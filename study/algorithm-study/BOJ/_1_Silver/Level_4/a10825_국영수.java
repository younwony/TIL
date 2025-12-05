/**
 * @작성자 wony
 * @작성일 2020. 2. 7.
 * @사용처
 * @Todo
 */

package _1_Silver.Level_4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2020. 2. 7.
 * @사용처
 * @Todo
 */

public class a10825_국영수 {

	/**
	 * @작성자 wony
	 * @작성일 2020. 2. 7.
	 * @사용처
	 * @param args
	 * @Todo
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		
		int testCase = scanner.nextInt();
		
		ArrayList<Student> arrayList = new ArrayList<Student>();
		
		for(int i = 0; i < testCase; i++) {
			arrayList.add(new Student(scanner.next(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
		}
		
		scanner.close();
		
		Collections.sort(arrayList, new Student());
		
		for(Student student : arrayList) {
			System.out.println(student.getName());
		}

	}

	static class Student implements Comparator<Student>{

		private String Name;
		private int korean;
		private int english;
		private int math;


		public Student() {
			super();
		}

		public Student(String name, int korean, int english, int math) {
			super();
			Name = name;
			this.korean = korean;
			this.english = english;
			this.math = math;
		}

		@Override
		public int compare(Student o1, Student o2) {
			// TODO Auto-generated method stub
			if(o1.getKorean() == o2.getKorean()) {
				if(o1.getEnglish() == o2.getEnglish()) {
					if(o1.getMath() == o2.getMath()) {
						return o1.getName().compareTo(o2.getName());
					}else {
						return o2.getMath() - o1.getMath();
					}
				}else{
					return o1.getEnglish() - o2.getEnglish();
				}
			}else {
				return o2.getKorean() - o1.getKorean();
			}
		}

		public String getName() {
			return Name;
		}




		public void setName(String name) {
			Name = name;
		}




		public int getKorean() {
			return korean;
		}




		public void setKorean(int korean) {
			this.korean = korean;
		}




		public int getEnglish() {
			return english;
		}




		public void setEnglish(int english) {
			this.english = english;
		}




		public int getMath() {
			return math;
		}




		public void setMath(int math) {
			this.math = math;
		}
	}
}

