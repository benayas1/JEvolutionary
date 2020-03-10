package org.benayas.jevolutionary.gggp.concurrent;

import org.benayas.jevolutionary.gggp.grammar.IGrammarElement;

public class TreeNodeConcurrent implements Cloneable{
	private IGrammarElement e;
	private TreeNodeConcurrent parent;
	private int level;
	private int position;
	
	public TreeNodeConcurrent( IGrammarElement e, int level, TreeNodeConcurrent parent, int position ){
		this.e = e;
		this.level = level;
		this.parent = parent;
		this.position = position;
	}
	
	public void setLevel( int level ) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
	
	public TreeNodeConcurrent getParent() {
		return parent;
	}
	
	public IGrammarElement getElement() {
		return e;
	}

	
	public void setParent( TreeNodeConcurrent parent ) {
		this.parent = parent;
	}

	public String toString() {
		return "Level: " + level + " Parent: " + parent + " Element: " + e.toString();
	}

	@Override
	public TreeNodeConcurrent clone(){
		TreeNodeConcurrent node = new TreeNodeConcurrent( this.e, this.level, this.parent, this.position );
		return node;
	}
	
	public static void main(String[] args) {
		/*
		 * TreeNode root = new TreeNode( new NonTerminalElement("1"), 0, );
		 * 
		 * root.set( new TreeNode( new NonTerminalElement("2"), 1 ), new TreeNode( new
		 * NonTerminalElement("3"), 1 ), new TreeNode( new NonTerminalElement("4"), 1
		 * ));
		 * 
		 * TreeNode node = root.getChildren().get( 0 ); node.set( new TreeNode( new
		 * TerminalElement("5"), 2 ), new TreeNode( new NonTerminalElement("6"), 2 ) );
		 * 
		 * node = node.getChildren().get( 1 ); node.set( new TreeNode( new
		 * TerminalElement("7"), 3 ), new TreeNode( new TerminalElement("8"), 3 ) );
		 * 
		 * node = root.getChildren().get( 1 ); node.set( new TreeNode( new
		 * TerminalElement("9"), 2 ) );
		 * 
		 * node = root.getChildren().get( 2 ); node.set( new TreeNode( new
		 * NonTerminalElement("10"), 2 ), new TreeNode( new TerminalElement("12"), 2 )
		 * );
		 * 
		 * node = node.getChildren().get( 0 ); node.set( new TreeNode( new
		 * NonTerminalElement("11"), 3 ) );
		 * 
		 * node = node.getChildren().get( 0 ); node.set( new TreeNode( new
		 * NonTerminalElement("13"), 4 ), new TreeNode( new NonTerminalElement("14"), 4
		 * ), new TreeNode( new NonTerminalElement("15"), 4 ) );
		 * 
		 * TreeNode node1 = node.getChildren().get( 0 ); node1.set( new TreeNode( new
		 * TerminalElement("16"), 5 ) );
		 * 
		 * node1 = node.getChildren().get( 1 ); node1.set( new TreeNode( new
		 * TerminalElement("17"), 5 ) );
		 * 
		 * node1 = node.getChildren().get( 2 ); node1.set( new TreeNode( new
		 * TerminalElement("18"), 5 ) );
		 * 
		 * List<Object> values = root.values(); values.forEach( v -> System.out.println(
		 * v ) );
		 * 
		 * System.out.println( "" );
		 * 
		 * List<Object> values2 = root.valuesLoop(); values2.forEach( v ->
		 * System.out.println( v ) );
		 * 
		 * System.out.println( root.count(e -> e.getElement() instanceof
		 * NonTerminalElement ) ); System.out.println( root.countLoop(e ->
		 * e.getElement() instanceof NonTerminalElement ) );
		 */
	}
}