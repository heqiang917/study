package com.study.webapp.extend;

public class Brass extends Instrument {
	public void play(Note n) {
		System.out.println("Brass.play():" + n);
	}

	@Override
	public void adjust() {
		// TODO Auto-generated method stub

	}
}
