package org.benayas.jevolutionary.operators.mutation;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.domain.IDomain;
import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.util.RandomGen;

public class GaussMutator implements IMutation<IndividualVector, ChromosomeStructure> {
	
	private double mutationRate;

	public GaussMutator( double mutationRate ){
		this.mutationRate = mutationRate;
	}
	
	@Override
	public List<IndividualVector> mutate( ChromosomeStructure structure, List<IndividualVector> population ) {
		List<IndividualVector> output = new ArrayList<>();
		
		population.forEach( i -> output.add( mutation( structure, i ) ) );
		
		return output;
	}
	
	private IndividualVector mutation( ChromosomeStructure structure, IndividualVector individual ) {	
		if ( RandomGen.get().getDouble() < mutationRate ){
			int gen = RandomGen.get().getInt( individual.dimension() );
			IDomain<?> domain = structure.get( gen );
			individual.set( gen, domain.gaussian( individual.get( gen ) ) );
		}
		
		return individual;
	}
}