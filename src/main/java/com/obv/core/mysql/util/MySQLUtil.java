package com.obv.core.mysql.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.obv.core.http.util.HttpUnitUtil;
import com.obv.core.mysql.entity.MySQLConnector;
import com.obv.core.mysql.entity.Quarter;
import com.obv.core.util.FileUtil;

/**
 * @author yingdin
 * 
 */
public class MySQLUtil {

	static ResultSet rs;
	private final static String STOCK_LIST_FILE_PATH = "src/main/resources/list";
	private final static String CLEAN_DB = "truncate table records";
	private final static String GET_ALL_RECORDS = "drop table records";
	private final static String CREATE_TABLE = "create  table  records(id  varchar(20),Date  varchar(20),open float,close float,volumn int,primary  key(id,Date))";

	public static void cleanDB() {
		MySQLConnector.DEFAULT_CONN.execute(CLEAN_DB);
	}

	public static void initialDB() {
		MySQLConnector.DEFAULT_CONN.execute(CREATE_TABLE);
	}

	public static ArrayList<String> getAllRecords() throws SQLException {
		ArrayList<String> records = new ArrayList<String>();
		rs = MySQLConnector.DEFAULT_CONN.getResults(GET_ALL_RECORDS);
		while (rs.next()) {
			records.add(rs.toString());
		}
		return records;
	}

	public static void collectStockData(String stockID, String from_year,
			String from_month, String from_date, String to_year,
			String to_month, String to_date, MySQLConnector sql_conn)
			throws IOException, SAXException, SQLException {
		String tradeDate, openPrice, closePrice, volumn;
		String storeQuery;
		ArrayList<String> priceVolumeResults = HttpUnitUtil
				.getStockTradeDetailFromDate(stockID, from_year, from_month,
						from_date, to_year, to_month, to_date);
		for (int i = 0; i < priceVolumeResults.size(); i++) {
			String recordLine = priceVolumeResults.get(i);
			String[] recordColumns = recordLine.split("\\|");
			if (recordColumns.length >= 6) {
				tradeDate = recordColumns[1];
				openPrice = recordColumns[2];
				closePrice = recordColumns[5];
				volumn = recordColumns[6];
				System.out.println("Storing trade record :" + stockID + ":"
						+ tradeDate + " " + openPrice + " " + closePrice + " "
						+ volumn);
				storeQuery = "insert into records(id,Date,open,close,volumn) values ('"
						+ stockID
						+ "','"
						+ tradeDate
						+ "',"
						+ openPrice
						+ ","
						+ closePrice + "," + volumn + ")";
				sql_conn.execute(storeQuery);
			}
		}

	}

	public static void collectAllStockData(String from_year, String from_month,
			String from_date, String to_year, String to_month, String to_date)
			throws Exception {
		String stockID;
		String stockList = FileUtil.loadFile(STOCK_LIST_FILE_PATH);

		MySQLConnector sqlconn = new MySQLConnector("root");

		Pattern pattern = Pattern.compile("\\d{6}");
		Matcher matcher = pattern.matcher(stockList);
		while (matcher.find()) {
			stockID = matcher.group();
			System.out.println("storing " + stockID);
			collectStockData(stockID, from_year, from_month, from_date,
					to_year, to_month, to_date, sqlconn);
		}
		sqlconn.closeConn();
	}

	public static void collectStockDataInParallel(String from_year,
			String from_month, String from_date, String to_year,
			String to_month, String to_date, String stockList) throws Exception {
		String stockID;

		MySQLConnector sqlconn = new MySQLConnector("root");

		Pattern pattern = Pattern.compile("\\d{6}");
		Matcher matcher = pattern.matcher(stockList);
		while (matcher.find()) {
			stockID = matcher.group();
			System.out.println("storing " + stockID);
			collectStockData(stockID, from_year, from_month, from_date,
					to_year, to_month, to_date, sqlconn);
		}
		sqlconn.closeConn();
	}

	public static void storeCaiWuZhaiYao(String stockID, Quarter q,
			MySQLConnector sql_conn) throws Exception {
		String storeQuery;
		ArrayList<Double> caiWuZhaiYao_Data = HttpUnitUtil.getCaiWuZhaiYao(
				stockID, q);

		if (caiWuZhaiYao_Data.size() >= 5) {
			System.out.println(stockID + "....stored");
			storeQuery = "insert into caiwuzhaiyao(id,quarter,meigujingzichan,meigushouyi,meiguxianjinhanliang,meiguzibengongjijin,liudongzichanheji,zichanzongji,changqifuzhaiheji,zhuyingyewushouru,caiwufeiyong,jinglirun) values ('"
					+ stockID
					+ "','"
					+ q.toString().substring(1, q.toString().length())
					+ "','"
					+ caiWuZhaiYao_Data.get(0)
					+ "','"
					+ caiWuZhaiYao_Data.get(1)
					+ "','"
					+ caiWuZhaiYao_Data.get(2)
					+ "',"
					+ caiWuZhaiYao_Data.get(3)
					+ ","
					+ caiWuZhaiYao_Data.get(4)
					+ ","
					+ caiWuZhaiYao_Data.get(5)
					+ ","
					+ caiWuZhaiYao_Data.get(6)
					+ ","
					+ caiWuZhaiYao_Data.get(7)
					+ ","
					+ caiWuZhaiYao_Data.get(8)
					+ ","
					+ caiWuZhaiYao_Data.get(9) + ")";
			sql_conn.execute(storeQuery);
		}else{
			FileUtil.logFailStock(stockID);
		}
	}

	public static void storeAllCaiWuZhaiYao(Quarter Q) throws Exception {
		String stockID;
		String stockList = FileUtil.loadFile(STOCK_LIST_FILE_PATH);

		MySQLConnector sqlconn = new MySQLConnector("root");

		Pattern pattern = Pattern.compile("\\d{6}");
		Matcher matcher = pattern.matcher(stockList);
		while (matcher.find()) {
			stockID = matcher.group();
			System.out.println("storing " + stockID);
			storeCaiWuZhaiYao(stockID, Q, sqlconn);
		}
		sqlconn.closeConn();
	}

	public static void storeCaiWuZhaiYaoInParallel(String stockList, Quarter Q)
			throws Exception {
		String stockID;

		MySQLConnector sqlconn = new MySQLConnector("root");

		Pattern pattern = Pattern.compile("\\d{6}");
		Matcher matcher = pattern.matcher(stockList);
		while (matcher.find()) {
			stockID = matcher.group();
			System.out.println("storing " + stockID);
			storeCaiWuZhaiYao(stockID, Q, sqlconn);
		}
		sqlconn.closeConn();
	}

	public static void main(String[] args) throws Exception {
		MySQLConnector sqlconn = new MySQLConnector("root");
		storeCaiWuZhaiYao("600025", Quarter._2013A, sqlconn);
		// storeAllCaiWuZhaiYao( Quarter._2013B);
	}

}
