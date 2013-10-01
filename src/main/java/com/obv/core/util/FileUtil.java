/**
 * 
 */
package com.obv.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author yingdin
 * 
 */
public class FileUtil {

	private final static String FAIL_LIST = "src/main/resources/failedlist";

	public static String loadFile(String filePath) throws Exception {
		File file = new File(filePath);
		InputStream in = null;
		in = new FileInputStream(file);
		String reqMsgContentLine = "";
		String reqMsg = "";
		BufferedReader bufferreader = new BufferedReader(new InputStreamReader(
				in));

		while ((reqMsgContentLine = bufferreader.readLine()) != null) {
			reqMsg += reqMsgContentLine;
		}
		return reqMsg;
	}

	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("error  ");
			e.printStackTrace();
		}
	}

	public static synchronized void logFailStock(String fail_ID) throws Exception {
		FileOutputStream out = new FileOutputStream(FAIL_LIST,true);  
        
        out.write((fail_ID+"\n").getBytes());  
          
        out.close();  
	}
	
	public static void cleanLogFile() throws Exception {
		FileOutputStream out = new FileOutputStream(FAIL_LIST);  
        
        out.write("".getBytes());  
          
        out.close();  
	}

	public static void main(String[] args) throws Exception {
		logFailStock("001111");
		logFailStock("121212");
	}
}
