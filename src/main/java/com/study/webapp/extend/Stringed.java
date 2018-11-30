package com.study.webapp.extend;

public class Stringed extends Instrument {

	public void play(Note n) {
		System.out.println("Stringed.play():" + n);
	}

	@Override
	public void adjust() {
		// TODO Auto-generated method stub

	}

}
