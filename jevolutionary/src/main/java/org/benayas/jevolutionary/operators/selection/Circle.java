package org.benayas.jevolutionary.operators.selection;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.RandomGen;

public class Circle<T extends Individual> implements ISelection<T> {
	
	private double pct;

	public Circle( double pct ){
		this.pct = pct;
	}
	
	@Override
	public List<T> select(List<T> gen) {
		
		int n = (int) Math.round( gen.size() * ( 1 - pct ) );
		n = n / 2;
		n = n % 2 == 1 ? n-- : n;
		
		List<T> listAux = new ArrayList<>( gen );
		List<T> scrambled = new ArrayList<>();
		
		for ( int i = 0; i < gen.size(); i++ ){
			int index = RandomGen.get().getInt( listAux.size() );
			scrambled.add( listAux.get( index ) );
			listAux.remove( index );			
		}

		List<T> list = new ArrayList<>();
		
		for ( int i = 0; i < n ; i++ ){
			int index = RandomGen.get().getInt( scrambled.size() );
			list.add( scrambled.get( index ) );
			list.add( scrambled.get( ( index + scrambled.size() / 2 ) % scrambled.size() ) );
		}
		
		return list;
	}
}
