package org.benayas.jevolutionary.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.benayas.jevolutionary.IBaseStructure;
import org.benayas.jevolutionary.domain.IDomain;
import org.benayas.jevolutionary.util.Gene;

public class ChromosomeStructure implements IBaseStructure {
	
	private IDomain<?>[] params;
	
	public ChromosomeStructure( List<Gene> genes ){
		this.params = new IDomain<?>[genes.size()];
		Collections.sort( genes );
		
		for ( int i = 0; i < genes.size(); i++){
			params[i] = IDomain.Factory.create( genes.get( i ) );
		}
	}
	
	public ChromosomeStructure( Gene... genes ){
		this( Arrays.asList(genes) );
	}
	
	public ChromosomeStructure( IDomain<?>... params ){
		this.params = params;
	}
	
	public IDomain<?> get( int param ){
		return params[ param ];
	}
	
	public int dimension(){
		return params.length;
	}
	
	public List<Gene> getPermutators(){
		List<Gene> list = new ArrayList<>();
		for (int i = 0; i < params.length; i++){
			list.add( new Gene( i, (Double)params[i].lowerBound(), (Double)params[i].upperBound(), params[i].type().getName() ) );
		}
		return list;
	}
	
}
