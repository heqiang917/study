package com.study.webapp.extend;

public class Parcel {

	private class PContents implements Contents {

		@Override
		public int value() {
			// TODO Auto-generated method stub
			return 11;
		}

	}

	protected class PDestination implements Destination {
		private String lable;

		public PDestination(String la) {
			lable = la;
		}

		@Override
		public String readlable() {
			// TODO Auto-generated method stub
			return lable;
		}

	}

	public Destination destination(String s) {
		return new PDestination(s);
	}

	public Contents contents() {
		return new PContents();
	}

}
