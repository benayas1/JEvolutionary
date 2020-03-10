package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.gggp.concurrent.IndividualTreeConcurrent;
import org.benayas.jevolutionary.gggp.concurrent.TreeNodeConcurrent;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.gggp.grammar.Rule;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGenEnum;
/**
 * @author alberto
 *	Base class for operators for GGGP. This class provides common methods based on grammar depth, level, searching and generation of elements
 */
public abstract class GrammarBasedConcurrent {

	protected GrammarDepth grammarDepth;
	
	public GrammarBasedConcurrent( GrammarDepth grammarDepth ) {
		this.grammarDepth = grammarDepth;
	}
	
	protected Pair<TreeNodeConcurrent,IGrammarElement> select( IndividualTreeConcurrent a ) {
		
		//Get a random non terminal node node and its parent node
		TreeNodeConcurrent fromA = a.random( e -> e.getElement() instanceof NonTerminalElement );
		
		//Get the number of siblings this node has
		int ruleMembersNumber = a.getChildren( fromA.getParent() ).size();
		
		//Get the position within its siblings this node has	
		final int position = fromA.getPosition();

		
		//Remove rules with less elements than ruleAux
/*		List<Rule> rules = ( (NonTerminalElement)fromA.getParent().getElement() ).getRuleSet();
		List<Rule> rulesFiltered = rules.stream().filter( r -> r.size() == ruleMembersNumber ).collect( Collectors.toList() );//Pick only those of same length
		List<Rule> rulesReduced = new ArrayList<>();
		for ( Rule rl : rulesFiltered ){
			boolean valid = true;
			for ( int i = 0; i < rl.size() && valid; i++ ){
				if ( i == position ){
					valid = rl.get(i) == fromA.getElement();
				}
			}
			if ( valid )
				rulesReduced.add( rl );
		}*/
		
		//Remove rules with less elements than ruleAux
		List<Rule> rulesReduced = ( (NonTerminalElement)fromA.getParent().getElement() ).getRuleSet().stream()  
														.filter( r -> r.size() == ruleMembersNumber ) //Pick only those of same length
														.filter( r -> r.get( position ) == fromA.getElement() ) // Pick only those whose position element is in the same position
														.collect( Collectors.toList() );
		
		//Get single elements from the remaining rules, in the position of the original node
		List<IGrammarElement> elements = rulesReduced.stream().map( r -> r.get( position ) ).collect( Collectors.toList() );
		IGrammarElement target = elements.get( RandomGenEnum.INSTANCE.getInt( elements.size() ) );
		
		//return data
		return new Pair<TreeNodeConcurrent,IGrammarElement>( fromA, target );
	}
	
	
	/**
	 * Starting from a given node, appends a branch based on the current grammar to it
	 * @param node root node
	 * @param depth max depth
	 */	
	protected void developBranch( IndividualTreeConcurrent a, TreeNodeConcurrent node, int depth ){		
		
		if ( node.getElement() instanceof NonTerminalElement ) {
			
			//Remove its childrens
			a.remove( node, true );
			
			//Create FIFO structure
			Queue<TreeNodeConcurrent> fifo = new ArrayDeque<>();			
			fifo.add( node );
			
			//Process nodes
			while ( !fifo.isEmpty() ) {
				TreeNodeConcurrent n = fifo.poll();
				
				if ( !( n.getElement() instanceof NonTerminalElement ) ) 
					continue;
				
				//Pick a rule from the candidates
				List<Rule> candidates = ((NonTerminalElement)n.getElement()).getRuleSet().stream().filter( r -> grammarDepth.getDepth( r ) + n.getLevel() <= depth ).collect( Collectors.toList() );					
				Rule r = candidates.get( RandomGenEnum.INSTANCE.getInt( candidates.size() ) );
				
				int pos = 0;
				for ( IGrammarElement e : r ){
					//Create node
					TreeNodeConcurrent n2 = new TreeNodeConcurrent( e, n.getLevel() + 1, n, pos++ );
					a.insertNode( n2 );
					
					//If this is not terminal, keep developing the tree
					if ( e instanceof NonTerminalElement )
						fifo.add( n2 );				
				}
			}
		}
	}
	
}
