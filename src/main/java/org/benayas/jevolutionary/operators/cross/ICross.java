package org.benayas.jevolutionary.operators.cross;

import java.util.List;

import org.benayas.jevolutionary.IBaseStructure;
import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.UtilReflect;

public interface ICross<T extends Individual, R extends IBaseStructure> extends IOperator{
		public List<T> reproduct( R structure, List<T> parent );
		
		
		public static ICross<?,?> create(String className, Object[] params ){
		    try {		    	
		    	return (ICross<?,?>) UtilReflect.create(Class.forName( className ), params );	    	
		        	                		        
			} catch (Exception e) {
				e.printStackTrace();
			}

		    return null; 
		}	
		
}
