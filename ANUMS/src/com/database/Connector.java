package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
	private static Connection connection=null; 
	public Connector() {
		// TODO Auto-generated constructor stub
	}
	public static void Connect()
	{
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
//			remote connection
			connection = DriverManager
					.getConnection("jdbc:mysql://192.163.237.178:3306/djudgeco_docs?user=djudgeco_root&password=881106vl");
//			local connection
//				connection = DriverManager
//						.getConnection("jdbc:mysql://localhost:3306/djudgeco_docs?user=djudgeco_root&password=881106vl");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public static void DisConnect()
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnection()
	{
		return connection;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
