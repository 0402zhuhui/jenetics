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

import org.jenetics.util.Array;
import org.jscience.mathematics.number.Float64;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
class TestUtils {

	private TestUtils() {
	}
	
	/**
	 * Data for alter count tests.
	 */
	public static Object[][] alterCountParameters() {
		return new Object[][] {
				//    ngenes,       nchromosomes     npopulation
				{ new Integer(1),   new Integer(1),  new Integer(100) },
				{ new Integer(5),   new Integer(1),  new Integer(100) },
				{ new Integer(80),  new Integer(1),  new Integer(100) },
				{ new Integer(1),   new Integer(2),  new Integer(100) },
				{ new Integer(5),   new Integer(2),  new Integer(100) },
				{ new Integer(80),  new Integer(2),  new Integer(100) },
				{ new Integer(1),   new Integer(15), new Integer(100) },
				{ new Integer(5),   new Integer(15), new Integer(100) },
				{ new Integer(80),  new Integer(15), new Integer(100) },
				
				{ new Integer(1),   new Integer(1),  new Integer(150) },
				{ new Integer(5),   new Integer(1),  new Integer(150) },
				{ new Integer(80),  new Integer(1),  new Integer(150) },
				{ new Integer(1),   new Integer(2),  new Integer(150) },
				{ new Integer(5),   new Integer(2),  new Integer(150) },
				{ new Integer(80),  new Integer(2),  new Integer(150) },
				{ new Integer(1),   new Integer(15), new Integer(150) },
				{ new Integer(5),   new Integer(15), new Integer(150) },
				{ new Integer(80),  new Integer(15), new Integer(150) },
				
				{ new Integer(1),   new Integer(1),  new Integer(500) },
				{ new Integer(5),   new Integer(1),  new Integer(500) },
				{ new Integer(80),  new Integer(1),  new Integer(500) },
				{ new Integer(1),   new Integer(2),  new Integer(500) },
				{ new Integer(5),   new Integer(2),  new Integer(500) },
				{ new Integer(80),  new Integer(2),  new Integer(500) },
				{ new Integer(1),   new Integer(15), new Integer(500) },
				{ new Integer(5),   new Integer(15), new Integer(500) },
				{ new Integer(80),  new Integer(15), new Integer(500) }
		};
	}
	
	/**
	 * Data for alter probability tests.
	 */
	public static Object[][] alterProbabilityParameters() {
		return new Object[][] {
				//    ngenes,       nchromosomes     npopulation
				{ new Integer(20),   new Integer(20),  new Integer(20), new Double(0.5) },
				{ new Integer(1),   new Integer(1),  new Integer(150), new Double(0.15) },
				{ new Integer(5),   new Integer(1),  new Integer(150), new Double(0.15) },
				{ new Integer(80),  new Integer(1),  new Integer(150), new Double(0.15) },
				{ new Integer(1),   new Integer(2),  new Integer(150), new Double(0.15) },
				{ new Integer(5),   new Integer(2),  new Integer(150), new Double(0.15) },
				{ new Integer(80),  new Integer(2),  new Integer(150), new Double(0.15) },
				{ new Integer(1),   new Integer(15), new Integer(150), new Double(0.15) },
				{ new Integer(5),   new Integer(15), new Integer(150), new Double(0.15) },
				{ new Integer(80),  new Integer(15), new Integer(150), new Double(0.15) },
				
				{ new Integer(1),   new Integer(1),  new Integer(150), new Double(0.5) },
				{ new Integer(5),   new Integer(1),  new Integer(150), new Double(0.5) },
				{ new Integer(80),  new Integer(1),  new Integer(150), new Double(0.5) },
				{ new Integer(1),   new Integer(2),  new Integer(150), new Double(0.5) },
				{ new Integer(5),   new Integer(2),  new Integer(150), new Double(0.5) },
				{ new Integer(80),  new Integer(2),  new Integer(150), new Double(0.5) },
				{ new Integer(1),   new Integer(15), new Integer(150), new Double(0.5) },
				{ new Integer(5),   new Integer(15), new Integer(150), new Double(0.5) },
				{ new Integer(80),  new Integer(15), new Integer(150), new Double(0.5) },				
				
				{ new Integer(1),   new Integer(1),  new Integer(150), new Double(0.85) },
				{ new Integer(5),   new Integer(1),  new Integer(150), new Double(0.85) },
				{ new Integer(80),  new Integer(1),  new Integer(150), new Double(0.85) },
				{ new Integer(1),   new Integer(2),  new Integer(150), new Double(0.85) },
				{ new Integer(5),   new Integer(2),  new Integer(150), new Double(0.85) },
				{ new Integer(80),  new Integer(2),  new Integer(150), new Double(0.85) },
				{ new Integer(1),   new Integer(15), new Integer(150), new Double(0.85) },
				{ new Integer(5),   new Integer(15), new Integer(150), new Double(0.85) },
				{ new Integer(80),  new Integer(15), new Integer(150), new Double(0.85) }
		};
	}
	
