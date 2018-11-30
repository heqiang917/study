package com.study.webapp.leetcode;

/**
 * 两链表数相加
 * 
 * @author admin
 *
 *         2018年10月19日
 */
public class Leet_01 {

	public static void main(String[] args) {
		int[] put = { 2, 3, 4 };
		int[] out = { 5, 6, 7 };
		ListNode l1 = buidListNode(put);
		ListNode l2 = buidListNode(out);
		System.out.println(add(l1, l2));
	}

	public static ListNode add(ListNode l1, ListNode l2) {
		ListNode dummyHead = new ListNode(0);
		ListNode p = l1;
		ListNode q = l2;
		ListNode curr = dummyHead;
		int carry = 0;
		while (p != null || q != null) {
			int x = (p != null) ? p.val : 0;
			int y = (q != null) ? q.val : 0;
			int sum = x + y + carry;
			carry = sum / 10;
			curr.next = new ListNode(sum % 10);
			curr = curr.next;
			if (p != null)
				p = p.next;
			if (q != null)
				q = q.next;
		}
		if (carry > 0) {
			curr.next = new ListNode(carry);
		}
		return dummyHead.next;
	}

	public static ListNode buidListNode(int[] put) {
		ListNode first = null;
		ListNode last = null;
		ListNode newNode;
		if (put.length > 0) {
			for (int i = 0; i < put.length; i++) {
				// newNode作为一个过渡的对象，每一次把值暂时存放在newNode
				newNode = new ListNode(put[i]);
				newNode.next = null;
				if (first == null) {
					// 第一次存放值，把first和last指向相同的对象
					first = newNode;
					last = newNode;
				} else {
					// 后面每一次都把新的值存放在last中的next中，并吧last设为新值，
					last.next = newNode;
					last = newNode;
				}
			}
		}
		// 不懂的是，为什么first的对象的next一直在增加，表面看first与last无关联
		return first;
	}

}
