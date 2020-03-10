package org.benayas.jevolutionary.test;

import org.benayas.jevolutionary.IEvaluation;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.util.RandomGen;


public class TestFitness implements IEvaluation {

	private double score;
	private int id;

	public TestFitness( IndividualVector i ) {
		double modificator = RandomGen.get().gauss(10,1)/10.0; // just to give some randomness to the example
		this.score = modificator * (Double)i.get(0) * (Double)i.get(1) / (Double)i.get(2);  //Members of individual vector must be casted, so a conversor method would be nice
		this.id = i.getId();
	}

	@Override
	public int compareTo(IEvaluation o) {
		TestFitness r = (TestFitness) o;						
        return score >= r.score ? 1 : score < r.score ? -1 : 0;
	}

	@Override
	public double score() {
		return score;
	}
	
	public int id() {
		return id;
	}

}
