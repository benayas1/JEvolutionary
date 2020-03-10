package org.benayas.jevolutionary.domain;

interface IParamGenerator<T>{
	public T gaussian(IDomain<T> domain, T val);
	public T random( IDomain<T> domain );
	
	public static class Factory{
		@SuppressWarnings("unchecked")
		public static <T> IParamGenerator<T> get( Class<T> type ){
			if ( type == Double.class || type == double.class )
				return (IParamGenerator<T>) new DoubleGenerator();
			
			if ( type == Integer.class || type == int.class )
				return (IParamGenerator<T>) new IntegerGenerator();
			
			if ( type == Boolean.class || type == boolean.class )
				return (IParamGenerator<T>) new BooleanGenerator();
			
			return null;			
		}
	}
}
