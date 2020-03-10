package org.benayas.jevolutionary.util;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

public enum RandomGenEnum {
	
	INSTANCE();

	private RandomGenerator random;
	
	private RandomGenEnum(){
		random = new MersenneTwister( 7 );		
	}
	
	/*public static RandomGen get(){
		if ( instance == null ){
			instance = new RandomGen();
		}
		return instance;
	}*/
	
	public double gauss(){
		return random.nextGaussian();
	}
	
	public double gauss( double mean, double stdev ){
		return random.nextGaussian() * stdev + mean;
	}
	
	public double getDouble(){
		return random.nextDouble();
	}
	
	public double getDouble(double min, double max){
		return ( ( max - min ) * getDouble() ) + min;
	}
	
	public int getInt(){
		return random.nextInt();
	}
	
	public int getInt( int bound ){
		return random.nextInt( bound );
	}
	
	public boolean getBoolean(){
		return random.nextBoolean();
	}
}
