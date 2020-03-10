package org.benayas.jevolutionary.gggp.concurrent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.gggp.IndividualTree;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.TerminalElement;
import org.benayas.jevolutionary.util.RandomGenEnum;

public class IndividualTreeConcurrent extends IndividualTree implements Cloneable{

	private TreeNodeConcurrent root;
	private List<TreeNodeConcurrent> nodes = new ArrayList<>();
		
	public IndividualTreeConcurrent() {
		super();
	}
	
	public IndividualTreeConcurrent( IGrammarElement element ) {
		root = new TreeNodeConcurrent( element, 0, null, 0 );
	}
	
	public TreeNodeConcurrent getRoot(){
		return root;
	}
	
	public int count( Predicate<TreeNodeConcurrent> p ){
		return (int) nodes.stream().filter( n -> p.test( n ) ).count();
	} 
	
	public TreeNodeConcurrent random( Predicate<TreeNodeConcurrent> p ) {
		List<TreeNodeConcurrent> valids = nodes.stream().filter( n -> p.test( n ) ).collect( Collectors.toList() );
		if ( valids.isEmpty() )
			return null;
		TreeNodeConcurrent selected = valids.get( RandomGenEnum.INSTANCE.getInt( valids.size() ) );
		return selected;		
	}
	
	public List<TreeNodeConcurrent> getChildren( TreeNodeConcurrent parent ){
		return nodes.stream().filter( n -> n.getParent() == parent ).collect( Collectors.toList() );
	}
	
	
	public void insertNode( TreeNodeConcurrent node ) {
		nodes.add( node );
	}
	
	public void remove( TreeNodeConcurrent node, boolean onlyChildren ) {		
		Queue<TreeNodeConcurrent> toRemove = new LinkedList<>();
		
		if ( onlyChildren ) {
			toRemove.addAll( getChildren( node ) );
		}else {
			toRemove.add( node );
		}
		
		while( !toRemove.isEmpty() ) {
			TreeNodeConcurrent removing = toRemove.poll(); // Get the first element
			toRemove.addAll( getChildren( removing ) ); // Add its children
			nodes.remove( removing ); // Remove the element
		}		
	}
	
	public List<TreeNodeConcurrent> getBranch( TreeNodeConcurrent node ){
		List<TreeNodeConcurrent> branch = new ArrayList<TreeNodeConcurrent>();
		branch.add( node );
		
		Queue<TreeNodeConcurrent> toRead = new LinkedList<>();
		toRead.add( node );
		
		while( !toRead.isEmpty() ) {
			TreeNodeConcurrent reading = toRead.poll(); // Get the first element
			List<TreeNodeConcurrent> children = getChildren( reading );
			toRead.addAll( children ); // Add its children
			branch.addAll( children );
			nodes.remove( reading ); // Remove the element
		}		
		
		return branch;
	}
	
	public void insertBranch( List<TreeNodeConcurrent> branch ) {
		branch.stream().forEachOrdered( n -> insertNode( n ) );
	}
	
	public void fixLevels( TreeNodeConcurrent node, int level ) {		
		//Set the level to the node
		node.setLevel( level );
				
		//For each children, adjust the level
		for ( TreeNodeConcurrent n : getChildren( node ) )					
			fixLevels( n, level + 1 );									
	}
		
	@Override
	public Object[] values(){
		List<Object> list = nodes.stream().filter( e -> e.getElement() instanceof TerminalElement )
				                                      .map( e -> ((TerminalElement)e.getElement()).getObj() )                                      
				                                      .collect( Collectors.toList() );
		Object[] array = list.toArray();
		return array;				
	}
	
	@Override
	public boolean equals(Object o) {
		IndividualTreeConcurrent ind = (IndividualTreeConcurrent) o;
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
	public IndividualTreeConcurrent clone(){
		// Create a new instance
		IndividualTreeConcurrent a = new IndividualTreeConcurrent();
		
		// Clone root and nodes
		a.root = this.root.clone();
		
		// Map values and insert cloned nodes into new individual
		Map<TreeNodeConcurrent, TreeNodeConcurrent> map = this.nodes.stream().collect( Collectors.toMap( n -> n, n -> n.clone() ) );
		map.values().stream().forEach( n -> a.insertNode( n ) );
		
		// Put root in this map
		map.put(this.root, a.getRoot() );
		
		// Change parent of each node in the new individual
		for ( int i = 0; i < a.nodes.size(); i++ ) {
			a.nodes.get( i ).setParent( map.get( a.nodes.get( i ).getParent() ) );
		}	
		
		return a;
	}
}