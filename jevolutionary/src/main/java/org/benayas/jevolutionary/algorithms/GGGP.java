package org.benayas.jevolutionary.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.Generation;
import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.gggp.IndividualTree;
import org.benayas.jevolutionary.gggp.grammar.IGrammar;
import org.benayas.jevolutionary.gggp.init.ITreeInitMethod;
import org.benayas.jevolutionary.operators.cross.ICross;
import org.benayas.jevolutionary.operators.mutation.IMutation;
import org.benayas.jevolutionary.operators.replacement.IReplacement;
import org.benayas.jevolutionary.operators.selection.ISelection;
import org.benayas.jevolutionary.stop.IStopCondition;

public class GGGP extends EvolutionaryComputation<IndividualTree, IGrammar> {
	
	//Operators
	private ISelection<IndividualTree> selector;
	private ICross<IndividualTree, IGrammar> reproductor;
	private IMutation<IndividualTree,IGrammar> mutator;
	private IReplacement<IndividualTree> replacement;
	private ITreeInitMethod<?> init;

	public GGGP(IGrammar structure, int elements, int loops, int verbose, IStopCondition stop, IOperator[] operators, ITreeInitMethod<?> init ) {
		super(structure, elements, loops, stop, verbose); 
		this.selector = (ISelection<IndividualTree>) operators[0];
		this.reproductor = (ICross<IndividualTree, IGrammar>) operators[1];
		this.mutator = (IMutation<IndividualTree,IGrammar>) operators[2];
		this.replacement = (IReplacement<IndividualTree>) operators[3];		
		this.init = init; 
	}

	@Override
	protected Generation<IndividualTree> nextGeneration() {
		if ( generation == null )
			return createInitialGeneration();
				
		//Selection Operator
		List<IndividualTree> mattingPool = selector.select( generation.getEvaluatedIndividuals() );
		
		//Reproduction Operator		
		List<IndividualTree> children = reproductor.reproduct( structure, mattingPool );
				
		//Mutation operator
		children = mutator.mutate( structure, children );
		
		//Replacement operator
		List<IndividualTree> next = replacement.replace( generation.getEvaluatedIndividuals(), children );
		
		Generation<IndividualTree> nextGeneration = new Generation<>( next );
		
		return nextGeneration;
	}
	
	private Generation<IndividualTree> createInitialGeneration(){
		List<IndividualTree> list = new ArrayList<>();
		for ( int i = 0; i < elements; i++){
			list.add( init.create() );
		}

		return new Generation<>( list );
	}

}