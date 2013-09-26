/**
 * 
 */
package com.obv.core.http.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;
import com.obv.core.mysql.entity.Quarter;
import com.obv.core.mysql.entity.QuarterBaseLineCount;

/**
 * @author yingdin
 * 
 */
public class HttpUnitUtil {
	// structure of priceVolumeResults-- 1 Date 2 Open Price 5 Close Price 6
	// Volumn of day

	private final static int BASE_INCREASEMENT = 14;

	public static ArrayList<String> getStockTradeDetailFromDate(
			String _stockID, String _from_year, String _from_month,
			String _from_date, String _to_year, String _to_month,
			String _to_date) throws IOException, SAXException {

		String fromMonthOfYahoo = getRealMonth(_from_month);
		String toMonthOfYahoo = getRealMonth(_to_month);
		ArrayList<String> priceVolumeResults = new ArrayList<String>();
		HttpUnitOptions.setScriptingEnabled(false);
		WebConversation wc = new WebConversation();
		WebResponse response;
		if (_stockID.matches("00\\d{4}") || _stockID.matches("30\\d{4}")) {
			response = wc.getResponse("http://finance.yahoo.com/q/hp?s="
					+ _stockID + ".SZ&a=" + fromMonthOfYahoo + "&b="
					+ _from_date + "&c=" + _from_year + "&d=" + toMonthOfYahoo
					+ "&e=" + _to_date + "&f=" + _to_year + "&g=d");
		} else {
			response = wc.getResponse("http://finance.yahoo.com/q/hp?s="
					+ _stockID + ".SS&a=" + fromMonthOfYahoo + "&b="
					+ _from_date + "&c=" + _from_year + "&d=" + toMonthOfYahoo
					+ "&e=" + _to_date + "&f=" + _to_year + "&g=d");
		}
		WebTable[] table = response.getTables();
		if (table.length >= 4) {
			String infoTable = table[4].getTableCell(1, 0).getText()
					.replaceAll(",", "");
			String[] strlines = infoTable.split("\n");
			for (int i = 0; i < strlines.length; i++) {
				if (strlines[i].contains(_from_year)
						|| strlines[i].contains(_to_year)) {
					priceVolumeResults.add(strlines[i]);
				}
			}

		}

		return priceVolumeResults;
	}

