package org.benayas.jevolutionary.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.Generation;
import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.operators.cross.ICross;
import org.benayas.jevolutionary.operators.mutation.IMutation;
import org.benayas.jevolutionary.operators.replacement.IReplacement;
import org.benayas.jevolutionary.operators.selection.ISelection;
import org.benayas.jevolutionary.stop.IStopCondition;

public class GeneticAlgorithm extends EvolutionaryComputation<IndividualVector, ChromosomeStructure>{

	//Operators
	private ISelection<IndividualVector> selector;
	private ICross<IndividualVector,ChromosomeStructure> reproductor;
	private IMutation<IndividualVector,ChromosomeStructure> mutator;
	private IReplacement<IndividualVector> replacement;
	
	public GeneticAlgorithm( ChromosomeStructure structure, int elements, int loops, int verbose, IStopCondition stop, IOperator[] operators ){
		super( structure, elements, loops, stop, verbose );
		this.selector = (ISelection<IndividualVector>) operators[0];
		this.reproductor = (ICross<IndividualVector,ChromosomeStructure>) operators[1];
		this.mutator = (IMutation<IndividualVector,ChromosomeStructure>) operators[2];
		this.replacement = (IReplacement<IndividualVector>) operators[3];
	}
	
	public GeneticAlgorithm( ChromosomeStructure structure, int elements, int loops, int verbose, IStopCondition stop, ISelection<IndividualVector> selector, ICross<IndividualVector,ChromosomeStructure> reproductor, IMutation<IndividualVector,ChromosomeStructure> mutator, IReplacement<IndividualVector> replacement ){
		this( structure, elements, loops, verbose, stop, new IOperator[]{selector, reproductor, mutator, replacement} );
	}
	
	private Generation<IndividualVector> createInitialGeneration(){
		List<IndividualVector> list = new ArrayList<>();
		for ( int i = 0; i < elements; i++){
			list.add( IndividualVector.create( structure ) );
		}

		return new Generation<>( list );
	}
	
	@Override
	protected Generation<IndividualVector> nextGeneration(){
		if ( generation == null )
			return createInitialGeneration();
				
		//Selection Operator
		List<IndividualVector> mattingPool = selector.select( generation.getEvaluatedIndividuals() );
		
		//Reproduction Operator		
		List<IndividualVector> children = reproductor.reproduct( structure, mattingPool );
				
		//Mutation operator
		children = mutator.mutate( structure, children );
		
		//Replacement operator
		List<IndividualVector> next = replacement.replace( generation.getEvaluatedIndividuals(), children );
		
		Generation<IndividualVector> nextGeneration = new Generation<>( next );
		
		return nextGeneration;
	}
	
	@Override
	protected boolean stop() {
		return generation == null ? false : stop.stop( generation );
	}
	
/*	private double evaluate( IFitness function, Object o){
		return function.fitness( o );
	}*/
	
}
