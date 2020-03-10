package org.benayas.jevolutionary.algorithms;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.benayas.jevolutionary.Generation;
import org.benayas.jevolutionary.IBaseStructure;
import org.benayas.jevolutionary.IEvaluation;
import org.benayas.jevolutionary.IGeneticLoop;
import org.benayas.jevolutionary.IOperator;
import org.benayas.jevolutionary.Individual;
import org.benayas.jevolutionary.ga.ChromosomeStructure;
import org.benayas.jevolutionary.stop.IStopCondition;
import org.benayas.jevolutionary.util.ExcelWriter;
import org.benayas.jevolutionary.util.Log;
import org.benayas.jevolutionary.util.Pair;
import org.benayas.jevolutionary.util.UtilReflect;



public abstract class EvolutiveComputation< T extends Individual, R extends IBaseStructure> {
	
	public final static int NO_OUTPUT = 0;
	public final static int MINIMUM = 1;
	public final static int ALL = 2;
	
	protected R structure;
	protected Generation<T> generation;
	protected int elements;
	protected int maxLoops;
	protected IStopCondition stop;
	protected int verbose;
	private List<Object[]> export;
	private String exportFilename;
	
	protected EvolutiveComputation( R structure, int elements, int loops, IStopCondition stop, int verbose ){
		this.structure = structure;
		this.elements = elements;
		this.maxLoops = loops;
		this.stop = stop;
		this.verbose = verbose;
	}

	private  void updateFitness( Iterable<Pair<Integer, IEvaluation>> data ){
		
		Iterator<Pair<Integer, IEvaluation>> it = data.iterator();
		while ( it.hasNext() ){
			Pair<Integer, IEvaluation> result = it.next();
			generation.score( result.getL(), result.getR() );	
		}				
		
		if ( verbose >= ALL ) {
			Log.debug( "Best of generation " + generation.best().toString() );
			generation.printResults();
		}
	}
	
	protected abstract Generation<T> nextGeneration();
	
	protected boolean stop() {
		return generation == null ? false : stop.stop( generation );
	}

	public Collection<T> currentGeneration(){
		return generation.toEvaluate();
	}
	
	public final T execute( IGeneticLoop loop ){
		int i = 0;
		while ( !stop() && i < maxLoops ){
			//Log.debug("Starting loop " + ( i + 1) );
			generation =  nextGeneration();
			Iterable<Pair<Integer, IEvaluation>> data = loop.run();
 			updateFitness( data ); 			
 			
 			if ( export != null ) {
 				export.add( new Object[]{ i, generation.best().getScore(), generation.averageScore(), generation.zeroCounts(), generation.minimum() } ); 
 			}
 			i++;
		}
		
		if ( verbose >= MINIMUM )
			Log.info( "Best individual " + generation.best().toString() );
		
		if ( export != null ) {
			ExcelWriter excel = new ExcelWriter();
			excel.writeData( "Evolution", export, excel.new ChartSettings( 0, 1, 2, 3, 4 ), "Generation", "Best", "Average", "Zero Counts", "Minimum");
			excel.dumpFile( exportFilename == null ? "Algorithm Analysis " + LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss") ) : exportFilename );
		}
			
		
		return generation.best();
	}
	
	public void setExportAnalysis() {
		this.export = new ArrayList<>();
	}
	
	public void setExportAnalysis( String filename ) {
		this.exportFilename = filename;
		setExportAnalysis();
	}
	
	public static EvolutiveComputation<?,?> create(String className, ChromosomeStructure s, int n, int loops, IStopCondition stop, IOperator[] operators, int verbose ){	 
	    Class<?> clazz = null;
	    try {
	    	//Load the class
	    	clazz = Class.forName( className );
	    	
	    	EvolutiveComputation<?,?> evolutive = (EvolutiveComputation<?,?>) UtilReflect.create( clazz, new Object[]{ s, n, loops, verbose, stop, operators } );	    	
	        	                
	        return evolutive;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return null;
	}	
	
	public static EvolutiveComputation<?,?> create(String className, ChromosomeStructure s, int n, int loops, IStopCondition stop, Map<String,IOperator> operators, int verbose ){	 
	    IOperator[] ops = new IOperator[ operators.size() ];
	    ops[0] = operators.get("selection");
	    ops[1] = operators.get("cross");
	    ops[2] = operators.get("mutation");
	    ops[3] = operators.get("replacement");
	    return create( className, s, n, loops, stop, ops, verbose );
	}	
}
