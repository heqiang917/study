package com.study.webapp.Command;

/**
 * 客户端调用者
 * 
 * @author admin
 *
 *         2018年8月23日
 */
public class Invoker {

	private Command command;

	public void setCommand(Command command) {
		this.command = command;
	}

	public void action() {
		this.command.execute();
	}

}
