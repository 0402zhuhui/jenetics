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

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class AccumulatorsMinMaxTest 
	extends AbstractAccumulatorTester<Accumulators.MinMax<Double>> 
{
	
	final Factory<Accumulators.MinMax<Double>> 
	_factory = new Factory<Accumulators.MinMax<Double>>() {
		@Override
		public Accumulators.MinMax<Double> newInstance() {
			return new Accumulators.MinMax<Double>();
		}
	};
	@Override
	protected Factory<Accumulators.MinMax<Double>> getFactory() {
		return _factory;
	}
	
	@Test
	public void minMax() {
		final Integer[] array = new Integer[20];
		for (int i = 0; i < array.length; ++i) {
			array[i] = i;
		}
		ArrayUtils.shuffle(array);
		
		final Accumulators.MinMax<Integer> minMax = new Accumulators.MinMax<Integer>();
		Accumulators.accumulate(Arrays.asList(array), minMax);
		Assert.assertEquals(minMax.getMin(), new Integer(0));
		Assert.assertEquals(minMax.getMax(), new Integer(19));
	}
	
}
