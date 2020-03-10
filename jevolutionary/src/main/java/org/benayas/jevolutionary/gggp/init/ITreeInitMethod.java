package org.benayas.jevolutionary.gggp.init;

import org.benayas.jevolutionary.gggp.IndividualTree;

public interface ITreeInitMethod<T extends IndividualTree> {
	public T create();
}
