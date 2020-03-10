package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.gggp.IndividualTreeRecursive;
import org.benayas.jevolutionary.gggp.TreeNode;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.gggp.grammar.Rule;
import org.benayas.jevolutionary.util.Log;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGen;


/**
 * @author alberto
 *	Base class for operators for GGGP. This class provides common methods based on grammar depth, level, searching and generation of elements
 */
public abstract class GrammarBased {

	private int find;
	private int current = -1;
	protected GrammarDepth grammarDepth;
	
	public GrammarBased( GrammarDepth grammarDepth ) {
		this.grammarDepth = grammarDepth;
	}
	
	protected Pair<Pair<TreeNode, TreeNode>,IGrammarElement> select( IndividualTreeRecursive a ) {
		TreeNode root = a.getRoot();
		
		//Get a random non terminal node node and its parent node
		Pair<TreeNode, TreeNode> pair = random( root, e -> e.getElement() instanceof NonTerminalElement );
		
		//Find elements and rebuild rule from parent
		List<IGrammarElement> elements = new ArrayList<>();
		for ( TreeNode n : pair.getL().getChildren() ){
			elements.add( n.getElement() );
		}
		Rule ruleAux = new Rule( elements );
		
		//Look for the ordinal of the rule within the rule set		
		final int position = ruleAux.indexOf( pair.getR().getElement() );

		
		//Remove rules with less elements than ruleAux
		List<Rule> rules = ((NonTerminalElement)pair.getL().getElement()).getRuleSet();
		List<Rule> rulesFiltered = rules.stream().filter( r -> r.size() == ruleAux.size() ).collect( Collectors.toList() );//Pick only those of same length
		List<Rule> rulesReduced = new ArrayList<>();
		for ( Rule rl : rulesFiltered ){
			boolean valid = true;
			for ( int i = 0; i < rl.size() && valid; i++ ){
				if ( i == position ){
					valid = rl.get(i) == ruleAux.get(i);
				}
			}
			if ( valid )
				rulesReduced.add( rl );
		}
		
		//Get single elements from the remaining rules, in the position of the original node
		elements = rulesReduced.stream().map( r -> r.get( position ) ).collect( Collectors.toList() );
		IGrammarElement target = elements.get( RandomGen.get().getInt( elements.size() ) );
		
		//return data
		return new Pair<Pair<TreeNode, TreeNode>,IGrammarElement>( pair, target );
	}
	
	
	/**
	 * Starting from a given node, appends a branch based on the current grammar to it
	 * @param node root node
	 * @param cs level of the node
	 * @param depth max depth
	 */
	protected void developBranch( TreeNode node, int cs, int depth ){		
		
		if ( node.getElement() instanceof NonTerminalElement ) {
			
			NonTerminalElement element = (NonTerminalElement)node.getElement();
			
			//Pick a rule from the candidates
			List<Rule> candidates = element.getRuleSet().stream().filter( r -> grammarDepth.getDepth( r ) + cs <= depth ).collect( Collectors.toList() );
			
			Rule r = null;
			try {//TODO este try esta puesto para entender cuando da el error
				r = candidates.get( RandomGen.get().getInt( candidates.size() ) );
			}catch(Exception e) {
				Log.error( "Cuentas filtro: " + candidates.size() + " + " + cs + " <= " + depth );
				Log.error( "Element: " + element.getLabel() + " \n" );
				List<Rule> rules = element.getRuleSet();
				Log.error( "Rules: " + rules.size() + " \n" );

				rules.stream().forEach( ru -> Log.error( "Rule " + grammarDepth.getDepth( ru ) + " \n") );
				Log.error( node.toString() );
				
				e.printStackTrace();
				System.exit(1);
			}
				
			for ( IGrammarElement e : r ){
				//Create node
				TreeNode n = new TreeNode( e, cs + 1 );
				//If this is not terminal, keep developing the tree
				if ( e instanceof NonTerminalElement )
					developBranch( n, cs + 1, depth );
				node.set( n );
			}	
		}
	}
	
	
	/**
	 * Finds a pair of <parent,child> that matches predicate p, and is found on a random position
	 * @param root
	 * @param p
	 * @return
	 */
	protected Pair<TreeNode, TreeNode> random( TreeNode root, Predicate<TreeNode> p ){
		//Get a random number
		int count = root.countLoop( p );
		
		//If no element meets the condition, return null
		if ( count == 0 )
			return null;
		
		//Initialize internal variables
		find = RandomGen.get().getInt( count );
		
		current = -1;
		//Find the pair on position "find"
		return recursiveFind( root, p );
	}
	
	/**
	 * This method does the recursive step in the find method. It looks for the N element
	 * matching the predicate p. N is stored in attribute current, and attribute find stores the
	 * N-element position to be found
	 * @param parent
	 * @param p
	 * @return A pair of parent and element found
	 */
	private Pair<TreeNode, TreeNode> recursiveFind( TreeNode parent, Predicate<TreeNode> p ){
		//Get children
		List<TreeNode> children = parent.getChildren();
		
		//For each children, look for 
		for ( TreeNode n : children ){
			
			// Stop the search if the condition is not met for this child
			if ( p.test( n ) ){
				current++;
				if ( current == find ){
					return new Pair<TreeNode,TreeNode>( parent, n ); //If found, return parent on the left side, and node on the right side
				}
			}
			Pair<TreeNode, TreeNode> ret = recursiveFind( n, p );//If not found, keep searching
			if ( ret != null )
				return ret;				
			
		}
		//If nothing is found in this branch
		return null; 
	}
	
	protected int fixLevels( TreeNode node, int level ) {
		
		int maxLevel = level;
		
		//Set the level to the node
		node.setLevel( level );
		
		//Get children
		List<TreeNode> children = node.getChildren();
				
		//For each children, adjust the level
		for ( TreeNode n : children )					
			maxLevel = Math.max( fixLevels( n, level + 1), maxLevel );									
		
		return maxLevel;
	}
	
	protected int maxLevels( TreeNode node ) {
		
		int maxLevel = node.getLevel();
						
		//For each children, adjust the level
		for ( TreeNode n : node.getChildren() )					
			maxLevel = Math.max( maxLevels( n ), maxLevel );									
		
		return maxLevel;
	} 
	
}
