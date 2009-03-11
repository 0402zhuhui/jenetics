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

import java.util.Random;

//import org.jenetics.FitnessFunction;
//import org.jenetics.Genotype;
//import org.jenetics.IntegerChromosome;
//import org.jenetics.IntegerGene;
//import org.jenetics.Phenotype;
//import org.jenetics.Population;
//import org.jscience.mathematics.number.Integer64;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: ArrayUtilsTest.java,v 1.3 2009-03-11 21:27:52 fwilhelm Exp $
 */
public class ArrayUtilsTest {

	@Test
	public void asList() {
		final Array<Integer> ia = new Array<Integer>(50);
		for (int i = 0; i < ia.length(); ++i) {
			ia.set(i, i);
		}
		
		final Array<Integer> sia = ia.subArray(23, 46);
		Integer[] array = ArrayUtils.asList(sia).toArray(new Integer[0]);
		for (int i = 0; i < array.length; ++i) {
			Assert.assertEquals(array[i], new Integer(i + 23));
		}
		
		Integer[] object = new Integer[23];
		array = ArrayUtils.asList(sia).toArray(object);
		Assert.assertSame(array, object);
		for (int i = 0; i < array.length; ++i) {
			Assert.assertEquals(array[i], new Integer(i + 23));
		}
	}
	
	
	@Test
	public void subset() {
		final Random random = new Random();
		
		for (int i = 1; i < 100; ++i) {
			int[] sub = new int[i];
			ArrayUtils.subset(1000, sub, random);
			
			Assert.assertTrue(isSorted(sub));
		}
		
	}
	
	private static boolean isSorted(int[] array) {
		boolean sorted = true;
		for (int i = 0; i < array.length - 1 && sorted; ++i) {
			sorted = array[i] < array[i + 1];
		}
		return sorted;
	}
	
	
//	@Test
//	public void performance() {
//		final int SIZE = 1000;
//		final Population<IntegerGene, Integer64> pop = new Population<IntegerGene, Integer64>(SIZE);
//		for (int i = 0; i < SIZE; ++i) {
//			pop.add(Phenotype.valueOf(
//					Genotype.valueOf(new IntegerChromosome(IntegerGene.valueOf(i, 0, SIZE))), 
//					new FitnessFunction<IntegerGene, Integer64>() {
//						private static final long serialVersionUID = 1L;
//						@Override
//						public Integer64 evaluate(Genotype<IntegerGene> genotype) {
//							return null;
//						}
//					}, i));
//		}
//		
//		final Timer timer = new Timer();
//		timer.start();
//		for (int j = 0; j < 10000; ++j) {
//			for (int i = 0; i < pop.size(); ++i) {
//				final Phenotype<?, ?> pt = pop.get(i);
//			}
//		}
//		timer.stop();
//		System.out.println(timer.toString());
//		
//		timer.reset();
//		timer.start();
//		for (int i = 0; i < 10000; ++i) {
//			ArrayUtils.subset(1000, 400, RandomRegistry.getRandom());
//		}
//		timer.stop();
//		System.out.println(timer);
//		
//	}
	
}






