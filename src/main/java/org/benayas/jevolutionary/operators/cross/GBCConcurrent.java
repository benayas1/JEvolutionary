package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.benayas.jevolutionary.gggp.concurrent.IndividualTreeConcurrent;
import org.benayas.jevolutionary.gggp.concurrent.TreeNodeConcurrent;
import org.benayas.jevolutionary.gggp.grammar.GrammarDepth;
import org.benayas.jevolutionary.gggp.grammar.IGrammar;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGenEnum;

public class GBCConcurrent extends GrammarBasedConcurrent implements ICross<IndividualTreeConcurrent, IGrammar>{
	
	private double percentage;
	
	public GBCConcurrent(GrammarDepth grammarDepth, double perc) {
		super( grammarDepth );
		this.percentage = perc;
	}
	
	@Override
	public List<IndividualTreeConcurrent> reproduct(IGrammar structure, List<IndividualTreeConcurrent> mattingPool ) {
		List<List<IndividualTreeConcurrent>> families = matting( mattingPool );
		
		Vector<IndividualTreeConcurrent> children = new Vector<>();
		
		families.parallelStream().filter( f -> RandomGenEnum.INSTANCE.getDouble() < percentage ).forEach( f -> children.addAll( cross( f.get(0), f.get(1) ) ) );
				
		return children;
	}
	
	public List<IndividualTreeConcurrent> cross( IndividualTreeConcurrent indA, IndividualTreeConcurrent indB ){
		
		IndividualTreeConcurrent a = indA.clone();
		IndividualTreeConcurrent b = indB.clone();		
		
		//Select a node to be crossed
		Pair< TreeNodeConcurrent, IGrammarElement> data = select( a );
		IGrammarElement target = data.getR(); //Used to pick an equivalent node from tree B
		TreeNodeConcurrent toB = data.getL();
		
		//Find, within the second parent, pick another node of the matching type
		TreeNodeConcurrent toA = b.random( e -> ( e.getElement() instanceof NonTerminalElement && e.getElement() ==  target ) );
		if ( toA != null ) {	
			//Switch trees
			
			TreeNodeConcurrent aux = toB.clone();
			
			//Get node from A that is going to B
			//Adjust its levels, according to its new parent
			toB.setPosition( toA.getPosition() );
			a.fixLevels( toB, toA.getLevel() );//set new level to branch B, reading from its new parent node
			toB.setParent( toA.getParent() ); //Assign new parent
			List<TreeNodeConcurrent> branchToB = a.getBranch( toB );// Get the whole branch to transfer, this also removes the branch
			//a.remove( toB, false ); //Remove the branch from the original tree			
			
			//Get node from B that is going to A
			//Adjust its levels, according to its new parent
			toA.setPosition( aux.getPosition() );
			b.fixLevels( toA, aux.getLevel() );//set new level, reading from its new parent node										
			toA.setParent( aux.getParent() ); //Assign new parents and get branches	
			List<TreeNodeConcurrent> branchToA = b.getBranch( toA );// Get the whole branch to transfer, this also removes the branch
			//b.remove( toA, false );			
			
			b.insertBranch( branchToB ); // Insert the branch into the new tree
			a.insertBranch( branchToA );
		}
		
		//Return data
		List<IndividualTreeConcurrent> children = new ArrayList<>();
		children.add( a );
		children.add( b );	
		return children;
	}

	protected List<List<IndividualTreeConcurrent>> matting( List<IndividualTreeConcurrent> mattingPool ){
		List<List<IndividualTreeConcurrent>> families = new ArrayList<>();		
		
		for ( int i = 0; i <= mattingPool.size() - 2 ; i = i + 2 ){
			List<IndividualTreeConcurrent> parents = new ArrayList<>();
			//Get the parents
			for (int j = 0; j < 2; j++ )
				parents.add( mattingPool.get( i + j ) );
			
			families.add( parents );			
		}
		
		return families;
	}
}