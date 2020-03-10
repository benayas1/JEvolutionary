package org.benayas.jevolutionary.ga;

import org.benayas.jevolutionary.Individual;

public class IndividualVector extends Individual {

	private Object[] values;

	private IndividualVector( int dimension ){
		super();
		this.values = new Object[dimension];
	}
	
	private IndividualVector( Object[] values ){
		super();
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(int i){
		return (T) values[i];
	}
	
	public void set(int i, Object value){
		values[i] = value;
	}
		
	public int dimension(){
		return values.length;
	}
	
	@Override
	public Object[] values(){
		return values;
	}
	
	public static IndividualVector create( ChromosomeStructure structure ){
		Object[] values = new Object[structure.dimension()];
		for ( int i = 0; i < structure.dimension(); i++){
			values[i] = structure.get( i ).random();
		}		
		
		return new IndividualVector( values );
	}
	
	public static IndividualVector create( int dimension ){
		return new IndividualVector( dimension );
	}
	
	public String toString(){
		String val = "";
		for ( int i = 0; i < values.length; i++ ){
			val = val + " [" + i + "] = " + values[i].toString();
		}
		return "ID " + getId() + "\nScore: " + score.toString() + "\nParams:" + val;
	}
	
// The following methods are for cloning purposes. Clone is used to create the matting pool	
	
/*	public Individual clone(){
		try {
			Individual ind = (Individual) super.clone();
			ind.id = combID++;
			ind.score = null;
			ind.values = new Object[ this.values.length ];
			for ( int i = 0; i < ind.values.length ; i++ ){
				ind.values[i] = values[i];
			}
			
			return ind;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}*/
	
	@Override
	public boolean equals(Object o) {
		IndividualVector ind = (IndividualVector) o;
		for ( int i = 0; i < values.length; i++ ){
			if ( values[ i ] != ind.values[ i ] )
				return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int h = 0;
		h = 37 * h + values.length;
		for ( int i = 0; i < values.length; i++ ){
			h = 37 * h +  values[ i ].hashCode();
		}
		return h;
	}

	@Override
	public int compareTo(Individual o) {
		return score.compareTo( o.getEvaluation() );
	}
}
