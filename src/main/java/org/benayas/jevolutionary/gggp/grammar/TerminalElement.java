package org.benayas.jevolutionary.gggp.grammar;

public class TerminalElement implements IGrammarElement{
	
	private Object obj;

	
	public TerminalElement( Object obj ) {
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}
	
	public String toString() {
		return obj.toString();
	}

}
