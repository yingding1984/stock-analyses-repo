package com.obv.core.tools;

import java.io.IOException;
import java.sql.SQLException;

import org.xml.sax.SAXException;

import com.obv.core.util.MySQLUtil;

public class DataCollector {
	public static void main(String[] args) throws IOException, SAXException, SQLException {
		MySQLUtil.collectStockDataToDB("600319", "2013", "07", "29","2013","09","05");
//      MySQLUtil.restoreAllStockDataToDB("2013", "08", "01","2013","09","05");
	}
}	