package org.benayas.jevolutionary.stop;

import org.benayas.jevolutionary.Generation;

public class NonStop implements IStopCondition {

	@Override
	public boolean stop(Generation<?> g) {
		return false;
	}

}
