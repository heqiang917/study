package com.study.webapp.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 写一个生产者-消费者队列 可以通过阻塞队列实现,也可以通过wait-notify来实现. 使用阻塞队列来实现
 * 
 * @author admin
 *
 *         2018年8月14日
 */
public class QueueTest {

	public static void main(String[] args) {
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(100);
		Producer p = new Producer(queue);
		Consumer c1 = new Consumer(queue);
		Consumer c2 = new Consumer(queue);

		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
		System.out.println("");
	}

}
