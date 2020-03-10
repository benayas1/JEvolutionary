package org.benayas.jevolutionary.domain;

import org.benayas.jevolutionary.util.RandomGen;
import org.benayas.jevolutionary.util.UtilNumber;

class DoubleGenerator implements IParamGenerator<Double> {

	@Override
	public Double gaussian(IDomain<Double> domain, Double value) {
		double newValue = 0;
		int n = 10;
		int i = 0;
		//the range inside the bounds is +- 3 st dev (6), so given mean = the intermediate value within the bounds, 99.7% of the values will be within
		//the range [ lowerbound , upperbound ]. 
		double stdev = ( domain.upperBound() - domain.lowerBound() ) / 6; 
		do{		
			newValue = RandomGen.get().gauss( value, stdev );
			i++;
		}while( ( newValue > domain.upperBound() || newValue < domain.lowerBound() ) && ( i < n ) );
		
		newValue = newValue > domain.upperBound() ? domain.upperBound() : newValue < domain.lowerBound() ? domain.lowerBound() : newValue;
		
		return UtilNumber.round( newValue, 4 );
	}

	@Override
	public Double random(IDomain<Double> domain) {
		return UtilNumber.round( ( ( domain.upperBound() - domain.lowerBound() ) * RandomGen.get().getDouble() ) + domain.lowerBound(), 4 );
	}
}
