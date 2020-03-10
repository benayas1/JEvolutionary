package org.benayas.jevolutionary.gggp.grammar;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GrammarDepth {

	private IGrammar grammar;
	private HashMap <Object,Integer> depths;
	private int maxDepth;
	
	public GrammarDepth( IGrammar grammar, int maxDepth ) {
		this.grammar = grammar;
		this.depths = new HashMap<>();
		this.maxDepth = maxDepth;
		
		//Depth = 0 for terminal elements
		grammar.getTerminals().forEach( e -> depths.put(e, 0) );
				
		//Read Non Terminal Elements
		List<NonTerminalElement> nonTerminals = grammar.getNonTerminals();
						
		//Depth = 1 for rules deriving in only terminal elements
		nonTerminals.stream().flatMap( nt -> nt.getRuleSet().stream() ).filter( r -> r.stream().filter( e -> e instanceof TerminalElement ).count() == r.size() ).forEach( r1 -> depths.put( r1, 1 ) );
				
		boolean keepIterating = true;
				
		while( keepIterating ) {
					
			//Add depth of non terminals containing everything they need to be calculated
			nonTerminals.stream().filter(  nt -> !depths.containsKey( nt ) && nt.getRuleSet().stream().filter( r -> depths.containsKey( r ) ).count() == nt.getRuleSet().size() ).forEach( e -> depths.put( e, e.getRuleSet().stream().mapToInt( r -> depths.get( r ) ).min().getAsInt() ) );

			//Add depth of rules containing everything they need to be calculated
			nonTerminals.stream().flatMap( nt -> nt.getRuleSet().stream() ).filter( r -> r.stream().filter( e -> !depths.containsKey( e ) ).count() == 0 ).forEach( r -> depths.put( r, r.stream().mapToInt( e -> depths.get( e ) ).max().getAsInt() + 1 ) );

			//Add depth of non terminals containing everything they need to be calculated
			for ( NonTerminalElement nt : nonTerminals ) {
				//Insert Rules where all non terminals are the same element as the antecedent
				List<Rule> set = nt.getRuleSet().stream().filter( r -> !depths.containsKey( r ) && r.stream().filter( e -> e instanceof NonTerminalElement ).allMatch( e -> depths.containsKey( e ) && e == nt ) ).collect( Collectors.toList() );				
				set.forEach( r -> depths.put( r, depths.get( nt ) + 1 ) );	                                                
						
				//Insert Rules when all non terminals different than the antecedent are known
				set = nt.getRuleSet().stream().filter( r -> !depths.containsKey( r ) && r.stream().filter( e -> e instanceof NonTerminalElement && e != nt ).count() > 0 && r.stream().filter( e -> e instanceof NonTerminalElement && e != nt ).allMatch( e -> depths.containsKey( e ) ) ).collect( Collectors.toList() );				
				set.forEach( r -> depths.put( r, r.stream().filter( e -> e instanceof NonTerminalElement && e != nt ).mapToInt( e -> depths.get( e ) ).max().getAsInt() + 1 ) );	                                                
					
				//Insert Non Terminals where all their rules are known
				if ( !depths.containsKey( nt ) && nt.getRuleSet().stream().filter( r -> r.stream().filter( e -> e instanceof NonTerminalElement && e != nt ).count() > 0 ).allMatch( r -> depths.containsKey( r ) ) )
					depths.put( nt, nt.getRuleSet().stream().filter( r -> r.stream().filter( e -> e instanceof NonTerminalElement && e != nt ).count() > 0 ).mapToInt( r -> depths.get( r ) ).min().getAsInt() );
			}
					
			//Keep iteraring if there are elements to be calculated
			keepIterating = nonTerminals.stream().filter( nt -> !depths.containsKey( nt ) ).count() + nonTerminals.stream().flatMap( nt -> nt.getRuleSet().stream() ).filter( nt -> !depths.containsKey( nt ) ).count() != 0;					
		}
	}
	
	public IGrammar getGrammar() {
		return grammar;
	}
	
	public int getDepth( Object o ) {
		return depths.get( o );
	}

	public int getMaxDepth() {
		return maxDepth;
	}

}
