package org.benayas.jevolutionary.operators.selection;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.Individual;

public class Bests<T extends Individual> implements ISelection<T> {
	
	private double pct;

	public Bests( double pct ){
		this.pct = pct;
	}
	
	@Override
	public List<T> select( List<T> gen ) {
		int index = (int) Math.round( gen.size() * ( 1 - pct ) );
		if ( index % 2 == 1 )
			gen.remove( 0 );
		
		List<T> list = new ArrayList<>();
		gen.forEach( i -> list.add( i ) );
		
		return list;
	}


}
