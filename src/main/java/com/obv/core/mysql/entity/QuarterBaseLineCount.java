/**
 * 
 */
package com.obv.core.mysql.entity;

import java.util.Hashtable;

/**
 * @author yingding
 * 
 */
public class QuarterBaseLineCount {

	public static Hashtable<Quarter, Integer> qHash = QuarterBaseLineCount.getQuarterHash();

	/**
	 * 
	 */
	private static Hashtable<Quarter, Integer> getQuarterHash() {
		// TODO Auto-generated constructor stub
		Hashtable<Quarter, Integer> qHash = new Hashtable<Quarter, Integer>();
		qHash.put(Quarter._2013C, 0);
		qHash.put(Quarter._2013B, 1);
		qHash.put(Quarter._2013A, 2);
		qHash.put(Quarter._2012D, 3);
		qHash.put(Quarter._2012C, 4);
		qHash.put(Quarter._2012B, 5);
		qHash.put(Quarter._2012A, 6);
		qHash.put(Quarter._2011D, 7);
		qHash.put(Quarter._2011C, 8);
		qHash.put(Quarter._2011B, 9);
		qHash.put(Quarter._2011A, 10);
		
		return qHash;

	}
	

}
