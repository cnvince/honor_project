package com.util;

public class ArrayUtility {

	public ArrayUtility() {
		// TODO Auto-generated constructor stub
	}
	
	public static double[] reverseArray(double[] iG)
	{
		Double temp;
		int SIZE=iG.length;
		for (int i = 0; i < SIZE/2; i++)
		  {
		     temp = iG[i];
		     iG[i] = iG[SIZE-1 - i];
		     iG[SIZE-1 - i] = temp;
		  }
		return iG;
	}
}
