package org.benayas.jevolutionary.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class Log {
	
	public static boolean useStandardLog = false;
	private static Logger logger = null;
	
	public static void log(Level level, String msg){
		if ( useStandardLog ){
			StandardLog.info( msg );
			return;
		}
		
		if ( logger == null){
			ThreadContext.put( "ThreadID", Thread.currentThread().getName() );
			ThreadContext.put( "ROUTINGKEY", "testingKey" );
			logger = LogManager.getLogger("benayas.tradingServices.MainClass");		
		}
	
		logger.log(level, msg);
	}
	
	public static void debug( String msg ){
		log( Level.DEBUG, msg);
	}
	
	public static void error( String msg ){
		log( Level.ERROR, msg);
	}
	
	public static void fatal( String msg ){
		log( Level.FATAL, msg);
	}
	
	public static void info( String msg ){
		log( Level.INFO, msg);
	}
	
	public static void trace( String msg ){
		log( Level.TRACE, msg);
	}
	
	public static void warn( String msg ){
		log( Level.WARN, msg);
	}

}
