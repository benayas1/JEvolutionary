package org.benayas.jevolutionary.operators.mutation;

import java.util.List;

import org.benayas.jevolutionary.IBaseStructure;
import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.UtilReflect;

public interface IMutation<T extends Individual, R extends IBaseStructure> extends IOperator{
	public List<T> mutate( R structure, List<T> population );
	
	public static IMutation<?,?> create(String className, Object[] params ){
	    try {		    	
	    	return (IMutation<?,?>) UtilReflect.create(Class.forName( className ), params );	    	
	        	                		        
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return null; 
	}
}
