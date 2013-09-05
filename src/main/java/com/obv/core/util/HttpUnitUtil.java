/**
 * 
 */
package com.obv.core.util;

import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.SAXException;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

/**
 * @author yingdin
 * 
 */
public class HttpUnitUtil {
    // structure of priceVolumeResults-- 1 Date 2 Open Price 5 Close Price 6
    // Volumn of day

    public static ArrayList<String> getStockTradeDetailFromDate(String _stockID, String _year, String _month,
            String _date) throws IOException, SAXException {

        String monthOfYahoo = getRealMonth(_month);
        ArrayList<String> priceVolumeResults = new ArrayList<String>();
        HttpUnitOptions.setScriptingEnabled(false);
        WebConversation wc = new WebConversation();
//        wc.setHeaderField(fieldName, fieldValue);
        WebResponse response;
        if(_stockID.matches("^00") || _stockID.matches("^30")) {
            response = wc.getResponse("http://finance.yahoo.com/q/hp?s=" + _stockID + ".SZ&a=" + monthOfYahoo + "&b="
                    + _date + "&c=" + _year + "&d=08&e=4&f=" + _year + "&g=d");
        } else {
            response = wc.getResponse("http://finance.yahoo.com/q/hp?s=" + _stockID + ".SS&a=" + monthOfYahoo + "&b="
                    + _date + "&c=" + _year + "&d=08&e=4&f=" + _year + "&g=d");
        }
        WebTable[] table = response.getTables();
        if(table.length >= 4) {
            String infoTable = table[4].getTableCell(1, 0).getText().replaceAll(",", "");
            String[] strlines = infoTable.split("\n");
            for(int i = 0; i < strlines.length; i++) {
                if(strlines[i].contains(_year)) {
                    priceVolumeResults.add(strlines[i]);
                }
            }

        }

        return priceVolumeResults;
    }

    private static String getRealMonth(String _month) {
        String monthOfYahooStr = null;
        Integer monthOfYahoo = Integer.parseInt(_month);
        monthOfYahoo = monthOfYahoo - 1;
        if(monthOfYahoo <= 10) {
            monthOfYahooStr = "0" + monthOfYahoo;
        } else {
            monthOfYahooStr = monthOfYahoo.toString();
        }
        return monthOfYahooStr;

    }

}
