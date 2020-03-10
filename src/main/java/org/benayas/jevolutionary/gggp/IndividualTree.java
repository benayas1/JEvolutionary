package org.benayas.jevolutionary.gggp;

import org.benayas.jevolutionary.Individual;


public abstract class IndividualTree extends Individual implements Cloneable{
	
	public IndividualTree() {
		super();
	}

	@Override
	public abstract Object[] values();
	
	@Override
	public boolean equals(Object o) {
		IndividualTree ind = (IndividualTree) o;
		return this.getId() == ind.getId();
	}
	
	@Override
	public int hashCode() {
		return this.getId() * 47;
	}

	@Override
	public int compareTo(Individual o) {
		return score.compareTo( o.getEvaluation() );
	}
	
	@Override
	public String toString(){
		String val = "";
		Object[] values = values();
		for ( int i = 0; i < values.length; i++ ){
			val = val + " [" + i + "] = " + values[i].toString() + "\n";
		}
		return "ID " + getId() + "\nScore: " + score.toString() + "\nParams:\n" + val;
	}

}