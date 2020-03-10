package org.benayas.jevolutionary.gggp.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rule extends ArrayList<IGrammarElement>{
	
	private static final long serialVersionUID = 1L;

	public Rule( IGrammarElement... elements ) {
		addAll( Arrays.asList( elements ) );
	}
	
	public Rule(List<IGrammarElement> elements) {
		addAll( elements );
	}
	
}
