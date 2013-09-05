package com.obv.core.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.obv.core.mysql.MySQLConnector;

public class MySQLUtil {

    static ResultSet rs;
    private final static String STOCK_LIST_FILE_PATH = "src/main/resources/stock-list";
    private final static String CLEAN_DB = "truncate table records";
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
        while(rs.next()) {
            records.add(rs.toString());
        }
        return records;
    }

    public static void restoreStockDataToDB(String stockID, String year, String month, String date) throws IOException,
            SAXException {
        String tradeDate, openPrice, closePrice, volumn;
        String restoreQuery;
        ArrayList<String> priceVolumeResults = HttpUnitUtil.getStockTradeDetailFromDate(stockID, year, month, date);
        for(int i = 0; i < priceVolumeResults.size(); i++) {
            String recordLine = priceVolumeResults.get(i);
            String[] recordColumns = recordLine.split("\\|");
            if(recordColumns.length >= 6) {
                tradeDate = recordColumns[1];
                openPrice = recordColumns[2];
                closePrice = recordColumns[5];
                volumn = recordColumns[6];
                System.out.println("Storing trade record :" + stockID + ":" + tradeDate + " " + openPrice + " "
                        + closePrice + " " + volumn);
                restoreQuery = "insert into records(id,Date,open,close,volumn) values ('" + stockID + "','" + tradeDate
                        + "'," + openPrice + "," + closePrice + "," + volumn + ")";
                MySQLConnector.execute(restoreQuery);
            }
        }

    }

    public static void restoreAllStockDataToDB(String year, String month, String date) throws Exception {
        String stockID;
        String stockList = FileLoader.loadFile(STOCK_LIST_FILE_PATH);
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(stockList);
        while(matcher.find()) {
            stockID = matcher.group();
            System.out.println("storing " + stockID);
            restoreStockDataToDB(stockID, year, month, date);
//            Thread.sleep(5000);
        }
        // TO-DO get stock id list and execute storage
    }

    public static void main(String[] args) throws Exception {
        // cleanDB();
         restoreStockDataToDB("600318", "2013", "07", "29");
//        restoreAllStockDataToDB("2013", "08", "01");
    }
}
