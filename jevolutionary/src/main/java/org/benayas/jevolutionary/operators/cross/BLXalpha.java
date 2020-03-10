package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGen;

public class BLXalpha extends Sexual {

	protected double alpha;
	
	public BLXalpha(double perc, double alpha) {
		super(perc, 2, 2);
		this.alpha = alpha;
	}

	@Override
	public List<IndividualVector> reproduct(ChromosomeStructure structure, List<IndividualVector> mattingPool ) {
		List<List<IndividualVector>> families = matting( mattingPool );
		
		List<IndividualVector> children = new ArrayList<>();
		
		families.forEach( f ->  children.addAll( RandomGen.get().getDouble() < percentage ? child( f ) : f ) );
				
		return children;
	}
	
	protected List<IndividualVector> child( List<IndividualVector> parents ){
		List<IndividualVector> children = new ArrayList<>();
		IndividualVector p1 = parents.get( 0 );
		IndividualVector p2 = parents.get( 1 );
			
		IndividualVector child1 = IndividualVector.create( p1.dimension() );
		IndividualVector child2 = IndividualVector.create( p1.dimension() );
		for ( int i = 0; i < child1.dimension(); i++){
			double r1 = p1.get( i );
			double r2 = p2.get( i );
			Pair<Double, Double> interval = interval( Math.min( r1, r2 ), Math.max( r1, r2 ) );
			double v1 = RandomGen.get().getDouble( interval.getL(), interval.getR() );
				
			child1.set( i, v1  );
			child2.set( i, r1 + r2 - v1 );
		}
		children.add( child1 );
		children.add( child2 );		
		
		return children;
	}
	
	protected Pair<Double, Double> interval( double a, double b ){
		Pair<Double, Double> interval = new Pair<>();
		interval.setL( a - alpha * ( b - a ) );
		interval.setR( b + alpha * ( b - a ) );
		return interval;
	}

}
