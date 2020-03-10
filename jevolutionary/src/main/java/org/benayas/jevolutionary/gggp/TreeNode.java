package org.benayas.jevolutionary.gggp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;
import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;
import org.benayas.jevolutionary.gggp.grammar.NonTerminalElement;
import org.benayas.jevolutionary.gggp.grammar.TerminalElement;

public class TreeNode implements Cloneable{
	private IGrammarElement e;
	private List<TreeNode> nodes = new ArrayList<>();
	private int level;
	
	public TreeNode( IGrammarElement e, int level ){
		this.e = e;
		this.level = level;
	}
	
	public void setLevel( int level ) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public TreeNode getNode( int... coordinates ) {
		if ( coordinates.length == 1 ) {
			return nodes.get( coordinates[0] );
		}
		
		return nodes.get( coordinates[0] ).getNode( ArrayUtils.subarray( coordinates, 1, coordinates.length ) );
	}
	
	public IGrammarElement getElement() {
		return e;
	}		
	
	public List<Object> values() { 
		List<Object> list = new ArrayList<>();
		
		if ( e instanceof TerminalElement ) {
			list.add( ((TerminalElement)e).getObj() );
			return list;
		}
		
		for ( TreeNode n : nodes ) {
			list.addAll( n.values() );
		}
		
		return list;
	}
	
	public List<Object> valuesLoop(){
		List<Object> list = new ArrayList<>();
		Stack<TreeNode> stack = new Stack<>();
		Set<TreeNode> used = new HashSet<>();
		TreeNode current = this;
		
		while( current != null || stack.size() > 0 ) {	
			
			while ( current != null ) {
				stack.push( current );
				current = current.getChildren().isEmpty() ? null : current.getChildren().get(0);
			}
			
			/* Current must be NULL at this point */
			current = stack.pop(); 
			
			if ( current.getElement() instanceof TerminalElement ) {
				list.add( ((TerminalElement)current.getElement()).getObj() );
				used.add( current );
				current = null;
			}else{
				Optional<TreeNode> find = current.getChildren().stream().filter( e -> !used.contains( e ) ).findFirst();
				if ( find.isPresent() ) {
					stack.push( current );
					current = find.get();
				}
				else {
					used.add(current);	
					current = null;
				}
			}
						
		}
		return list;				
	}
	
	public void set( TreeNode... children ){
		nodes.addAll( Arrays.asList( children ) );
	}
	
	public void set( int pos, TreeNode n) {
		nodes.set( pos, n);
	}
	
	public TreeNode remove( int index ){
		TreeNode t = nodes.get( index );
		nodes.remove( index );
		return t;
	}
	
	public int depth(){
		return 1 + nodes.stream().mapToInt( n -> n.depth() ).max().getAsInt();
	}
	
	public int count( Predicate<TreeNode> p ){
		return (int) ( nodes.stream().filter( n -> p.test( n ) ).count() + nodes.stream().mapToInt( n -> n.count( p ) ).sum() );
	}
	
	public int countLoop( Predicate<TreeNode> p ) {
		int count = (int) nodes.stream().filter( n -> p.test( n ) ).count();
		
		Stack<TreeNode> stack = new Stack<>();
		stack.addAll( nodes );
		
		Set<TreeNode> used = new HashSet<>();
		used.add( this );
		
		while( !stack.isEmpty() ) {
			TreeNode current = stack.pop();
			if ( !used.contains( current ) ) {
				count += (int) current.getChildren().stream().filter( n -> p.test( n ) ).count();
				used.add( current );
				stack.addAll( current.getChildren() );
			}
		}
		
		return count;
	}

	public List<TreeNode> getChildren() {
		return nodes;
	}
		

	public void removeChildren() {
		nodes = new Vector<>();
	}
	
	public String toString() {
		return "Level: " + level + " Children: " + nodes.size() + " Element: " + e.toString();
	}

	@Override
	public TreeNode clone(){
		TreeNode node = new TreeNode( this.e, this.level );
		for( TreeNode n : this.nodes ) {
			node.nodes.add( n.clone() );
		}
		return node;
	}
	
	public static void main(String[] args) {
		TreeNode root = new TreeNode( new NonTerminalElement("1"), 0 );
		
		root.set( new TreeNode( new NonTerminalElement("2"), 1 ), new TreeNode( new NonTerminalElement("3"), 1 ), new TreeNode( new NonTerminalElement("4"), 1 ));
		
		TreeNode node = root.getChildren().get( 0 );
		node.set( new TreeNode( new TerminalElement("5"), 2 ), new TreeNode( new NonTerminalElement("6"), 2 ) );
		
		node = node.getChildren().get( 1 );
		node.set( new TreeNode( new TerminalElement("7"), 3 ), new TreeNode( new TerminalElement("8"), 3 ) );
		
		node = root.getChildren().get( 1 );
		node.set( new TreeNode( new TerminalElement("9"), 2 ) );
		
		node = root.getChildren().get( 2 );
		node.set( new TreeNode( new NonTerminalElement("10"), 2 ), new TreeNode( new TerminalElement("12"), 2 ) );
		
		node = node.getChildren().get( 0 );
		node.set( new TreeNode( new NonTerminalElement("11"), 3 ) );
		
		node = node.getChildren().get( 0 );
		node.set( new TreeNode( new NonTerminalElement("13"), 4 ), new TreeNode( new NonTerminalElement("14"), 4 ), new TreeNode( new NonTerminalElement("15"), 4 ) );
		
		TreeNode node1 = node.getChildren().get( 0 );
		node1.set( new TreeNode( new TerminalElement("16"), 5 ) );
		
		node1 = node.getChildren().get( 1 );
		node1.set( new TreeNode( new TerminalElement("17"), 5 ) );
		
		node1 = node.getChildren().get( 2 );
		node1.set( new TreeNode( new TerminalElement("18"), 5 ) );
		
		List<Object> values = root.values();
		values.forEach( v -> System.out.println( v ) );
		
		System.out.println( "" );
		
		List<Object> values2 = root.valuesLoop();
		values2.forEach( v -> System.out.println( v ) );
		
		System.out.println( root.count(e -> e.getElement() instanceof NonTerminalElement ) );
		System.out.println( root.countLoop(e -> e.getElement() instanceof NonTerminalElement ) );
		
	}
}
