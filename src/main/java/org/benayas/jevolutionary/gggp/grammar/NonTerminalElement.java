package org.benayas.jevolutionary.gggp.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NonTerminalElement implements IGrammarElement{
	
	private List<Rule> rules = new ArrayList<>();
	private String label;
	
	public String getLabel() {
		return label;
	}

	public NonTerminalElement( String s ) {
		this.label = s;
	}
	
	public NonTerminalElement( List<Rule> set ) {
		rules.addAll( set );	
	}
	
	public NonTerminalElement( Rule... set ) {
		this( Arrays.asList( set ) );		
	}

	public List<Rule> getRuleSet() {
		return rules;
	}
	
	public void setRules( List<Rule> set ) {
		rules.addAll( set );
	}
	
	public void setRules( Rule... set ) {
		setRules( Arrays.asList( set ) );		
	}
	
	public String toString() {
		return "Label:" + label + " Rules: " + rules.size();
	}
}