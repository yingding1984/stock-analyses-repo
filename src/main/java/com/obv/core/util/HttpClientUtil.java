/**
 * 
 */
package com.obv.core.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author yingdin
 * 
 */
public class HttpClientUtil {

    public static void getWebPage() throws HttpException, IOException {
        HttpMethod method = new GetMethod(
                "http://finance.yahoo.com/q/hp?s=600318.SS&a=11&b=8&c=2000&d=08&e=5&f=2013&g=d");
        byte[] responseBody = method.getResponseBody();
        method.releaseConnection();
        System.out.println(new String(responseBody));
    }
}
