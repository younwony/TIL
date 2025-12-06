/**
 * @작성자 wony
 * @작성일 2019. 12. 17.
 * @사용처
 *
 */
package _0_Bronze.Level_5;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @작성자 wony
 * @작성일 2019. 12. 17.
 * @사용처
 * 
 */
public class a16170_오늘의날짜는 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 12. 17.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC+0"));
		System.out.println(date.get(Calendar.YEAR));
		System.out.println(date.get(Calendar.MONTH)+1);
		System.out.println(date.get(Calendar.DATE));
	}

}
