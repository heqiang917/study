package com.study.webapp.extend;

public class TestParcel {

	public static void main(String[] args) {
		Parcel p = new Parcel();

		Contents c = p.contents();

		Destination d = p.destination("haha");
	}

}
