package com.study.webapp.test;

/**
 * 装饰者模式
 * 
 * @author wangjing
 *
 */
public class QuackCounter implements Quackable {
	Quackable duck;
	static int numberOfQuacks;
	Observable observable;

	public QuackCounter(Quackable duck) {
		this.duck = duck;
	}

	public QuackCounter() {
		observable = new Observable(this);
	}

	@Override
	public void quack() {
		duck.quack();
		numberOfQuacks++;
	}

	public static int getQuacks() {
		return numberOfQuacks;
	}

	@Override
	public void registerObserver(Observer observer) {
		observable.registerObserver(observer);

	}

	@Override
	public void notifyObserver() {
		observable.notifyObserver();

	}
}
