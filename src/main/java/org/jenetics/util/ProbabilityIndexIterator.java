/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *     
 */
package org.jenetics.util;

import static org.jenetics.util.Validator.checkProbability;
import static org.jenetics.util.Validator.nonNull;

import java.util.Random;

/**
 * This class allows to iterate over an array and <i>selects</i> the single 
 * indices with a given probability.
 * 
 * [code]
 *     final Array<Integer> array = ...
 *     final ProbabilityIndexIterator it = 
 *         new ProbabilityIndexIterator(array.length(), 0.3, random);
 *         
 *     for (int i = it.next(); i != -1; i = it.next()) {
 *         final Integer element = array.get(i);
 *         System.out.println(element);
 *     }
 * [/code]
 * 
 * The example above iterates over the given <i>array</i>. Every array element
 * has a probability to be printlined of 0.3.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: org.eclipse.jdt.ui.prefs 421 2010-03-18 22:41:17Z fwilhelm $
 */
public class ProbabilityIndexIterator {
	private final int _n;
	private final double _probability;
	private final Random _random;
	
	private int _pos = -1;
	
	/**
	 * Create a new ProbabilityIndexIterator.
	 * 
	 * @param n the maximal value (exclusively) of the iterator.
	 * @param probability the index selection probability.
	 * @param random the random engine used for creating the random indexes.
	 * @throws IllegalArgumentException if {@code n == Integer.MAX_VALUE} or
	 *         {@code n <= 0} or the given {@code probability} is not valid.
	 * @throws NullPointerException if the given {@code random} engine is 
	 *         {@code null}.
	 */
	public ProbabilityIndexIterator(
		final int n, 
		final double probability, 
		final Random random
	) {
		if (n == Integer.MAX_VALUE) {
			throw new IllegalArgumentException(String.format(
					"n must be smaller than Integer.MAX_VALUE."
				));
		}
		if (n <= 0) {
			throw new IllegalArgumentException(String.format(
					"n must be greate than zero: %d", n
				));
		}
		
		_n = n;
		_probability = checkProbability(probability);
		_random = nonNull(random, "Random generator");
	}
	
	private void increment() {
		if (_pos < Integer.MAX_VALUE) {
			++_pos;
		}
	}
	
	/**
	 * Return the next <i>random</i> index or -1 if this iterator has reached its
	 * end.
	 * 
	 * @return the next <i>random</i> index.
	 */
	public int next() {
		while (_random.nextDouble() >= _probability && _pos < _n) {
			increment();
		}
		increment();

		return _pos < _n ? _pos : -1;
	}
	
}
