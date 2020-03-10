package org.benayas.jevolutionary;

import org.benayas.jevolutionary.ga.IndividualVector;

public interface IndividualConvertor<T> {
		public T convert( IndividualVector i );
}
