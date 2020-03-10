package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.gggp.IndividualTreeRecursive;
import org.benayas.jevolutionary.gggp.TreeNode;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammar;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGen;

public class GBC extends GrammarBased implements ICross<IndividualTreeRecursive, IGrammar>{
	
	private double percentage;
	
	public GBC(GrammarDepth grammarDepth, double perc) {
		super( grammarDepth );
		this.percentage = perc;
	}
	
	@Override
	public List<IndividualTreeRecursive> reproduct(IGrammar structure, List<IndividualTreeRecursive> mattingPool ) {
		List<List<IndividualTreeRecursive>> families = matting( mattingPool );
		
		List<IndividualTreeRecursive> children = new ArrayList<>();
		
		families.stream().filter( f -> RandomGen.get().getDouble() < percentage ).forEach( f -> children.addAll( cross( f.get(0), f.get(1) ) ) );
				
		return children;
	}
	
	public List<IndividualTreeRecursive> cross( IndividualTreeRecursive indA, IndividualTreeRecursive indB ){
		
		IndividualTreeRecursive a = indA.clone();
		IndividualTreeRecursive b = indB.clone();
		
		//Select a node to be crossed
		Pair<Pair<TreeNode, TreeNode>, IGrammarElement> data = select( a );
		IGrammarElement target = data.getR(); //Used to pick an equivalent node from tree B
		Pair<TreeNode, TreeNode> pairA = data.getL();
		
		//Find within the second parent, pick another node of the matching type
		Pair<TreeNode, TreeNode> pairB = random( b.getRoot(), e -> ( e.getElement() instanceof NonTerminalElement && e.getElement() ==  target ) );
		if ( pairB != null ) {	
			//Switch trees
			//Get node from A that is going to B
			TreeNode toB = pairA.getR();
			//Adjust its levels, according to its new parent
			fixLevels( toB, pairB.getL().getLevel() + 1 );//set new level to branch B, reading from its new parent node
			
			//Get node from B that is going to A
			TreeNode toA = pairB.getR();
			//Adjust its levels, according to its new parent
			fixLevels( toA, pairA.getL().getLevel() + 1 );//set new level, reading from its new parent node
			
			//Set in A the node coming from B
			pairA.getL().set( pairA.getL().getChildren().indexOf( pairA.getR() ), toA );
			pairB.getL().set( pairB.getL().getChildren().indexOf( pairB.getR() ), toB );		
		}
		
		//Return data
		List<IndividualTreeRecursive> children = new ArrayList<>();
		children.add( a );
		children.add( b );	
		return children;
	}

	protected List<List<IndividualTreeRecursive>> matting( List<IndividualTreeRecursive> mattingPool ){
		List<List<IndividualTreeRecursive>> families = new ArrayList<>();		
		
		for ( int i = 0; i <= mattingPool.size() - 2 ; i = i + 2 ){
			List<IndividualTreeRecursive> parents = new ArrayList<>();
			//Get the parents
			for (int j = 0; j < 2; j++ )
				parents.add( mattingPool.get( i + j ) );
			
			families.add( parents );			
		}
		
		return families;
	}
}