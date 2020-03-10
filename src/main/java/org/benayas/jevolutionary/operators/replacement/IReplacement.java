package org.benayas.jevolutionary.operators.replacement;

import java.util.List;

import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.UtilReflect;

public interface IReplacement<T extends Individual> extends IOperator{

	public List<T> replace( List<T> oldGen, List<T> offspring );
	
	public static IReplacement<?> create(String className, Object[] params ){
	    try {		    	
	    	return (IReplacement<?>) UtilReflect.create(Class.forName( className ), params );	    	
	        	                		        
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return null; 
	}
}
