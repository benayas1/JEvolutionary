package org.benayas.jevolutionary.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StandardLog {
	private static Logger logger = null;
	
	public static void log(Level level, String msg){
		if ( logger == null){
			logger = Logger.getLogger(StandardLog.class.getName());		
		}
	
		logger.log(level, msg);
	}
	
	public static void info( String msg ){
		log( Level.INFO, msg);
	}
		
}