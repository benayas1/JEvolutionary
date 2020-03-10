package org.benayas.jevolutionary.gggp.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.benayas.jevolutionary.gggp.concurrent.GBIMConcurrent;
import org.benayas.jevolutionary.gggp.concurrent.IndividualTreeConcurrent;
import org.benayas.jevolutionary.operators.cross.GBCConcurrent;
import org.benayas.jevolutionary.operators.mutation.GBMConcurrent;

public class Grammar implements IGrammar{

	private NonTerminalElement axiom;
	private List<NonTerminalElement> nonTerminals;
	private List<TerminalElement> terminals;
	
	public Grammar( NonTerminalElement axiom, List<TerminalElement> terminals, List<NonTerminalElement> nonTerminals ) {
		this.axiom = axiom;
		this.terminals = terminals;
		this.nonTerminals = nonTerminals;	 
	}
	
	@Override
	public NonTerminalElement getAxiom() {
		return axiom;
	}
	
	public List<NonTerminalElement> getNonTerminals() {
		return nonTerminals;
	}


	public List<TerminalElement> getTerminals() {
		return terminals;
	}

	
	public static void main(String[] args) {	
		
		long time = System.currentTimeMillis();
/*		for ( int i = 0; i < 1000; i++ ) {
			TerminalElement t0 = new TerminalElement(0);
			TerminalElement t1 = new TerminalElement(1);
			TerminalElement t2 = new TerminalElement(2);
			TerminalElement t3 = new TerminalElement(3);
			TerminalElement t4 = new TerminalElement(4);
			TerminalElement t5 = new TerminalElement(5);
			TerminalElement t6 = new TerminalElement(6);
			TerminalElement t7 = new TerminalElement(7);
			TerminalElement t8 = new TerminalElement(8);
			TerminalElement t9 = new TerminalElement(9);
			TerminalElement tplus = new TerminalElement("+");
			TerminalElement tminus = new TerminalElement("-");
			TerminalElement tequal = new TerminalElement("=");
			NonTerminalElement n = new NonTerminalElement("n");
			NonTerminalElement f = new NonTerminalElement("f");
			NonTerminalElement e = new NonTerminalElement("e");
			NonTerminalElement s = new NonTerminalElement("s");					
			e.setRules( new Rule(e,tplus,e), new Rule(e,tminus,e), new Rule(f,tplus,e), new Rule(f,tminus,e), new Rule(n) );
			s.setRules( new Rule(e,tequal,n) );
			f.setRules( new Rule(n) );
			n.setRules( new Rule(t0), new Rule(t1), new Rule(t2), new Rule(t3), new Rule(t4), new Rule(t5), new Rule(t6), new Rule(t7), new Rule(t8), new Rule(t9) );
			//n.setRules( new Rule(t0), new Rule(t1) );
	
			
			List<NonTerminalElement> nt = new ArrayList<>();
			List<TerminalElement> t = new ArrayList<>();
			
			t.addAll(Arrays.asList(new TerminalElement[] {t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,tplus,tminus,tequal}));
			//t.addAll(Arrays.asList(new TerminalElement[] {t0,t1}));
	
			nt.addAll(Arrays.asList(new NonTerminalElement[] {n,f,e,s}));
			
			IGrammar grammar = new Grammar( s, t, nt );
			
			GrammarDepth grammarDepth = new GrammarDepth( grammar, 6 );
			
			GBIM init = new GBIM(grammarDepth);
			
			List<IndividualTreeRecursive> population = new ArrayList<>();
			for (int in = 0; in < 10000; in++) {
				population.add( init.create() );	
			}
			
			IndividualTree ind = IndividualTree.create( init );	
			for ( Object o : ind.values() ) {
				System.out.print( o.toString() + " " );			
			}
			System.out.println();
			
			IndividualTree ind2 = IndividualTree.create( init );		
			for ( Object o : ind2.values() ) {
				System.out.print( o.toString() + " " );
			}
			System.out.println();
			
			GBC cross = new GBC(grammarDepth, 1);
			
			List<IndividualTreeRecursive> changed = cross.reproduct(grammar, population);
			
			for ( Object o : changed.get(0).values() ) 
				System.out.print( o.toString() + " " );
			
			System.out.println();
			
			for ( Object o : changed.get(1).values() ) 
				System.out.print( o.toString() + " " );
			
			System.out.println();
						
			GBM mutation = new GBM( grammarDepth, 1 );
			changed = mutation.mutate(grammar, changed);
			for ( Object o : changed.get(0).values() ) 
				System.out.print( o.toString() + " " );
			
			//System.out.println( changed.size() );
		}
		System.out.println( "Grammar antigua " + ( System.currentTimeMillis() - time ) );*/

		
		time = System.currentTimeMillis();
		for ( int i = 0; i < 1000; i++ ) {
			TerminalElement t0 = new TerminalElement(0);
			TerminalElement t1 = new TerminalElement(1);
			TerminalElement t2 = new TerminalElement(2);
			TerminalElement t3 = new TerminalElement(3);
			TerminalElement t4 = new TerminalElement(4);
			TerminalElement t5 = new TerminalElement(5);
			TerminalElement t6 = new TerminalElement(6);
			TerminalElement t7 = new TerminalElement(7);
			TerminalElement t8 = new TerminalElement(8);
			TerminalElement t9 = new TerminalElement(9);
			TerminalElement tplus = new TerminalElement("+");
			TerminalElement tminus = new TerminalElement("-");
			TerminalElement tequal = new TerminalElement("=");
			NonTerminalElement n = new NonTerminalElement("n");
			NonTerminalElement f = new NonTerminalElement("f");
			NonTerminalElement e = new NonTerminalElement("e");
			NonTerminalElement s = new NonTerminalElement("s");					
			e.setRules( new Rule(e,tplus,e), new Rule(e,tminus,e), new Rule(f,tplus,e), new Rule(e,tminus,f), new Rule(n), new Rule(e,e) );
			s.setRules( new Rule(e,tequal,n) );
			f.setRules( new Rule(n) );
			n.setRules( new Rule(t0), new Rule(t1), new Rule(t2), new Rule(t3), new Rule(t4), new Rule(t5), new Rule(t6), new Rule(t7), new Rule(t8), new Rule(t9) );
			//n.setRules( new Rule(t0), new Rule(t1) );
	
			
			List<NonTerminalElement> nt = new ArrayList<>();
			List<TerminalElement> t = new ArrayList<>();
			
			t.addAll(Arrays.asList(new TerminalElement[] {t0,t1,t2,t3,t4,t5,t6,t7,t8,t9,tplus,tminus,tequal}));
			//t.addAll(Arrays.asList(new TerminalElement[] {t0,t1}));
	
			nt.addAll(Arrays.asList(new NonTerminalElement[] {n,f,e,s}));
			
			IGrammar grammar = new Grammar( s, t, nt );
			
			GrammarDepth grammarDepth = new GrammarDepth( grammar, 6 );
			
			GBIMConcurrent init = new GBIMConcurrent(grammarDepth);
			
			List<IndividualTreeConcurrent> population = new ArrayList<>();
			for (int in = 0; in < 10000; in++) {
				population.add( init.create() );	
			}
			
			
			GBCConcurrent cross = new GBCConcurrent(grammarDepth, 1);
			
			List<IndividualTreeConcurrent> changed = cross.reproduct(grammar, population);
			
						
			GBMConcurrent mutation = new GBMConcurrent( grammarDepth, 1 );
			changed = mutation.mutate(grammar, changed);

		}
		System.out.println( "Grammar concurrent " + ( System.currentTimeMillis() - time ) );
	}

}
