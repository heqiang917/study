package com.study.webapp.controller;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者
 * 
 * @author admin
 *
 *         2018年8月14日
 */
public class Producer implements Runnable {
	private final BlockingQueue<Integer> queue;

	public Producer(BlockingQueue q) {
		this.queue = q;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				queue.put(produce());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private int produce() {
		int a = new Random().nextInt(10000);
		System.out.println("Thread: " + Thread.currentThread().getId() + ", producer:" + a);
		return a;
	}

}
