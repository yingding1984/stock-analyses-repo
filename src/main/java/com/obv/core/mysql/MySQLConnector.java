package com.obv.core.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class MySQLConnector {

	static Connection con = null;

	private MySQLConnector(String rootPasswd) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/stock",
					"root", rootPasswd);

			if (!con.isClosed())
				System.out.println("Successfully connected to MySQL server...");

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}

	public static MySQLConnector DEFAULT_CONN = new MySQLConnector("root");

	public void execute(String _sqlStmt) {

		try {

			Statement st = con.createStatement();
			st.execute(_sqlStmt);
			// ResultSet rs = st.executeQuery(_sqlStmt);
			/*
			 * while (rs.next()) { System.out.println(rs.getString("id")); }
			 */

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}
	
	public void closeConn() throws SQLException{
		con.close();
		
	}

	public ResultSet getResults(String _sqlStmt) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(_sqlStmt);
			con.close();

			return rs;

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
		return null;
	}
}
