package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;

public abstract class Sexual implements ICross<IndividualVector, ChromosomeStructure> {

	protected int numParents;
	protected int numChildren;
	protected double percentage;

	public Sexual( double perc, double parents, double children){
		this.percentage = perc;
		this.numParents = (int) parents;
		this.numChildren = (int) children;
	}
	
	
	protected List<List<IndividualVector>> matting( List<IndividualVector> mattingPool ){
		List<List<IndividualVector>> families = new ArrayList<>();		
		
		for ( int i = 0; i <= mattingPool.size() - numParents ; i = i + numParents ){
			List<IndividualVector> parents = new ArrayList<>();
			//Get the parents
			for (int j = 0; j < numParents; j++ )
				parents.add( mattingPool.get( i + j ) );
			
			families.add( parents );			
		}
		
		return families;
	}

}
