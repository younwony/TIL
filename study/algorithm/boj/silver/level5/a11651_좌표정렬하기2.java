/**
 * @작성자 wony
 * @작성일 2019. 11. 15.
 * @사용처
 *
 */
package _1_Silver.Level_5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * @작성자 wony
 * @작성일 2019. 11. 15.
 * @사용처 좌표정렬2
 * 
 */
public class a11651_좌표정렬하기2 {

	/**
	 * @작성자 wony
	 * @작성일 2019. 11. 15.
	 * @사용처
	 * @Todo
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
Scanner scanner = new Scanner(System.in);
		
		int testCount = scanner.nextInt();
		
		List<MapXY> arrayList = new ArrayList<MapXY>();
		
		for(int i = 0; i < testCount; i++){
			arrayList.add(new MapXY(scanner.nextInt(), scanner.nextInt()));
		}
		
		scanner.close();

		Comparator<MapXY> comparator = new Comparator<MapXY>() {
			
			@Override
			public int compare(MapXY o1, MapXY o2) {
				// TODO Auto-generated method stub
				if(o1.getY() == o2.getY()){
					return Integer.compare(o1.getX(), o2.getX());
				}else 
					return Integer.compare(o1.getY(), o2.getY());
			}
		};
		
		
		arrayList.sort(comparator);
		
		for(MapXY data : arrayList)
			System.out.println(data.getX() + " " + data.getY());
	}

	/**
	 * 2020.07.26 Again Study
	 *
	 * public static void main(String[] args) {
	 *         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	 *
	 *         try {
	 *             int n = Integer.valueOf(bufferedReader.readLine());
	 *
	 *             ArrayList<point> pointList = new ArrayList<>();
	 *
	 *             String[] str;
	 *             for(int i = 0; i < n; i++){
	 *                 str = bufferedReader.readLine().split(" ");
	 *                 pointList.add(new point(Integer.valueOf(str[0]), Integer.valueOf(str[1])));
	 *             }
	 *
	 *             Collections.sort(pointList, new point());
	 *
	 *             StringBuilder result = new StringBuilder();
	 *
	 *             for(point point : pointList){
	 *                 result.append(point.x + " " + point.y + "\n");
	 *             }
	 *
	 *             System.out.println(result.toString());
	 *
	 *         }catch (Exception e){
	 *             e.printStackTrace();
	 *         }finally {
	 *             try {
	 *                 if(bufferedReader != null){
	 *                     bufferedReader.close();
	 *                 }
	 *             }catch (Exception e2){
	 *                 e2.printStackTrace();
	 *             }
	 *         }
	 *
	 *
	 *     }
	 *
	 *     public static class point implements Comparator<point> {
	 *         int x;
	 *         int y;
	 *
	 *         public point() {
	 *         }
	 *
	 *         public point(int x, int y) {
	 *             this.x = x;
	 *             this.y = y;
	 *         }
	 *
	 *         @Override
	 *         public int compare(point o1, point o2) {
	 *             if(o1.y < o2.y){
	 *                 return -1;
	 *             }else if(o1.y > o2.y){
	 *                 return 1;
	 *             }else{
	 *                 return o1.x - o2.x;
	 *             }
	 *         }
	 *     }
	 */
	static class MapXY{

		int x;
		int y;

		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}

		public MapXY(int x, int y){
			this.x = x;
			this.y = y;
		}
	}

}
