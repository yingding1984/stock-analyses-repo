/**
 * 
 */
package com.obv.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author yingdin
 *
 */
public class FileLoader {

    public static String loadFile(String filePath) throws Exception{
        File file = new File(filePath);
        InputStream in = null;
        in = new FileInputStream(file);
        String reqMsgContentLine = "";
        String reqMsg = "";
        BufferedReader bufferreader = new BufferedReader(new InputStreamReader(in));

        while((reqMsgContentLine = bufferreader.readLine()) != null) {
            reqMsg += reqMsgContentLine;
        }
        return reqMsg;
    }
}
