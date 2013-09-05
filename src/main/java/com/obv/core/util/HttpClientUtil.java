/**
 * 
 */
package com.obv.core.util;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author yingdin
 *
 */
public class HttpClientUtil {

    public static void getWebPage() throws HttpException, IOException{
        HttpMethod method = new GetMethod("http://finance.yahoo.com/q/hp?s=600318.SS&a=11&b=8&c=2000&d=08&e=5&f=2013&g=d");
        byte[] responseBody = method.getResponseBody();
        method.releaseConnection();
        System.out.println(new String(responseBody));
    }
    
    public static void main(String[] args) throws HttpException, IOException {
        String url = "http://finance.yahoo.com/q/hp?s=600318.SS&a=11&b=8&c=2000&d=08&e=5&f=2013&g=d";

       
          HttpClient client = new HttpClient();

          // Create a method instance.
          GetMethod method = new GetMethod(url);
          
          // Provide custom retry handler is necessary
          method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
                  new DefaultHttpMethodRetryHandler(3, false));

          try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();

            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            System.out.println(new String(responseBody));

          } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
          } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
          } finally {
            // Release the connection.
            method.releaseConnection();
          }  
        }
    }

