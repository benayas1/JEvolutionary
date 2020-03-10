package org.benayas.jevolutionary.domain;

public class BooleanDomain implements IDomain<Boolean> {
	
	private IParamGenerator<Boolean> generator;
	
	@SuppressWarnings("unchecked")
	public BooleanDomain(){
		this.generator = (IParamGenerator<Boolean>) IParamGenerator.Factory.get( type() );
	}

	@Override
	public Boolean lowerBound() {
		return false;
	}

	@Override
	public Boolean upperBound() {
		return true;
	}

	@Override
	public Class<?> type() {
		return Boolean.class;
	}

	@Override
	public Boolean random() {
		return generator.random( this );
	}

	@Override
	public Boolean gaussian(Object value) {
		return generator.gaussian( this, (Boolean) value);
	}

}
