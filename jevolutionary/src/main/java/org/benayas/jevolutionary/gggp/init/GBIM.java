package org.benayas.jevolutionary.gggp.init;

import java.util.List;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.gggp.IndividualTreeRecursive;
import org.benayas.jevolutionary.gggp.TreeNode;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.gggp.grammar.Rule;
import org.benayas.jevolutionary.operators.cross.GrammarBased;
import org.benayas.jevolutionary.util.RandomGen;

public class GBIM extends GrammarBased implements ITreeInitMethod<IndividualTreeRecursive>{
	
	public GBIM( GrammarDepth grammarDepth ){
		super( grammarDepth );
	}
	
	@Override
	public IndividualTreeRecursive create(){
		//Get Axiom
		NonTerminalElement s = grammarDepth.getGrammar().getAxiom();
		
		//Generate a random depth between axiom's depth (min) and maxDepth
		int depth = grammarDepth.getDepth( s ) + RandomGen.get().getInt( grammarDepth.getMaxDepth() + 1 - grammarDepth.getDepth( s ) );
		int cs = 0;
		
		//Create individual from axiom
		IndividualTreeRecursive individual = new IndividualTreeRecursive( s );
		
		//Get axiom as root
		TreeNode root = individual.getRoot();
					
		//Pick a rule from the candidates
		List<Rule> candidates = s.getRuleSet().stream().filter( r -> grammarDepth.getDepth( r ) + cs <= depth ).collect( Collectors.toList() );
		Rule r = candidates.get( RandomGen.get().getInt( candidates.size() ) );
			
		for ( IGrammarElement e : r ){
			//Create node
			TreeNode n = new TreeNode( e, cs + 1 );
			
			//If this is not terminal, keep developing the tree
			developBranch( n, cs + 1, depth );
			
			//Insert the data in the node
			root.set( n );
		}	

		//Return individual with a tree inside
		return individual;
	}

}