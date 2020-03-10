package org.benayas.jevolutionary.domain;

import org.benayas.jevolutionary.util.RandomGen;

class IntegerGenerator implements IParamGenerator<Integer> {

	@Override
	public Integer gaussian(IDomain<Integer> domain, Integer value) {	
		int newValue = 0;
		int n = 5;
		int i = 0;
		do{		
			newValue = (int) Math.round( value + ( value * RandomGen.get().gauss() ) );
			i++;
		}while( ( newValue  > domain.upperBound() || newValue < domain.lowerBound() ) && ( i < n ) );
		
		newValue = newValue > domain.upperBound() ? domain.upperBound() : newValue < domain.lowerBound() ? domain.lowerBound() : newValue;
		
		return newValue;
	}

	@Override
	public Integer random(IDomain<Integer> domain) {
		int rangeSize = 1 + domain.upperBound() - domain.lowerBound();
		int value = rangeSize > 0 ? RandomGen.get().getInt( rangeSize ) + domain.lowerBound() : domain.upperBound();
		if ( value < 0 )
			System.out.println( value );
		return value;
	}
}
