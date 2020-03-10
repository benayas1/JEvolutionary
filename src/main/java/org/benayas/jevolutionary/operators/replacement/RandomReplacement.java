package org.benayas.jevolutionary.operators.replacement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.util.RandomGen;


public class RandomReplacement<T extends Individual> implements IReplacement<T>{

	@Override
	public List<T> replace(List<T> oldGen, List<T> offspring) {
		Set<Integer> used = new HashSet<>();
		List<T> newGen = new ArrayList<>( offspring );
		int required =  oldGen.size() - newGen.size();
		
		int n = 0;
		while ( n < required ){
			int random = RandomGen.get().getInt( oldGen.size() );
			if ( !used.contains( random ) ){
				used.add( random );
				newGen.add( oldGen.get( random ) );
				n++;
			}
		}
		
		return newGen;
	}
}
