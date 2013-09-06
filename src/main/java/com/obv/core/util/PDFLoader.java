/**
 * 
 */
package com.obv.core.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 * @author yingdin
 * 
 */
public class PDFLoader {
    private final static String STOCK_LIST_FILE_PATH = "src/main/resources/JunXu.pdf";

    public static String readPDF(String path) throws Exception {
        try {

            PdfReader reader = new PdfReader(path);
            int n = reader.getNumberOfPages();
            String content = new String();
            for(int i = 1; i < n; i++) {
                System.out.println("**********************************PAGE NUMBER " + i
                        + "*************************************************************************************");
                String contentLine = PdfTextExtractor.getTextFromPage(reader, i);
                System.out.println(contentLine);
                content += contentLine;
            }
            return content;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // System.out.println(readPDF(STOCK_LIST_FILE_PATH));
        readPDF(STOCK_LIST_FILE_PATH);
    }
}
