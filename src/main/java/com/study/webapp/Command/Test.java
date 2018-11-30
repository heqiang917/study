package com.study.webapp.Command;

public class Test {
	public static void main(String[] args) {

		Receiver receiver = new Receiver();
		Command command = new ConcreteCommand(receiver);
		Invoker invoker = new Invoker();
		invoker.setCommand(command);
		invoker.action();
	}
}
