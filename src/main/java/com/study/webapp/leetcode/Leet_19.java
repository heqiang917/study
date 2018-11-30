package com.study.webapp.leetcode;

/**
 * 给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。
 * https://blog.csdn.net/wzhworld/article/details/76888521
 * 
 * @author admin
 *
 *         2018年11月30日
 */
public class Leet_19 {

	public static void main(String[] args) {
		ListNode l = new ListNode(1);
		ListNode a = new ListNode(2);
		ListNode b = new ListNode(3);
		ListNode c = new ListNode(4);
		ListNode d = new ListNode(5);
		ListNode e = new ListNode(6);
		d.next = e;
		c.next = d;
		b.next = c;
		a.next = b;
		l.next = a;
		ListNode m = remove(l, 2);
		ListNode p = m;
		System.out.print(m.val);
		while (p.next != null) {
			p = p.next;
			System.out.print(p.val);
		}
	}

	public static ListNode remove(ListNode head, int n) {
		ListNode p = head;
		ListNode q = head;
		for (int i = 0; i < n; i++) {
			p = p.next;
		}
		if (p == null) {
			head = head.next;
			return head;
		}
		while (p.next != null) {
			p = p.next;
			q = q.next;
		}
		q.next = q.next.next;
		return head;
	}

}
