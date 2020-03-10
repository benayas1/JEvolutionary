package org.benayas.jevolutionary.operators.mutation;

import java.util.List;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.gggp.concurrent.IndividualTreeConcurrent;
import org.benayas.jevolutionary.gggp.concurrent.TreeNodeConcurrent;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammar;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.operators.cross.GrammarBasedConcurrent;
import org.benayas.jevolutionary.util.RandomGenEnum;

public class GBMConcurrent extends GrammarBasedConcurrent implements IMutation<IndividualTreeConcurrent,IGrammar> {

	private double mutationRate;

	public GBMConcurrent( GrammarDepth grammarDepth, double mutationRate ) {
		super(grammarDepth);
		this.mutationRate = mutationRate;
	}

	@Override
	public List<IndividualTreeConcurrent> mutate(IGrammar structure, List<IndividualTreeConcurrent> population) {
		List<IndividualTreeConcurrent> output  = population.parallelStream().map( i -> mutation( structure, i ) ).collect( Collectors.toList() );
		
		return output;
	}
	
	private IndividualTreeConcurrent mutation( IGrammar structure, IndividualTreeConcurrent i ) {	
		if ( RandomGenEnum.INSTANCE.getDouble() < mutationRate ){
			//Select a node to be mutated
			TreeNodeConcurrent node = i.random( e -> e.getElement() instanceof NonTerminalElement );
			int minRuleDepth = ( (NonTerminalElement)node.getElement() ).getRuleSet().stream().mapToInt( e -> grammarDepth.getDepth( e ) ).min().getAsInt();
			if ( node.getLevel() + minRuleDepth <= grammarDepth.getMaxDepth() ) {
				i.remove( node, true );
				developBranch( i, node, grammarDepth.getMaxDepth() ); 
			}				
		}	
		return i;
	}
}