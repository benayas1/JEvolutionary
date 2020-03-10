package org.benayas.jevolutionary.gggp.concurrent;

import java.util.List;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.gggp.grammar.Rule;
import org.benayas.jevolutionary.gggp.init.ITreeInitMethod;
import org.benayas.jevolutionary.operators.cross.GrammarBasedConcurrent;
import org.benayas.jevolutionary.util.RandomGen;

public class GBIMConcurrent extends GrammarBasedConcurrent implements ITreeInitMethod<IndividualTreeConcurrent>{
	
	public GBIMConcurrent( GrammarDepth grammarDepth ){
		super( grammarDepth );
	}
	
	@Override
	public IndividualTreeConcurrent create(){
		//Get Axiom
		NonTerminalElement s = grammarDepth.getGrammar().getAxiom();
		
		//Generate a random depth between axiom's depth (min) and maxDepth
		int depth = grammarDepth.getDepth( s ) + RandomGen.get().getInt( grammarDepth.getMaxDepth() + 1 - grammarDepth.getDepth( s ) );
		int cs = 0;
		
		//Create individual from axiom
		IndividualTreeConcurrent individual = new IndividualTreeConcurrent( s );
		
		//Get axiom as root
		TreeNodeConcurrent root = individual.getRoot();
					
		//Pick a rule from the candidates
		List<Rule> candidates = s.getRuleSet().stream().filter( r -> grammarDepth.getDepth( r ) + cs <= depth ).collect( Collectors.toList() );
		Rule r = candidates.get( RandomGen.get().getInt( candidates.size() ) );
			
		int pos = 0;
		for ( IGrammarElement e : r ){
			//Create node
			TreeNodeConcurrent n = new TreeNodeConcurrent( e, cs + 1, root, pos++ );
			
			//Insert the data in the node
			individual.insertNode( n );
			
			//If this is not terminal, keep developing the tree
			developBranch( individual, n, depth );		
		}	

		//Return individual with a tree inside
		return individual;
	}
}