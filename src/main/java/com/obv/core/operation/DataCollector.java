package com.obv.core.operation;

import java.io.IOException;
import java.sql.SQLException;

import org.xml.sax.SAXException;

import com.obv.core.mysql.entity.MySQLConnector;
import com.obv.core.mysql.util.MySQLUtil;

/**
 * @author yingdin
 * 
 */
public class DataCollector {
	public static void main(String[] args) throws Exception {
		MySQLUtil.collectStockData("002300", "2013", "01", "01", "2013", "09",
				"06", MySQLConnector.DEFAULT_CONN);
		// MySQLUtil.collectAllStockDataToDB("2013", "08", "01", "2013", "09",
		// "05");
	}
}
