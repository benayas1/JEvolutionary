package org.benayas.jevolutionary.domain;

class IntegerDomain implements IDomain<Integer> {

	private int lower;
	private int upper;
	private IParamGenerator<Integer> generator;

	public IntegerDomain( int lower, int upper ){
		this.lower = lower;
		this.upper = upper;
		this.generator = (IParamGenerator<Integer>) IParamGenerator.Factory.get( type() );
	}
	
	@Override
	public Integer lowerBound() {
		return lower;
	}

	@Override
	public Integer upperBound() {
		return upper;
	}
	
	@Override
	public Class<?> type() {
		return Integer.TYPE;
	}

	@Override
	public Integer random() {
		return generator.random( this );
	}

	@Override
	public Integer gaussian(Object value) {
		return generator.gaussian( this, value instanceof Double ? ((Double)value).intValue() : (Integer) value );
	}

}
