package org.benayas.jevolutionary.gggp.grammar;

import java.util.List;

import org.benayas.jevolutionary.IBaseStructure;

public interface IGrammar extends IBaseStructure {
	public NonTerminalElement getAxiom();
	public List<TerminalElement> getTerminals();
	public List<NonTerminalElement> getNonTerminals();
}
