package org.benayas.jevolutionary;

public abstract class Individual implements Comparable<Individual>{
	
	public static int combID = 0;
	private int id;
	protected IEvaluation score = null;
	
	protected Individual(){
		this.id = combID++;
	}
	
	public double getScore() {
		return score.score();
	}

	public void setEvaluation(IEvaluation t) {
		this.score = t;
	}
	
	public boolean hasEvaluation(){
		return score != null;
	}
	
	public IEvaluation getEvaluation(){
		return score;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract Object[] values();

}
