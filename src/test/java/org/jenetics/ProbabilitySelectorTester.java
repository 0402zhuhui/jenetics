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
package org.jenetics;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jscience.mathematics.number.Float64;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import org.jenetics.util.ArrayUtils;
import org.jenetics.util.RandomRegistry;
import org.jenetics.util.Validator;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public abstract class ProbabilitySelectorTester<
	S extends ProbabilitySelector<Float64Gene, Float64>
>  
	extends SelectorTester<S> 
{

	protected abstract boolean isSorted();
	
	@Test
	public void nextIndex() {
		final Random random = RandomRegistry.getRandom();
		
		final double[] props = new double[10];
		double divisor = props.length*(props.length + 1)/2.0;
		for (int i = 0; i < props.length; ++i) {
			props[i] = (i + 1)/divisor;
		}
		randomize(props, random);
		
		double samples = 1000000;
		double[] indices = new double[props.length];
		Arrays.fill(indices, 0);
		
		for (int i = 0; i < samples; ++i) {
			indices[ProbabilitySelector.nextIndex(props, random)] += 1;
		}
		
		for (int i = 0; i < props.length; ++i) {
			indices[i] /= samples;
		}
		
		Reporter.log(toString(props) + String.format(": %6f", sum(props)));
		Reporter.log(toString(indices) + String.format(": %6f", sum(indices)));
		
		for (int i = 0; i < props.length; ++i) {
			Assert.assertEquals(indices[i], props[i], 0.005);
		}
	}
	
	@Test
	public void probabilities() {
		final Population<Float64Gene, Float64> population = TestUtils.newFloat64Population(100);
		ArrayUtils.shuffle(population, new Random(System.currentTimeMillis()));
		
		final S selector = getFactory().newInstance();
		final double[] props = selector.probabilities(population, 23);
		Assert.assertEquals(props.length, population.size());
		
		if (isSorted()) {
			assertSortedDescending(population);
			assertSortedDescending(props);
		}
		Assert.assertEquals(sum(props), 1.0, 0.000001);
		assertPositive(props);	
	}
	
	private static String toString(final double[] array) {
		StringBuilder out = new StringBuilder();
		
		out.append("[");
		if (array.length > 0) {
			out.append(String.format("%6f", array[0]));
		}
		for (int i = 1; i < array.length; ++i) {
			out.append(", ");
			out.append(String.format("%6f", array[i]));
		}
		out.append("]");
		
		return out.toString();
	}
	
	protected static double sum(final double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; ++i) {
			sum += array[i];
		}
		return sum;
	}
	
	protected static void assertPositive(final double[] array) {
		for (int i = 0; i < array.length; ++i) {
			Assert.assertTrue(array[i] >= 0.0, "All values must be positive: " + array[i]);
		}
	}
	
	private static void randomize(final double[] array, final Random random) {
		Validator.nonNull(array, "Array");
		for (int j = array.length - 1; j > 0; --j) {
			swap(array, j, random.nextInt(j + 1));
		}
	}
	
	private static void swap(final double[] array, int i, int j) {
		double temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	protected static void assertSortedDescending(final double[] values) {
		for (int i = 1; i < values.length; ++i) {
			Assert.assertTrue(values[i - 1] >= values[i]);
		}
	}
	
	protected static <T extends Comparable<T>> void assertSortedDescending(final List<? extends T> values) {
		for (int i = 1; i < values.size(); ++i) {
			Assert.assertTrue(values.get(i - 1).compareTo(values.get(i)) >= 0);
		}
	}
	
}
