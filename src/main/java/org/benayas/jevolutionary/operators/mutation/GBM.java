package org.benayas.jevolutionary.operators.mutation;

import java.util.List;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.gggp.IndividualTreeRecursive;
import org.benayas.jevolutionary.gggp.TreeNode;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammar;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.operators.cross.GrammarBased;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGen;

public class GBM extends GrammarBased implements IMutation<IndividualTreeRecursive,IGrammar> {

	private double mutationRate;

	public GBM( GrammarDepth grammarDepth, double mutationRate ) {
		super(grammarDepth);
		this.mutationRate = mutationRate;
	}

	@Override
	public List<IndividualTreeRecursive> mutate(IGrammar structure, List<IndividualTreeRecursive> population) {
		List<IndividualTreeRecursive> output  = population.stream().map( i -> mutation( structure, i ) ).collect( Collectors.toList() );
		
		return output;
	}
	
	private IndividualTreeRecursive mutation( IGrammar structure, IndividualTreeRecursive i ) {	
		if ( RandomGen.get().getDouble() < mutationRate ){
			//Select a node to be mutated
			Pair<TreeNode, TreeNode> data = random( i.getRoot(), e -> e.getElement() instanceof NonTerminalElement );
			TreeNode node = data.getR();
			int minRuleDepth = ( (NonTerminalElement)node.getElement() ).getRuleSet().stream().mapToInt( e -> grammarDepth.getDepth( e ) ).min().getAsInt();
			if ( node.getLevel() + minRuleDepth <= grammarDepth.getMaxDepth() ) {
				node.removeChildren();
				developBranch( node, node.getLevel(), grammarDepth.getMaxDepth() ); 
			}				
		}	
		return i;
	}
}