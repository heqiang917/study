package com.study.webapp.controller;

import java.util.concurrent.BlockingQueue;

/**
 * 消费者
 * 
 * @author admin
 *
 *         2018年8月14日
 */
public class Consumer implements Runnable {
	private final BlockingQueue<Integer> queue;

	public Consumer(BlockingQueue q) {
		this.queue = q;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
				consumer(queue.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void consumer(Integer n) {
		System.out.println("Thread: " + Thread.currentThread().getId() + ", Consumer:" + n);
	}

}
