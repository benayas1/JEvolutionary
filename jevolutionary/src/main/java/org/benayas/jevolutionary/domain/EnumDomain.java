package org.benayas.jevolutionary.domain;

class EnumDomain<T> implements IDomain<T> {

	private T[] values;

	public EnumDomain( T[] values ){
		this.values = values;
	}
	
	@Override
	public T lowerBound() {
		return values[0];
	}

	@Override
	public T upperBound() {
		return values[values.length - 1];
	}

	@Override
	public Class<?> type() {
		return values[0].getClass();
	}

	@Override
	public T random() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T gaussian(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
