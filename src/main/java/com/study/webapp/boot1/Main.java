package com.study.webapp.boot1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EventConfig.class);

		DemoPublisher demop = context.getBean(DemoPublisher.class);

		demop.publish("hekcwcnkdwcnoew");
		context.close();
	}

}
