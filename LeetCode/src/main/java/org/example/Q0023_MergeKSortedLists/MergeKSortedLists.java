package org.example.Q0023_MergeKSortedLists;

// Sequential Merge: Merge lists one by one. O(kN) time, O(1) space.
// Optimal: Use MinHeap or Divide & Conquer for O(N log k).
public class MergeKSortedLists {

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

    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;
        if (lists.length == 1)
            return lists[0];

        ListNode result = lists[0];
        for (int i = 1; i < lists.length; i++) {
            result = mergeTwoLists(result, lists[i]);
        }

        return result;
    }

    // Merge two sorted linked lists
    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }

        // Attach remaining nodes
        curr.next = (l1 != null) ? l1 : l2;

        return dummy.next;
    }

    // Helper to convert array to linked list for testing
    private static ListNode arrayToList(int[] arr) {
        if (arr == null || arr.length == 0)
            return null;
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int num : arr) {
            curr.next = new ListNode(num);
            curr = curr.next;
        }
        return dummy.next;
    }

    // Helper to print linked list
    private static void printList(ListNode head) {
        StringBuilder sb = new StringBuilder("[");
        while (head != null) {
            sb.append(head.val);
            if (head.next != null)
                sb.append(",");
            head = head.next;
        }
        sb.append("]");
        System.out.println(sb);
    }

    public static void main(String[] args) {
        MergeKSortedLists solution = new MergeKSortedLists();

        // Example 1
        ListNode[] lists1 = {
                arrayToList(new int[] { 1, 4, 5 }),
                arrayToList(new int[] { 1, 3, 4 }),
                arrayToList(new int[] { 2, 6 })
        };
        printList(solution.mergeKLists(lists1)); // [1,1,2,3,4,4,5,6]

        // Example 2
        ListNode[] lists2 = {};
        printList(solution.mergeKLists(lists2)); // []

        // Example 3
        ListNode[] lists3 = { null };
        printList(solution.mergeKLists(lists3)); // []
    }
}
