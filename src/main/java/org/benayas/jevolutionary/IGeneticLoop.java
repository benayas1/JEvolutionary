package org.benayas.jevolutionary;

import org.benayas.jevolutionary.util.Pair;

/**
 * @author alberto
 *	The object containing the logic must implement this interface.
 *	The return object contains pairs of combination ID linked to an object
 */
public interface IGeneticLoop {
	<T extends IEvaluation> Iterable<Pair<Integer, T>> run();
}
