package com.obv.core.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class MySQLConnector {
	public static void execute(String _sqlStmt) {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/stock",
					"root", "mint");

			if (!con.isClosed())
				System.out
						.println("Successfully connected to MySQL server using TCP/IP...");

			Statement st = con.createStatement();
			st.execute(_sqlStmt);
			// ResultSet rs = st.executeQuery(_sqlStmt);
			con.close();
			/*
			 * while (rs.next()) { System.out.println(rs.getString("id")); }
			 */

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		}
	}

	public static ResultSet getResults(String _sqlStmt) {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/stock",
					"root", "root");

			if (!con.isClosed())
				System.out
						.println("Successfully connected to MySQL server using TCP/IP...");

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
