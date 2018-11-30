package com.study.webapp;

import redis.clients.jedis.Jedis;

public class Test {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		String value = jedis.get("hello");
		System.out.println(value);
	}

}
