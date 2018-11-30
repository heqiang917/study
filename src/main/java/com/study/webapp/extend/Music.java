package com.study.webapp.extend;

public class Music {
	public static void tune(Wind i) {
		i.play(Note.MIDDLE_C);
	}

	public static void tune(Stringed i) {
		i.play(Note.MIDDLE_C);
	}

	public static void tune(Brass i) {
		i.play(Note.MIDDLE_C);
	}

	public static void main(String[] args) {
		Wind flute = new Wind();
		tune(flute);
	}

}
