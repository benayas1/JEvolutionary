package org.benayas.jevolutionary.ga;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.Generation;
import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.algorithms.EvolutiveComputation;
import org.benayas.jevolutionary.operators.mutation.IMutation;
import org.benayas.jevolutionary.operators.replacement.IReplacement;
import org.benayas.jevolutionary.operators.selection.ISelection;
import org.benayas.jevolutionary.stop.IStopCondition;

public class EvolutiveStrategy extends EvolutiveComputation<IndividualVector, ChromosomeStructure> {

	private ISelection<IndividualVector> selector;
	private IMutation<IndividualVector,ChromosomeStructure> mutator;
	private IReplacement<IndividualVector> replacement;

	public EvolutiveStrategy(ChromosomeStructure structure, int elements, int loops, int verbose,  IStopCondition stop, IOperator... operators) {
		super(structure, elements, loops, stop, verbose);
		this.selector = (ISelection<IndividualVector>) operators[0];
		this.mutator = (IMutation<IndividualVector,ChromosomeStructure>) operators[1];
		this.replacement = (IReplacement<IndividualVector>) operators[2];
	}
	
	public EvolutiveStrategy( ChromosomeStructure structure, int elements, int loops, int verbose, IStopCondition stop, ISelection<IndividualVector> selector, IMutation<IndividualVector,ChromosomeStructure> mutator, IReplacement<IndividualVector> replacement ){
		this( structure, elements, loops, verbose, stop, new IOperator[]{selector, mutator, replacement} );
	}
	
	private Generation<IndividualVector> createInitialGeneration(){
		List<IndividualVector> list = new ArrayList<>();
		for ( int i = 0; i < elements; i++){
			list.add( IndividualVector.create( structure ) );
		}

		return new Generation<IndividualVector>( list );
	}

	@Override
	protected Generation<IndividualVector> nextGeneration() {
		if ( generation == null )
			return createInitialGeneration();
				
		//Selection Operator
		List<IndividualVector> mattingPool = selector.select( generation.getEvaluatedIndividuals() );
				
		//Mutation operator
		mattingPool = mutator.mutate(structure, mattingPool);
		
		//Replacement operator
		List<IndividualVector> next = replacement.replace( generation.getEvaluatedIndividuals(), mattingPool );
		
		Generation<IndividualVector> nextGeneration = new Generation<>( next );
		
		return nextGeneration;
	}
}