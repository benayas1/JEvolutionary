package org.benayas.jevolutionary.operators.cross;

import java.util.ArrayList;
import java.util.List;

import org.benayas.jevolutionary.domain.IDomain;
import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.RandomGen;

public class MMX extends Sexual {

	private double fiA;//negative
	private double fiB;//negative
	private double fiC;//positive > 0
	
	public MMX(double perc, double parents, double a, double b, double c ) {
		super(perc, parents, 2);
		this.fiA = a;
		this.fiB = b;
		this.fiC = c;
	}

	@Override
	public List<IndividualVector> reproduct( ChromosomeStructure structure, List<IndividualVector> mattingPool) {
		List<List<IndividualVector>> families = matting( mattingPool );
		
		List<IndividualVector> children = new ArrayList<>();
		families.forEach( f -> children.addAll( children( f, structure ) ) );
		
		return children;
	}
	
	private Pair<Double,Double> minMax( double[] gens ){
		Pair<Double,Double> pair = new Pair<>();
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
				
		for ( double d : gens ){
			min = Math.min( min, d);
			max = Math.max( max, d);					
		}
		
		pair.setL( min );
		pair.setR( max );
		
		return pair;
	}
	
	private double fi( double gi ){
		return gi < fiC ? ( ( fiB - fiA ) / fiC ) * gi + fiA :  ( ( gi / 2 ) / ( 1 - fiC ) ) * ( gi - fiC );
	}
	
	private double normalize( double value, double lower, double upper ){
		return ( value - lower ) / ( upper - lower );
	}
	
	private double denormalize( double value, double lower, double upper ){
		return ( value * ( upper - lower ) ) + lower;
	}
	
	private List<IndividualVector> children( List<IndividualVector> parents, ChromosomeStructure structure ){
		double[][] gens = new double[ structure.dimension() ][ parents.size() ];
		List<IndividualVector> children = new ArrayList<>();
		
		//Creates 'empty' childrens
		for( int i = 0; i < numChildren; i++)
			children.add( IndividualVector.create( structure.dimension() ) );
		
		
		//Normalize gens
		for ( int i = 0; i < structure.dimension(); i++){
			IDomain<?> domain = structure.get( i );
			double lower = domain.lowerBound() instanceof Integer ? ((Integer) domain.lowerBound()).doubleValue() : (Double) domain.lowerBound();
			double upper = domain.upperBound() instanceof Integer ? ((Integer) domain.upperBound()).doubleValue() : (Double) domain.upperBound();			
						
			//Normalize [0,1]. This matrix is [gens][individuals]
			for ( int j = 0; j < parents.size(); j++){
				double value = parents.get( j ).get( i ) instanceof Integer ? ((Integer) parents.get( j ).get( i )).doubleValue() : (double) parents.get( j ).get( i );
				gens[i][j] = normalize( value, lower, upper );				
			}
			
			//Get min and max
			Pair<Double,Double> minMax = minMax( gens[i] );
			double gi = minMax.getR() - minMax.getL();
			
			//Calculate fi
			double fi = fi( gi );
			
			//Calculate range
			double giMin = Math.max( minMax.getL() + fi, 0 );
			double giMax = Math.min( minMax.getR() - fi, 1 );
			
			double v1 = denormalize( RandomGen.get().getDouble( giMin, giMax ), lower, upper );
			
			children.get(0).set( i, v1  );
			children.get(1).set( i, lower + upper - v1 );			
		}
		
		return children;
	}
}
