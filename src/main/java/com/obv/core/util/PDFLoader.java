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

    public static void readPDF(String path) throws Exception {
        try {

            PdfReader reader = new PdfReader(path);
            int n = reader.getNumberOfPages();
            System.out.println("page number = " + n);
            String str = PdfTextExtractor.getTextFromPage(reader, n); // Extracting
            System.out.println(str);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
