package org.benayas.jevolutionary.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

@SuppressWarnings("rawtypes")
public class UtilReflect {
	
	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	private static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz) || ClassUtils.isPrimitiveOrWrapper( clazz ) || clazz.isEnum();
    }

    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(Date.class);
        return ret;
    }
	
    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll( Arrays.asList( type.getDeclaredFields() ) );

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass() );
	    }

	    return fields;
	}
	
	
	@SuppressWarnings("unchecked")
	private static <T> void getFieldsForObject( Map<Integer, T> list, Map<Integer, Object> used, Object o, Class type, boolean goDeep ){
		
		//Check if this object has been already analyzed, to avoid infinite loops due to circular references
		if ( used.containsKey( o.hashCode() ) ){
			return;
		}else{
			used.put( o.hashCode(), o);
		}
		
		//Check if the object is instance of the target class
		if ( type.isInstance( o ) ){
			list.put( o.hashCode(), (T) o );
			if ( !goDeep )//Return if it does not go deep
				return;
		}
		List<Field> fields = FieldUtils.getAllFieldsList( o.getClass() );
		
		for( Field field : fields ) {						
			Object f = null;
			try {
				field.setAccessible(true);			
				f = field.get( o );
				boolean processed = f == null;	
				
				if ( !processed && TypeUtils.isArrayType( f.getClass() ) ){
					for ( int i = 0; i < Array.getLength( f ) ; i++){
						Object obj = Array.get(f, i);					
						if ( obj!= null && !isWrapperType( obj.getClass() ) && !list.containsKey( obj.hashCode() ) ){
							getFieldsForObject( list, used, obj, type, goDeep );
						}										
					}				
					processed = true;
				}
				
				if (!processed && f instanceof Iterable ){
					Iterator<Object> it = ((Iterable<Object>)f).iterator();
					while (it.hasNext()){
						Object obj = it.next();
						if ( obj!= null && !isWrapperType( obj.getClass() ) && !list.containsKey( obj.hashCode() ) ){
							getFieldsForObject( list, used, obj, type, goDeep );
						}						
					}
					processed = true;
				}
				
				if (!processed && f instanceof Map ){
					Iterator<Object> it = ((Map)f).values().iterator();
					while ( it.hasNext() ){
						Object obj = it.next();
						if ( obj!= null && !isWrapperType( obj.getClass() ) && !list.containsKey( obj.hashCode() ) ){
							getFieldsForObject( list, used, obj, type, goDeep );
						}						
					}
					processed = true;
				}
								
				if (!processed )
					if(!list.containsKey( f.hashCode() ) ){
						if ( !isWrapperType( f.getClass() ) )
							if (!field.isSynthetic() )
								if (!Modifier.isStatic(field.getModifiers()) )
									getFieldsForObject( list, used, f, type, goDeep );
								
					}		
											
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}				
		}
	}
	
	
	/**
	 * @param o Object to analyze
	 * @param clazz Class of the objects we are looking for
	 * @param goDeep flag to know whether to check or not within the fields
	 * @return a list of objects of class clazz contained in the object o or any of its children
	 */
	public static <T> List<T> getAllObjects(Object o, Class<T> clazz, boolean goDeep ){
		Map<Integer, T> list = new HashMap<>();
		Map<Integer, Object> used = new HashMap<>();
		getFieldsForObject( list, used, o, clazz, goDeep );
		List<T> result = new ArrayList<>();
		for (T obj : list.values()){
			if ( clazz.isInstance( obj ) )
				result.add( obj );
		}
		return result;
	}
	
	public static <T> T create( Class<T> clazz, Object[] params ){
		 // Load the class.
	    try {
	    	
	    	// Search for an "appropriate" constructor.
		    Class<?>[] paramTypes = new Class<?>[params.length];
		    int i = 0;
		    for ( Object o : params ){
		    	paramTypes[i] = ClassUtils.isPrimitiveWrapper( o.getClass() ) ? ClassUtils.wrapperToPrimitive( o.getClass() ) : o.getClass();
		    	i++;
		    }
		    
		    Constructor<?>[] constructors = clazz.getConstructors();
		    boolean found = false;
		    int k = 0;
		    while( !found && k < constructors.length ) {
		          Class<?>[] p = constructors[k].getParameterTypes();
		          boolean valid = true;
		          for ( int j = 0; j < p.length; j++ ){
		        	  valid = valid && p[j].isAssignableFrom( paramTypes[j] );	        		  	        	  
		          }
		          found = valid;	          
		          k = found ? k : k + 1;
		    }
		    Constructor<?> ctor = constructors[k];
		    
//		    Constructor<T> ctor = clazz.getConstructor( paramTypes );
	        
	        T newObject = (T) ctor.newInstance(params);        
	        
	        return newObject;
	        
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return null; 
	}
	
	@SuppressWarnings("unchecked")
	public static <T,R> T convert( Class<T> targetclass, R source ){
		 // Load the class.
	    try {	    	
		    Constructor<?> ctor = targetclass.getConstructor( new Class<?>[0] );
	        T result = (T) ctor.newInstance( new Object[0] );
	        
	        List<Field> fieldsResult = new ArrayList<>();
	        getAllFields( fieldsResult, result.getClass() );
	        
	        List<Field> fieldsSource = new ArrayList<>();
	        getAllFields( fieldsSource, source.getClass() );
	        
	        for( Field fieldSource : fieldsSource ) {														
				fieldSource.setAccessible(true);
				Object s = fieldSource.get( source );
				for( Field fieldResult : fieldsResult ){
					fieldResult.setAccessible(true);
					if ( fieldResult.getName().equals( fieldSource.getName() ) ){
						fieldResult.set( result, s );
					}
				}				
	        }	        
	        
	        return result;
	        
		} catch (Exception e) {
			e.printStackTrace();
			Log.error( e.getMessage() );
		}

	    return null; 
	}
	
	public static <T,R> List<T> convertList( Class<T> targetclass, List<R> source ){
		List<T> list = new ArrayList<>();
		for ( R r : source ){
			list.add( convert( targetclass, r) );
		}
		return list;
	}
	
}
