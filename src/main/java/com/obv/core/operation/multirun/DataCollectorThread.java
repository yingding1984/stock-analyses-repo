/**
 * 
 */
package com.obv.core.operation.multirun;

import com.obv.core.mysql.util.MySQLUtil;

/**
 * @author yingding
 * 
 */
public class DataCollectorThread extends Thread {

	private String fromYear;
	private String fromMonth;
	private String fromDate;
	private String toYear;
	private String toMonth;
	private String toDate;
	private String stockList;

	public DataCollectorThread(String _fromYear, String _fromMonth,
			String _fromDate, String _toYear, String _toMonth, String _toDate,
			String _stockList) {
		this.fromYear = _fromYear;
		this.fromMonth = _fromMonth;
		this.fromDate = _fromDate;
		this.toYear = _toYear;
		this.toMonth = _toMonth;
		this.toDate = _toDate;
		this.stockList = _stockList;
	}

	public void run() {
		try {
			MySQLUtil.collectStockDataInParallel(fromYear, fromMonth, fromDate,
					toYear, toMonth, toDate, stockList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
