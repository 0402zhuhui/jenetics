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
package org.jenetics.performance;

import java.util.Iterator;

import org.jscience.mathematics.number.Float64;

import org.jenetics.FitnessFunction;
import org.jenetics.Float64Chromosome;
import org.jenetics.Float64Gene;
import org.jenetics.Genotype;
import org.jenetics.Phenotype;
import org.jenetics.Population;
import org.jenetics.util.Array;
import org.jenetics.util.ArrayUtils;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
@Suite("Population")
public class PopulationTest {

	private int SIZE = 1000000;
	private final int LOOPS = 1000;

	private final Population<Float64Gene, Float64> _population = newFloat64GenePopulation(
			1, 1, SIZE
		);
	
	@Test(1)
	public TestCase iterator = new TestCase("iterator", LOOPS, SIZE) {
		@Override
		protected void test() {
			for (Iterator<?> it = _population.iterator(); it.hasNext();) {
				it.next();
			}
		}
	};

	@Test(2)
	public TestCase iterable = new TestCase("iterable", LOOPS, SIZE) {
		@Override
		protected void test() {
			for (@SuppressWarnings("unused") Object value : _population); 
		}
	};

	@Test(3)
	public TestCase sort = new TestCase("sort", LOOPS, SIZE) {
		@Override
		protected void test() {
			_population.sort();
		}
		
		@Override
		protected void afterTest() {
			ArrayUtils.shuffle(_population);
		}
	};
	

	private static final class Continous 
		implements FitnessFunction<Float64Gene, Float64> 
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Float64 evaluate(Genotype<Float64Gene> genotype) {
			return genotype.getChromosome().getGene().getAllele(); 
		}
	}

	private static final FitnessFunction<Float64Gene, Float64> FF = new Continous();
	
	private static final Population<Float64Gene, Float64> newFloat64GenePopulation(
		final int ngenes, 
		final int nchromosomes, 
		final int npopulation
	) {
		final Array<Float64Chromosome> chromosomes = 
			new Array<Float64Chromosome>(nchromosomes);
		
		for (int i = 0; i < nchromosomes; ++i) {
			chromosomes.set(i, new Float64Chromosome(0, 10, ngenes));
		}	
		
		final Genotype<Float64Gene> genotype = Genotype.valueOf(chromosomes.toISeq());
		final Population<Float64Gene, Float64> population = 
			new Population<Float64Gene, Float64>(npopulation);
		
		for (int i = 0; i < npopulation; ++i) {
			population.add(Phenotype.valueOf(genotype.newInstance(), FF, 0));
		}	
		
		return population;
	}

}
