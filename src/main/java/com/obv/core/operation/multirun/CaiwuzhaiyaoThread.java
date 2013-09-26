/**
 * 
 */
package com.obv.core.operation.multirun;

import com.obv.core.mysql.util.MySQLUtil;

/**
 * @author yingding
 * 
 */
public class CaiwuzhaiyaoThread extends Thread {

	private String stockList;

	public CaiwuzhaiyaoThread(String _stockList) {

		this.stockList = _stockList;
	}

	public void run() {
		try {
			MySQLUtil.storeCaiWuZhaiYaoInParallel(stockList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
