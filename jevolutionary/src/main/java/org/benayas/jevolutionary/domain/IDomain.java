package org.benayas.jevolutionary.domain;

import org.benayas.jevolutionary.util.Permutator;

public interface IDomain<T> {
	
	static final String DOUBLE = "Double";
	static final String INTEGER = "Integer";
	static final String BOOLEAN = "Boolean";
	
	public T lowerBound();
	public T upperBound();
	public T random();
	public T gaussian( Object value );
	public Class<?> type();
	
	public static class Factory{
		public static <T> IDomain<?> create( T low, T high ){
			if ( high.getClass() == Double.class )
				return new RealDomain( (Double)low, (Double)high );
			
			if ( high.getClass() == Integer.class )
				return new IntegerDomain( (Integer)low, (Integer)high );
			
			if ( high.getClass() == Boolean.class )
				return new BooleanDomain();
			
			return null;	
		}
		
		public static IDomain<?> create( Permutator p ){
			
			if ( p.getDomain().equals( DOUBLE) ){
				return create( p.getMin(), p.getMax() );
			}
			
			if ( p.getDomain().equals( INTEGER ) ){
				return create( (int)p.getMin(), (int)p.getMax() );
			}
			
			if ( p.getDomain().equals( BOOLEAN ) ){
				return create( false, true );
			}
			
			return null;	
		}
	}
}
