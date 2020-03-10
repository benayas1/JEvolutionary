package org.benayas.jevolutionary.domain;

class RealDomain implements IDomain<Double> {

	private double lower;
	private double upper;
	private IParamGenerator<Double> generator;

	@SuppressWarnings("unchecked")
	public RealDomain( double lower, double upper ){
		this.lower = lower;
		this.upper = upper;
		this.generator = (IParamGenerator<Double>) IParamGenerator.Factory.get( type() );
	}
	
	@Override
	public Double lowerBound() {
		return lower;
	}

	@Override
	public Double upperBound() {
		return upper;
	}

	@Override
	public Class<?> type() {
		return Double.TYPE;
	}

	@Override
	public Double random() {
		return generator.random( this );
	}

	@Override
	public Double gaussian(Object value) {
		return generator.gaussian( this, (Double) value );
	}
	
}
