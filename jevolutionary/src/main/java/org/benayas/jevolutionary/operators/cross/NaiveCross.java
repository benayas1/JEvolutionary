package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.util.RandomGen;

public class NaiveCross extends Sexual {

	public NaiveCross( double perc, double parents, double children){
		super( perc, parents, children );
	}

	@Override
	public List<IndividualVector> reproduct( ChromosomeStructure structure, List<IndividualVector> mattingPool ) {
		
		List<List<IndividualVector>> families = matting( mattingPool );
		
		List<IndividualVector> children = new ArrayList<>();
		
		families.forEach( f ->  children.addAll( RandomGen.get().getDouble() < percentage ? child( structure.dimension(), f ) : f ) );
				
		return children;
	}
	
	private List<IndividualVector> child( int dimension, List<IndividualVector> parents ){
		List<IndividualVector> children = new ArrayList<>();
		for ( int n = 0; n < numChildren; n++ ){
			IndividualVector child = IndividualVector.create( dimension );
			for ( int i = 0; i < child.dimension(); i++){
				child.set( i, parents.get( RandomGen.get().getInt( parents.size() ) ).get(i)  );
			}
			children.add( child );
		}
		
		return children;
	}
}
