package org.benayas.jevolutionary.domain;

import org.benayas.jevolutionary.util.RandomGen;

class BooleanGenerator implements IParamGenerator<Boolean> {

	@Override
	public Boolean gaussian(IDomain<Boolean> domain, Boolean val) {
		double gauss = RandomGen.get().gauss();
		//Switches value only if gauss is outside [-1,1], which means is out of 1 stdev, or only 32% of chances
		return ( gauss > 1 || gauss < -1 ) ? !val : val;
	}

	@Override
	public Boolean random(IDomain<Boolean> domain) {
		return RandomGen.get().getBoolean();
	}
}
