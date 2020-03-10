package org.benayas.jevolutionary.operators.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.RandomGen;

public class Tournament<T extends Individual> implements ISelection<T> {

	private double pct;

	public Tournament( double pct ){
		this.pct = pct;
	}
	
	private T get( List<T> disponibles, Set<T> used ){
		T i = disponibles.get( RandomGen.get().getInt( disponibles.size() ) );
		while ( used.contains( i ) )
			i = disponibles.get( RandomGen.get().getInt( disponibles.size() ) );

		return i;
	}
	
	private T tournament( T p1, T p2){		
		return p1.compareTo( p2 ) == 1 ? p2 : p1;
	}

	@Override
	public List<T> select(List<T> gen) {
		List<T> pool = new ArrayList<>( gen );
		int n = (int) Math.round( pool.size() * ( pct ) );
		
		n = Math.min( n % 2 == 1 ? n-- : n, pool.size());
		Set<T> set = new HashSet<>();
		
		while ( set.size() < n )
			set.add( tournament(  get( pool, set ), get( pool, set )) );		
		
		return new ArrayList<>( set );
	}
}