	public static ArrayList<Double> getCaiWuZhaiYao(String _stockID, Quarter q)
			throws IOException, SAXException {
		System.out.println("stock id is :" + _stockID);
		boolean isFirstQ = false;

		if (q.toString().contains("A"))
			isFirstQ = true;

		int plusCount = QuarterBaseLineCount.qHash.get(q);
		ArrayList<Double> caiWuZhaiYao_Data = new ArrayList<Double>();
		HttpUnitOptions.setScriptingEnabled(false);
		WebConversation wc = new WebConversation();
		WebResponse response;

		DecimalFormat df = new DecimalFormat(".##");

		response = wc
				.getResponse("http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/"
						+ _stockID + ".phtml");

		WebTable[] table = response.getTables();
		WebTable t = table[19];
		// System.out.println(t.getText());
		String meigujingzichan = t.getTableCell(
				3 + plusCount * BASE_INCREASEMENT, 1).getText();
		String meigushouyi = t.getTableCell(4 + plusCount * BASE_INCREASEMENT,
				1).getText();
		String meiguxianjinhanliang = t.getTableCell(
				5 + plusCount * BASE_INCREASEMENT, 1).getText();
		String meiguzibengongjijin = t.getTableCell(
				6 + plusCount * BASE_INCREASEMENT, 1).getText();
		String liudongzichanheji = t.getTableCell(
				8 + plusCount * BASE_INCREASEMENT, 1).getText();
		String zichanzongji = t.getTableCell(9 + plusCount * BASE_INCREASEMENT,
				1).getText();
		String changqifuzhaiheji = t.getTableCell(
				10 + plusCount * BASE_INCREASEMENT, 1).getText();

		double meigujingzichan_d = 0, meigushouyi_d = 0, meiguxianjinhanliang_d = 0, meiguzibengongjijin_d = 0, liudongzichanheji_d = 0, zichanzongji_d = 0, changqifuzhaiheji_d = 0;

		double zhuyingyewushouru_d = 0, caiwufeiyong_d = 0, jinlirun_d = 0;

		if (meigujingzichan != null && meigujingzichan.length() > 0)
			meigujingzichan_d = Double.parseDouble(meigujingzichan.substring(0,
					meigujingzichan.length() - 1));

		if (meigushouyi != null && meigushouyi.length() > 0)
			meigushouyi_d = Double.parseDouble(meigushouyi.substring(0,
					meigushouyi.length() - 1));

		if (meiguxianjinhanliang != null && meiguxianjinhanliang.length() > 0)
			meiguxianjinhanliang_d = Double.parseDouble(meiguxianjinhanliang
					.substring(0, meiguxianjinhanliang.length() - 1));

		if (meiguzibengongjijin != null && meiguzibengongjijin.length() > 0)
			meiguzibengongjijin_d = Double.parseDouble(meiguzibengongjijin
					.substring(0, meiguzibengongjijin.length() - 1));

		if (liudongzichanheji != null && liudongzichanheji.length() > 0)
			liudongzichanheji_d = Double.parseDouble(liudongzichanheji
					.substring(0, liudongzichanheji.length() - 2));

		if (zichanzongji != null && zichanzongji.length() > 0)
			zichanzongji_d = Double.parseDouble(zichanzongji.substring(0,
					zichanzongji.length() - 2));

		if (changqifuzhaiheji != null && changqifuzhaiheji.length() > 0)
			changqifuzhaiheji_d = Double.parseDouble(changqifuzhaiheji
					.substring(0, changqifuzhaiheji.length() - 2));

		if (isFirstQ) {
			String zhuyingyewushouru = t.getTableCell(
					11 + plusCount * BASE_INCREASEMENT, 1).getText();
			String caiwufeiyong = t.getTableCell(
					12 + plusCount * BASE_INCREASEMENT, 1).getText();
			String jinlirun = t.getTableCell(
					13 + plusCount * BASE_INCREASEMENT, 1).getText();

			if (zhuyingyewushouru != null && zhuyingyewushouru.length() > 0)
				zhuyingyewushouru_d = Double.parseDouble(zhuyingyewushouru
						.substring(0, zhuyingyewushouru.length() - 2));

			if (caiwufeiyong != null && caiwufeiyong.length() > 0)
				caiwufeiyong_d = Double.parseDouble(caiwufeiyong.substring(0,
						caiwufeiyong.length() - 2));

			if (jinlirun != null && jinlirun.length() > 0)
				jinlirun_d = Double.parseDouble(jinlirun.substring(0,
						jinlirun.length() - 2));

		} else {
			String zhuyingyewushouru_current = t.getTableCell(
					11 + plusCount * BASE_INCREASEMENT, 1).getText();
			String caiwufeiyong_current = t.getTableCell(
					12 + plusCount * BASE_INCREASEMENT, 1).getText();
			String jinlirun_current = t.getTableCell(
					13 + plusCount * BASE_INCREASEMENT, 1).getText();

			String zhuyingyewushouru_last = t.getTableCell(
					25 + plusCount * BASE_INCREASEMENT, 1).getText();
			String caiwufeiyong_last = t.getTableCell(
					26 + plusCount * BASE_INCREASEMENT, 1).getText();
			String jinlirun_last = t.getTableCell(
					27 + plusCount * BASE_INCREASEMENT, 1).getText();

			double zhuyingyewushouru_current_d = 0, caiwufeiyong_current_d = 0, jinlirun_current_d = 0, zhuyingyewushouru_last_d = 0, caiwufeiyong_last_d = 0, jinlirun_last_d = 0;

			if (zhuyingyewushouru_current != null
					&& zhuyingyewushouru_current.length() > 0)
				zhuyingyewushouru_current_d = Double
						.parseDouble(zhuyingyewushouru_current.substring(0,
								zhuyingyewushouru_current.length() - 2));

			if (caiwufeiyong_current != null
					&& caiwufeiyong_current.length() > 0)
				caiwufeiyong_current_d = Double
						.parseDouble(caiwufeiyong_current.substring(0,
								caiwufeiyong_current.length() - 2));

			if (jinlirun_current != null && jinlirun_current.length() > 0)
				jinlirun_current_d = Double.parseDouble(jinlirun_current
						.substring(0, jinlirun_current.length() - 2));

			if (zhuyingyewushouru_last != null
					&& zhuyingyewushouru_last.length() > 0)
				zhuyingyewushouru_last_d = Double
						.parseDouble(zhuyingyewushouru_last.substring(0,
								zhuyingyewushouru_last.length() - 2));

			if (caiwufeiyong_last != null && caiwufeiyong_last.length() > 0)
				caiwufeiyong_last_d = Double.parseDouble(caiwufeiyong_last
						.substring(0, caiwufeiyong_last.length() - 2));

			if (jinlirun_last != null && jinlirun_last.length() > 0)
				jinlirun_last_d = Double.parseDouble(jinlirun_last.substring(0,
						jinlirun_last.length() - 2));

			zhuyingyewushouru_d = Double.parseDouble(df
					.format(zhuyingyewushouru_current_d
							- zhuyingyewushouru_last_d));
			caiwufeiyong_d = Double.parseDouble(df
					.format(caiwufeiyong_current_d - caiwufeiyong_last_d));
			jinlirun_d = Double.parseDouble(df.format(jinlirun_current_d
					- jinlirun_last_d));
		}

		System.out.println(meigujingzichan_d);
		System.out.println(meigushouyi_d);
		System.out.println(meiguxianjinhanliang_d);
		System.out.println(meiguzibengongjijin_d);
		System.out.println(liudongzichanheji_d);
		System.out.println(zichanzongji_d);
		System.out.println(changqifuzhaiheji_d);
		System.out.println(zhuyingyewushouru_d);
		System.out.println(caiwufeiyong_d);
		System.out.println(jinlirun_d);

		caiWuZhaiYao_Data.add(meigujingzichan_d);
		caiWuZhaiYao_Data.add(meigushouyi_d);
		caiWuZhaiYao_Data.add(meiguxianjinhanliang_d);
		caiWuZhaiYao_Data.add(meiguzibengongjijin_d);
		caiWuZhaiYao_Data.add(liudongzichanheji_d);
		caiWuZhaiYao_Data.add(zichanzongji_d);
		caiWuZhaiYao_Data.add(changqifuzhaiheji_d);
		caiWuZhaiYao_Data.add(zhuyingyewushouru_d);
		caiWuZhaiYao_Data.add(caiwufeiyong_d);
		caiWuZhaiYao_Data.add(jinlirun_d);

		return caiWuZhaiYao_Data;
	}

	public static void main(String[] args) throws IOException, SAXException {
		getCaiWuZhaiYao("002528", Quarter._2013B);
	}

	private static String getRealMonth(String _month) {
		String monthOfYahooStr = null;
		Integer monthOfYahoo = Integer.parseInt(_month);
		monthOfYahoo = monthOfYahoo - 1;
		if (monthOfYahoo <= 10) {
			monthOfYahooStr = "0" + monthOfYahoo;
		} else {
			monthOfYahooStr = monthOfYahoo.toString();
		}
		return monthOfYahooStr;

	}

}
