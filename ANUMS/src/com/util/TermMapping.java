package com.util;

public class TermMapping {

	public TermMapping() {
		// TODO Auto-generated constructor stub
	}

	public static String toMethod(int number) {
		String method = "";
		switch (number) {
		case 0:
			method = "Simple Round-Robin";
			break;
		case 1:
			method = "Prioritized Round-Robin by document length";
			break;
		case 2:
			method = "Generic Document Score by Title Field";
			break;
		case 3:
			method = "Generic Document Score by Summary";
			break;
		case 4:
			method = "Generic Document Score by Summary and Title";
			break;
		default:
			break;

		}
		return method;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
