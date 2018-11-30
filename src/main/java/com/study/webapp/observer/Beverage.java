package com.study.webapp.observer;

/**
 * 观察者模式
 * 
 * @author
 *
 */
public abstract class Beverage {

	String description = "Unknown Beverage";

	public String getDescription() {
		return description;
	}

	public abstract double cost();

}
