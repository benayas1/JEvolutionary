package org.benayas.jevolutionary.test;

import java.util.List;
import java.util.stream.Collectors;

import org.benayas.jevolutionary.IEvaluation;
import org.benayas.jevolutionary.IGeneticLoop;
import org.benayas.jevolutionary.algorithms.GeneticAlgorithm;
import org.benayas.jevolutionary.domain.IDomain;
import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.ga.IndividualVector;
import org.benayas.jevolutionary.operators.cross.BLXalpha;
import org.benayas.jevolutionary.operators.cross.ICross;
import org.benayas.jevolutionary.operators.mutation.GaussMutator;
import org.benayas.jevolutionary.operators.mutation.IMutation;
import org.benayas.jevolutionary.operators.replacement.Elitist;
import org.benayas.jevolutionary.operators.replacement.IReplacement;
import org.benayas.jevolutionary.operators.selection.ISelection;
import org.benayas.jevolutionary.operators.selection.Tournament;
import org.benayas.jevolutionary.stop.IStopCondition;
import org.benayas.jevolutionary.stop.NonStop;
import org.benayas.jevolutionary.util.Gene;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.Log;

public class ExampleGA {

	public static void main(String[] args) {
		
		// Define genes
		Gene height = new Gene(0, 1.50, 2.1, IDomain.DOUBLE );
		Gene weight = new Gene(1, 50, 125, IDomain.DOUBLE );
		Gene age = new Gene(2, 18, 40, IDomain.DOUBLE );
		
		// Create ChromosomeStructure 
		ChromosomeStructure structure = new ChromosomeStructure(height, weight, age);
		
		// Define genetic algorithm metadata
		int elements = 100; // number of elements per generation
		int loops = 20; // number of maximum loops
		int verbose = 1; // 2 for maximum
		
		
		// Define GA operators
		IStopCondition stop = new NonStop(); // we just let it go
		ISelection<IndividualVector> selector = new Tournament<IndividualVector>(0.5); // Selection by tournament
		ICross<IndividualVector, ChromosomeStructure> cross = new BLXalpha(0.5, 0.1); // 50% of candidates will cross, alpha will be 0.1
		IMutation<IndividualVector, ChromosomeStructure> mutator = new GaussMutator(0.15); // Mutation rate is 0.15
		IReplacement<IndividualVector> replacement = new Elitist<IndividualVector>(); //Elitist replacement, keeping the fittest
		
		
		// Create the algorithm object
        GeneticAlgorithm ga = new GeneticAlgorithm(structure, elements, loops, verbose, stop, selector, cross, mutator, replacement);
        
        
        // The parameter of the execute method is an instance of a class that implements the IGeneticLoop interface
        // In that instance we have to define the external process that happens in between generations, and the object that calculates the fitness of each individual
        IndividualVector best = ga.execute(new IGeneticLoop() {
			@Override
			public List<Pair<Integer, IEvaluation>> run() {				
				Log.debug( "New Iteration" );
				// Generates a list of Fitness objects based on the individuals of the current generation.
				// Each fitness object is created out of an individual, and a score is calculated for each of them
				List<TestFitness> fitnessList = ga.currentGeneration().stream().map( i -> new TestFitness(i) ).collect( Collectors.toList() );
				
				return fitnessList.stream().map( r -> new Pair<Integer,IEvaluation>( r.id(), r ) ).collect( Collectors.toList() );
			}}
        );
        
        Log.info("Best Individual ID " + best.getId());
        Log.info("Best Individual's Height " + best.get(0) );
        Log.info("Best Individual's Weight " + best.get(1) );
        Log.info("Best Individual's Age " + best.get(2) );

	}
}