	/**
	 *  Create a population of Float64Genes
	 */
	public static final Population<Float64Gene, Float64> newFloat64GenePopulation(
		final int ngenes, 
		final int nchromosomes, 
		final int npopulation
	) {
		final Array<Float64Chromosome> chromosomes = 
			new Array<Float64Chromosome>(nchromosomes);
		
		for (int i = 0; i < nchromosomes; ++i) {
			chromosomes.set(i, new Float64Chromosome(0, 10, ngenes));
		}	
		
		final Genotype<Float64Gene> genotype = Genotype.valueOf(chromosomes);
		final Population<Float64Gene, Float64> population = 
			new Population<Float64Gene, Float64>(npopulation);
		
		for (int i = 0; i < npopulation; ++i) {
			population.add(Phenotype.valueOf(genotype.newInstance(), FF, 0));
		}	
		
		return population;
	}
	
	/**
	 * Count the number of different genes.
	 */
	public static int diff(
		final Population<Float64Gene, Float64> p1, 
		final Population<Float64Gene, Float64> p2
	) {
		int count = 0;
		for (int i = 0; i < p1.size(); ++i) {
			final Genotype<?> gt1 = p1.get(i).getGenotype();
			final Genotype<?> gt2 = p2.get(i).getGenotype();
			
			for (int j = 0; j < gt1.length(); ++j) {
				final Chromosome<?> c1 = gt1.getChromosome(j);
				final Chromosome<?> c2 = gt2.getChromosome(j);
				
				for (int k = 0; k < c1.length(); ++k) {
					if (!c1.getGene(k).equals(c2.getGene(k))) {
						++count;
					}
				}
			}
		}
		return count;
	}
	
	/**
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	private static final class Continous 
		implements FitnessFunction<Float64Gene, Float64> 
	{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Float64 evaluate(Genotype<Float64Gene> genotype) {
			return genotype.getChromosome().getGene().getAllele(); 
		}
	}
	
	/**
	 * 'Identity' fitness function.
	 */
	public static final FitnessFunction<Float64Gene, Float64> FF = new Continous();
	
	public static GeneticAlgorithm<Float64Gene, Float64> GA() {
		return new GeneticAlgorithm<Float64Gene, Float64>(
				Genotype.valueOf(new Float64Chromosome(0, 1)), FF
			);
	}
	
	
	public static Phenotype<Float64Gene, Float64> newFloat64Phenotype(final double value) {
		return Phenotype.valueOf(Genotype.valueOf(
				new Float64Chromosome(Float64Gene.valueOf(value, 0, 10))), FF, 0
			);
	}
	
	public static Phenotype<Float64Gene, Float64> newFloat64Phenotype() {
		return newFloat64Phenotype(Math.random()*10);
	}
	
	public static Population<Float64Gene, Float64> newFloat64Population(final int length) {
		final Population<Float64Gene, Float64> population = 
			new Population<Float64Gene, Float64>(length);
		
		for (int i = 0; i < length; ++i) {
			population.add(newFloat64Phenotype());
		}
		
		return population;
	}
	
}
