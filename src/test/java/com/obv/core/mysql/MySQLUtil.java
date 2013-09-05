package com.obv.core.mysql;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.obv.core.httpunit.test.HttpUnitRoom;

public class MySQLUtil {

	static ResultSet rs;
	private final static String CLEAN_DB = "drop table records";
	private final static String GET_ALL_RECORDS = "drop table records";
	private final static String CREATE_TABLE = "create  table  records(id  varchar(20),Date  varchar(20),open float,close float,volumn int,primary  key(id,Date))";

	public static void cleanDB() {
		MySQLConnector.execute(CLEAN_DB);
	}

	public static void initialDB() {
		MySQLConnector.execute(CREATE_TABLE);
	}

	public static ArrayList<String> getAllRecords() throws SQLException {
		ArrayList<String> records = new ArrayList<String>();
		rs = MySQLConnector.getResults(GET_ALL_RECORDS);
		while (rs.next()) {
			records.add(rs.toString());
		}
		return records;
	}

	public static void restoreStockDataToDB(String stockID, String year,
			String month, String date) throws IOException, SAXException {
		String tradeDate, openPrice, closePrice, volumn;
		String restoreQuery;
		ArrayList<String> priceVolumeResults = HttpUnitRoom
				.getStockTradeDetailFromDate(stockID, year, month, date);
		for (int i = 0; i < priceVolumeResults.size(); i++) {
			String recordLine = priceVolumeResults.get(i);
			String[] recordColumns = recordLine.split("\\|");
			tradeDate = recordColumns[1];
			openPrice = recordColumns[2];
			closePrice = recordColumns[5];
			volumn = recordColumns[6];
			restoreQuery = "insert into records(id,Date,open,close,volumn) values ('"
					+ stockID
					+ "','"
					+ tradeDate
					+ "',"
					+ openPrice
					+ ","
					+ closePrice + "," + volumn + ")";
			MySQLConnector.execute(restoreQuery);
		}

	}
	
	public static void restoreAllStockDataToDB(String year,
			String month, String date){
		String stockID;
		//TO-DO get stock id list and execute storage
	}

	public static void main(String[] args) throws IOException, SAXException {
		restoreStockDataToDB("002300","2013","08","29");
	}
}
