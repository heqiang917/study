package com.study.webapp.test;

import java.util.ArrayList;
import java.util.Iterator;

public class Flock implements Quackable {
	Observable observable;
	ArrayList quackers = new ArrayList();

	public void add(Quackable quacker) {
		quackers.add(quacker);
	}

	public Flock() {
		observable = new Observable(this);
	}

	@Override
	public void quack() {
		Iterator iterator = quackers.iterator();
		while (iterator.hasNext()) {
			Quackable quacker = (Quackable) iterator.next();
			quacker.quack();
		}

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
