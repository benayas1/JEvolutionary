package org.benayas.jevolutionary.gggp;

import java.util.List;

import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;


public class IndividualTreeRecursive extends IndividualTree implements Cloneable{

	private TreeNode root;
		
	public IndividualTreeRecursive() {
		super();
	}
	
	public IndividualTreeRecursive( IGrammarElement element ) {
		root = new TreeNode( element, 0 );		
	}
	
	public IGrammarElement getElement( int... coordinates ) {
		return getNode( coordinates ).getElement();
	}
	
	public TreeNode getNode( int... coordinates ) {
		return root.getNode(coordinates);
	}
	
	public TreeNode getRoot(){
		return root;
	}
		
	@Override
	public Object[] values(){
		List<Object> list = root.valuesLoop();
		Object[] array = list.toArray();
		return array;				
	}
	
	@Override
	public boolean equals(Object o) {
		IndividualTreeRecursive ind = (IndividualTreeRecursive) o;
		return this.getId() == ind.getId();
	}
	
	@Override
	public int hashCode() {
		return this.getId() * 47;
	}

	@Override
	public int compareTo(Individual o) {
		return score.compareTo( o.getEvaluation() );
	}
	
	@Override
	public String toString(){
		String val = "";
		Object[] values = values();
		for ( int i = 0; i < values.length; i++ ){
			val = val + " [" + i + "] = " + values[i].toString() + "\n";
		}
		return "ID " + getId() + "\nScore: " + score.toString() + "\nParams:\n" + val;
	}

	@Override
	public IndividualTreeRecursive clone(){
		IndividualTreeRecursive i = new IndividualTreeRecursive();
		i.root = this.root.clone();
		return i;
	}
}