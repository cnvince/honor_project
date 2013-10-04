package com.util;

import java.util.Comparator;

import com.resultpool.Server;


public class ServerScoreComparable implements Comparator<Server> {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compare(Server o1, Server o2) {
		return (o1.getScore()>o2.getScore() ? -1 : (o1.getScore()==o2.getScore() ? 0 : 1));
	}

}
