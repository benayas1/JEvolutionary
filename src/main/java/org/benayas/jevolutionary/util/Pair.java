package org.benayas.jevolutionary.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;



@SuppressWarnings("serial")
public class Pair<L, R> implements Serializable, Comparable<Pair<L, R>>{

	private L l;
	private R r;
	
	public Pair(){}
	public Pair(L l, R r){
	    this.l = l;
	    this.r = r;
	}
	
	public Pair(Entry<L,R> entry){
		this.l = entry.getKey();
		this.r = entry.getValue();
	}
	public L getL(){ return l; }
	public R getR(){ return r; }
	public void setL(L l){ this.l = l; }
	public void setR(R r){ this.r = r; }
	public String toString(){
		return l.toString() + " " +  r.toString();
	}
	
	public static List<Pair<?,?>> toList(List<?> left, List<?> right){
		List<Pair<?,?>> list = new ArrayList<Pair<?,?>>();
		
		for (int i = 0; i < left.size() ; i++){
			if ( i < right.size() )
			list.add( new Pair<>(left.get(i), ( i < right.size() ) ? right.get( i ) : null ) );	
		}
		
		return list;
	}
	
	public static List<Pair<?,?>> toList(Set<Entry<?,?>> set){
		List< Pair<?,?> > list = new ArrayList< Pair<?,?> >();
		Iterator<Entry<?, ?>> it = set.iterator();
		while ( it.hasNext() ){
			list.add(new Pair<>( it.next() ));
		}
		
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if ( this == o )
			return true;
		
		Pair<L,R> p = (Pair<L,R>)o;
			
		return l.equals(p.getL()) && r.equals(p.getR());
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Pair<L, R> o) {
		Pair<L,R> p = (Pair<L,R>)o;
		if (l instanceof Comparable)
			return ((Comparable<L>) l).compareTo(p.getL());

		return 0;
	}
	
	
}
