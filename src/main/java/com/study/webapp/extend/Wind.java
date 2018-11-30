package com.study.webapp.extend;

public class Wind extends Instrument {
	public void play(Note n) {
		System.out.println("Wind.play():" + n);
	}

	@Override
	public void adjust() {
		// TODO Auto-generated method stub

	}
}
