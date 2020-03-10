package org.benayas.jevolutionary.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alberto.Benayas
 *	This class represents a single permutator.
 *	A single permutator has its own ID, and receives a list of permutable objects.
 *	The main function of this object is to pass a value to every permutable object
 */
public class Gene implements Serializable, Comparable<Gene>{

	private static final long serialVersionUID = 1L;
	private int id;
	private double min;
	private double max;
	private String domain;
	private List<IPermutable> list;
	
	public Gene() {}
	
	public Gene(int id, double min, double max, String domain){
		this.setId( id );
		this.min = min;
		this.max = max;
		this.domain = domain;
		this.list = new ArrayList<>();
	}
	
	public Gene(int id, List<IPermutable> list){
		this( id, 0, 0, null );
		setList(list);
	}
	
	public void setList(List<IPermutable> list){
		this.list = list;
	}
	
	/**
	 * Receives a value, and propagates this value to its permutable objects
	 * @param value the value to be applied to the Permutable objects
	 * @return false if there were no permutable objects, true otherwise
	 */
	public boolean permutate(double value){	
		if (list == null)
			return false;
		
		for (IPermutable attr : list){
				attr.param(id, String.valueOf( value ));				
		}
		
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void add(IPermutable att) {
		list.add( att );	
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public String getDomain() {
		return domain;
	}

	@Override
	public int compareTo(Gene p) {
		return id < p.id ? -1 : id > p.id ? 1 : 0;
	}
}
