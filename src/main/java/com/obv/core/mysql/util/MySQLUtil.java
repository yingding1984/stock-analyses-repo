package com.obv.core.mysql.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.obv.core.http.util.HttpUnitUtil;
import com.obv.core.mysql.entity.MySQLConnector;
import com.obv.core.util.FileLoader;

/**
 * @author yingdin
 * 
 */
public class MySQLUtil {

    static ResultSet rs;
    private final static String STOCK_LIST_FILE_PATH = "src/main/resources/stock-list";
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
        while(rs.next()) {
            records.add(rs.toString());
        }
        return records;
    }

    public static void collectStockDataToDB(String stockID, String from_year, String from_month, String from_date, String to_year, String to_month, String to_date) throws IOException,
            SAXException, SQLException {
        String tradeDate, openPrice, closePrice, volumn;
        String restoreQuery;
        ArrayList<String> priceVolumeResults = HttpUnitUtil.getStockTradeDetailFromDate(stockID, from_year, from_month, from_date,to_year,to_month,to_date);
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
                MySQLConnector.DEFAULT_CONN.execute(restoreQuery);
            }
        }
        MySQLConnector.DEFAULT_CONN.closeConn();

    }

    public static void collectAllStockDataToDB(String from_year, String from_month, String from_date, String to_year, String to_month, String to_date) throws Exception {
        String stockID;
        String stockList = FileLoader.loadFile(STOCK_LIST_FILE_PATH);
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(stockList);
        while(matcher.find()) {
            stockID = matcher.group();
            System.out.println("storing " + stockID);
            collectStockDataToDB(stockID, from_year, from_month, from_date,to_year,to_month,to_date);
        }
    }

}