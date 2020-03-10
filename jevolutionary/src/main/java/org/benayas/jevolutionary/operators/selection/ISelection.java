package org.benayas.jevolutionary.operators.selection;

import java.util.List;

import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.UtilReflect;

public interface ISelection<T extends Individual> extends IOperator{
	public List<T> select( List<T> gen );
	
	public static ISelection<?> create(String className, Object[] params ){
	    try {		    	
	    	return (ISelection<?>) UtilReflect.create(Class.forName( className ), params );	    	
	        	                		        
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return null; 
	}
}
