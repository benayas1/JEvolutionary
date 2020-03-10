package org.benayas.jevolutionary.stop;

import org.benayas.jevolutionary.Generation;
import org.benayas.jevolutionary.util.UtilReflect;

public interface IStopCondition{
	
	public static IStopCondition create(String className, Object[] params ){
	    try {		    	
	    	return (IStopCondition) UtilReflect.create(Class.forName( className ), params );	    	
	        	                		        
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return null; 
	}
	
	public boolean stop( Generation<?> g );
}
