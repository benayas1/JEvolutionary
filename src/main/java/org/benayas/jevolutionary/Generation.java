package org.benayas.jevolutionary;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.util.Log;



public class Generation<T extends Individual> {
	
	private Map<Integer, T> individuals = new HashMap<>();
	private T best;
	
	public Generation( List<T> list ){
		list.forEach( i -> { individuals.put( i.getId(), i );
		                     if ( i.hasEvaluation() ) 
		                    	 score( i.getId(), i.getEvaluation() );
						   }
		);
	}

	public void score( int id, IEvaluation t ){
		T ind = individuals.get( id );
		ind.setEvaluation(t);
		best = best == null ? ind : best.compareTo(ind) == 1 ? best : ind;
	}
	
	public T best(){
		return best;
	}

	public List<T> getEvaluatedIndividuals() {
		List<T> list = individuals.values().stream().filter( e -> e.hasEvaluation() ).collect( Collectors.toList() );
		Collections.sort( list );
		return list;
	}
	
	public void printResults(){
		Log.trace( "Results of this generation: " );
		List<T> list = getEvaluatedIndividuals();
		for ( T i : list ){
			Log.trace( i.toString() );
		}
	}
	
	public Collection<T> toEvaluate(){
		return individuals.values().stream().filter( e -> !e.hasEvaluation() ).collect( Collectors.toList() );
	}

	public double averageScore() {
		return individuals.values().stream().filter( e -> e.hasEvaluation() ).mapToDouble( e -> e.getScore() ).average().orElse( 0 );
	}

	public long zeroCounts() {
		return individuals.values().stream().filter( e -> e.hasEvaluation() && e.getScore() == 0 ).count();
	}

	public Object minimum() {
		return individuals.values().stream().filter( e -> e.hasEvaluation() ).mapToDouble( e -> e.getScore() ).min().orElse( 0 );
	}
}
