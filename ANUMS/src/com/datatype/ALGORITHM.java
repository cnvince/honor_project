package com.datatype;

public class ALGORITHM {
	public static final int SRR = 0;
	public static final int PRR = 1;
	public static final int GDS_TS = 2;
	public static final int GDS_SS = 3;
	public static final int GDS_TSS = 4;
	public static final int GDS_DTSS = 5;
	public static final int LMS=6;
	public static final int JUDGE=7;
	public static final int LOCAL=8;
	public static final int SPRR=9;
	public static final int MW=10;
	public static final int OPRR=11;
	public static final int OGDS=12;
	public static final int SGDS=13;
	public static String getAlgorithm(int var)
	{
		switch(var)
		{
		case SRR:
			return "SRR";
		case PRR:
			return "PRR";
		case GDS_TS:
			return "GDS_TS";
		case GDS_SS:
			return "GDS_SS";
		case GDS_TSS:
			return "GDS_TSS";
		case GDS_DTSS:
			return "GDS_DTSS";
		case LMS:
			return "LMS";
		case JUDGE:
			return "JUDGE";
		case LOCAL:
			return "LRI";//local re-index
		case SPRR:
			return "SPRR";
		case MW:
			return "MW";//mutiple weight
		case OPRR:
			return "OPRR";
		case OGDS:
			return "OGDS";
		case SGDS:
			return "SGDS";
		}
		return null;
	}

}
