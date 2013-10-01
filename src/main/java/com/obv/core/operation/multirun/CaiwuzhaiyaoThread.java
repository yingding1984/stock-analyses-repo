/**
 * 
 */
package com.obv.core.operation.multirun;

import com.obv.core.mysql.entity.Quarter;
import com.obv.core.mysql.util.MySQLUtil;

/**
 * @author yingding
 * 
 */
public class CaiwuzhaiyaoThread extends Thread {

	private String stockList;
	private Quarter Q;

	public CaiwuzhaiyaoThread(String _stockList,Quarter _Q) {

		this.stockList = _stockList;
		this.Q=_Q;
	}

	
	
	public void run() {
		try {
			MySQLUtil.storeCaiWuZhaiYaoInParallel(stockList,Q);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
