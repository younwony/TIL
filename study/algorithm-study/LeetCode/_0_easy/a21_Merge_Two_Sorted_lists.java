/**
 * @작성자 wony
 * @작성일 2020. 2. 20.
 * @사용처
 */
package _0_easy;

public class a21_Merge_Two_Sorted_lists {

	/**
	 * Input: list1 = [1,2,4], list2 = [1,3,4]
	 * Output: [1,1,2,3,4,4]
	 * Example 2:
	 *
	 * Input: list1 = [], list2 = []
	 * Output: []
	 * Example 3:
	 *
	 * Input: list1 = [], list2 = [0]
	 * Output: [0]
	 *
	 *
	 * Constraints:
	 *
	 * The number of nodes in both lists is in the range [0, 50].
	 * -100 <= Node.val <= 100
	 * Both list1 and list2 are sorted in non-decreasing order.
	 * @param args
	 */

    public static void main(String[] args) {
		// 첫 번째 리스트: 1 -> 2 -> 4
		ListNode listNode = new ListNode(4);
		ListNode listNode2 = new ListNode(2, listNode);
		ListNode listNode3 = new ListNode(1, listNode2);

		// 두 번째 리스트: 1 -> 3 -> 4
		ListNode list2Node = new ListNode(4);
		ListNode list2Node2 = new ListNode(3, list2Node);
		ListNode list2Node3 = new ListNode(1, list2Node2);

		// 병합된 결과 리스트
		ListNode mergedList = mergeTwoLists(listNode3, list2Node3);
	}

    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		ListNode dummy = new ListNode(-1); // 결과 리스트의 시작점
		ListNode current = dummy; // 결과 리스트를 따라가는 포인터

		while (list1 != null && list2 != null) {
			if (list1.val < list2.val) {
				current.next = list1; // list1이 더 작을 때
				list1 = list1.next; // list1 포인터 이동
			} else {
				current.next = list2; // list2가 더 작을 때
				list2 = list2.next; // list2 포인터 이동
			}
			current = current.next; // 결과 리스트 포인터 이동
		}

		// 남아 있는 노드 연결
		if (list1 != null) current.next = list1;
		if (list2 != null) current.next = list2;

		return dummy.next; // 더미 노드의 다음 노드가 결과 리스트
	}

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
