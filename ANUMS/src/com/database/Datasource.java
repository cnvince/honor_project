package com.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Datasource {

	public Datasource() {
		// TODO Auto-generated constructor stub
	}
//	rest all tables
	public void reset()
	{
		Connector.Connect();
		Connection conn=null;
		conn=Connector.getConnection();
		try {
			Statement stmt=conn.createStatement();
//			create tables if not exist
			String sql_QUERIES="CREATE TABLE IF NOT EXISTS QUERIES(Q_TERM char(100) NOT NULL, ASSESSED TINYINT, PRIMARY KEY (Q_TERM));";
			String sql_QueryDocuemnt="CREATE TABLE IF NOT EXISTS QUERY_DOCUMENT(Q_TERM char(100) NOT NULL, TITLE char(100) NOT NULL,DOCID char(225) NOT NULL, SOURCE char(20),PRIMARY KEY (Q_TERM,DOCID));";
			String sql_Result="CREATE TABLE IF NOT EXISTS RESULT(Q_TERM char(100),DOCID char(225), USERID char(20), RDATE DATE, SCORE INT, ASSESSED TINYINT,PRIMARY KEY (Q_TERM, DOCID, USERID));";
			stmt.executeUpdate(sql_QUERIES);
			stmt.executeUpdate(sql_QueryDocuemnt);
			stmt.executeUpdate(sql_Result);
			String sql_Del_Q="DELETE FROM QUERIES;";
			String sql_Del_QD="DELETE FROM QUERY_DOCUMENT;";
			String sql_Del_Res="DELETE FROM RESULT;";
//			delete data
			stmt.executeUpdate(sql_Del_Q);
			stmt.executeUpdate(sql_Del_QD);
			stmt.executeUpdate(sql_Del_Res);
			//alaways remember to close the connection
			Connector.DisConnect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Datasource data=new Datasource();
		data.reset();
	}

}
