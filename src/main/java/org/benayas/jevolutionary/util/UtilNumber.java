package org.benayas.jevolutionary.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UtilNumber {
	/**
	 * Rounds using 2 decimal places
	 * @param value
	 * @return
	 */
	public static double round(double value){
		return round(value,2);
	}
	
	/**
	 * Round using N decimal places
	 * @param value value to be rounded
	 * @param places number of decimals
	 * @return
	 */
	public static double round(double value, int places) {
	    if (places < 0) 
	    	throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    double d = bd.doubleValue()* Math.pow(10, places);
	    long rounded = Math.round(d);
	    d = (double)( rounded / Math.pow(10, places) );
	    return bd.doubleValue();
	}
	
	public int toInt( double d ){
		return (int)d;
	}
}
