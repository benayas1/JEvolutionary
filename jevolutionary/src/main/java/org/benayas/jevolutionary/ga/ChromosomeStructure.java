package org.benayas.jevolutionary.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.benayas.jevolutionary.IBaseStructure;
import org.benayas.jevolutionary.domain.IDomain;
import org.benayas.jevolutionary.util.Permutator;

public class ChromosomeStructure implements IBaseStructure {
	
	private IDomain<?>[] params;
	
	public ChromosomeStructure( List<Permutator> permutators ){
		this.params = new IDomain<?>[permutators.size()];
		Collections.sort( permutators );
		
		for ( int i = 0; i < permutators.size(); i++){
			params[i] = IDomain.Factory.create( permutators.get( i ) );
		}
	}
	
	public ChromosomeStructure( IDomain<?>[] params ){
		this.params = params;
	}
	
	public IDomain<?> get( int param ){
		return params[ param ];
	}
	
	public int dimension(){
		return params.length;
	}
	
	public List<Permutator> getPermutators(){
		List<Permutator> list = new ArrayList<>();
		for (int i = 0; i < params.length; i++){
			list.add( new Permutator( i, (Double)params[i].lowerBound(), (Double)params[i].upperBound(), params[i].type().getName() ) );
		}
		return list;
	}
	
}
