package org.benayas.jevolutionary.operators.replacement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.benayas.jevolutionary.Individual;

public class Elitist<T extends Individual> implements IReplacement<T>{

	@Override
	public List<T> replace(List<T> oldGen, List<T> offspring) {
		List<T> newGen = new ArrayList<>( offspring );
		
		Collections.sort( oldGen );
		newGen.addAll( oldGen.subList( newGen.size() , oldGen.size() ) );
		
		return newGen;
	}

}
