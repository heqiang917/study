package com.study.webapp.test;

public class Quackologist implements Observer {

	@Override
	public void update(QuackObservable duck) {
		System.out.println("Quackologist" + duck + "hahahha");

	}

}
