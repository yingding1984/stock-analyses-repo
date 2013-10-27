/**
 * 
 */
package com.obv.core.operation.multirun;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.obv.core.mysql.entity.Quarter;
import com.obv.core.util.FileUtil;

/**
 * @author yingding
 * 
 */
public class MultiRunner {
	private final static String STOCK_LIST_FILE_PATH = "src/main/resources/list";
//	private final static String STOCK_LIST_FILE_PATH = "src/main/resources/failedlist";
	private final static int THREAD_COUNT = 20;
	private final static String FROM_YEAR = "2013";
	private final static String FROM_MONTH = "08";
	private final static String FROM_DATE = "01";
	private final static String TO_YEAR = "2013";
	private final static String TO_MONTH = "09";
	private final static String TO_DATE = "05";

	private static String[] getStockList() throws Exception {
		String stockFileContent = FileUtil.loadFile(STOCK_LIST_FILE_PATH);
		String stockID;
		ArrayList<String> StockListArray = new ArrayList();

		String[] stockGroup = new String[THREAD_COUNT];

		int stockNum = 0;

		Pattern pattern = Pattern.compile("\\d{6}");
		Matcher matcher = pattern.matcher(stockFileContent);
		while (matcher.find()) {
			stockID = matcher.group();
			StockListArray.add(stockID);
		}

		stockNum = StockListArray.size();
		System.out.println(stockNum+" stocks in all!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		int groupSize = stockNum / THREAD_COUNT;

		for (int i = 0; i < THREAD_COUNT - 1; i++) {
			StringBuffer stockSB = new StringBuffer();
			for (int j = groupSize * i; j < groupSize * (i + 1); j++) {
				stockSB.append(StockListArray.get(j) + " ");
			}
			stockGroup[i] = stockSB.toString();
		}

		// build last group
		StringBuffer stockSB = new StringBuffer();
		for (int i = groupSize * (THREAD_COUNT - 1); i < stockNum; i++) {

			stockSB.append(StockListArray.get(i) + " ");

		}
		stockGroup[THREAD_COUNT - 1] = stockSB.toString();

		return stockGroup;
	}

	public static void main(String[] args) throws Exception {
		String[] s = getStockList();
//
//		for (int i = 0; i < THREAD_COUNT; i++) {
//			Thread thread = new DataCollectorThread(FROM_YEAR, FROM_MONTH,
//					FROM_DATE, TO_YEAR, TO_MONTH, TO_DATE, s[i]);
//			thread.start();
//		}
		FileUtil.cleanLogFile();
		for (int i = 0; i < THREAD_COUNT; i++) {
		Thread th = new CaiwuzhaiyaoThread(s[i],Quarter._2013C);
		th.start();
	}
	}
}
