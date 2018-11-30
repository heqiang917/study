package com.study.webapp.test;

/**
 * 观察者模式
 * 
 * @author wangjing
 *
 */
public interface QuackObservable {

	public void registerObserver(Observer observer);

	public void notifyObserver();

}
