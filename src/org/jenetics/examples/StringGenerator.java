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
package org.jenetics.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jenetics.CharacterChromosome;
import org.jenetics.CharacterGene;
import org.jenetics.FitnessFunction;
import org.jenetics.GeneticAlgorithm;
import org.jenetics.Genotype;
import org.jenetics.GenotypeFactory;
import org.jenetics.Mutation;
import org.jenetics.SinglePointCrossover;
import org.jenetics.util.ConcurrentEvaluator;
import org.jenetics.util.Probability;
import org.jscience.mathematics.number.Integer64;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: StringGenerator.java,v 1.14 2008-09-29 21:42:49 fwilhelm Exp $
 */
public class StringGenerator {

	private static class Function implements FitnessFunction<CharacterGene, Integer64> {
		private static final long serialVersionUID = 644284481730863472L;
		
		private final String value;
		
		public Function(final String value) { 
			this.value = value;
		}
		
		public Integer64 evaluate(Genotype<CharacterGene> genotype) {
			final CharacterChromosome chromosome = (CharacterChromosome)genotype.getChromosome();
			return Integer64.valueOf(value.length() - levenshtein(value, chromosome));
		}
		
	}
	
	public static void main(String[] args) {
		final int maxThreads = 2;
		final ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
		
		final String value = 
			"To be, or not to be: that is the question: " +
			"Whether 'tis nobler in the mind to suffer...";
		
		final GenotypeFactory<CharacterGene> gtf = Genotype.valueOf(
			new CharacterChromosome(value.length())
		);
		final Function ff = new Function(value);
		final GeneticAlgorithm<CharacterGene, Integer64> 
		ga = new GeneticAlgorithm<CharacterGene, Integer64>(gtf, ff);
		
		ga.setPopulationSize(5000);
		ga.setSurvivorFraction(Probability.valueOf(0.3));
		ga.setOffspringFraction(Probability.valueOf(0.7));
		ga.setMaximalPhenotypeAge(30);
//		ga.setSelectors(new RouletteWheelSelector<CharacterGene, Integer64>());
//		ga.setSelectors(new LinearRankSelector<CharacterGene, Integer64>());
//		ga.setSelectors(new BoltzmannSelector<CharacterGene, Integer64>());
		ga.setAlterer(
			new Mutation<CharacterGene>(Probability.valueOf(0.3)).append(
			new SinglePointCrossover<CharacterGene>(Probability.valueOf(0.1))
		));
		ga.setFitnessEvaluator(new ConcurrentEvaluator());

		GAUtils.execute(ga, 25);
		
		pool.shutdown();
	}
	
	
	/**
	 * Return Levenshtein distance of two character sequences.
	 */
	private static int levenshtein(final CharSequence s, final CharSequence t) {
		//Step 1:
		final int n = s.length();
		final int m = t.length();
		if (n == 0 || m == 0) {
			return Math.max(n, m);
		}
		
		//Step 2:
		int d[][] = new int[n + 1][m +1];
		for (int i = 0; i <= n; ++i) {
			d[i][0] = i;
		}
		for (int j = 0; j <= m; ++j) {
			d[0][j] = j;
		}
		
		//Step 3:
		for (int i = 1; i <= n; ++i) {
			final char si = s.charAt(i - 1);
			
			//Step 4:
			for (int j = 1; j <= m; ++j) {
				final char tj = t.charAt(j - 1);
				
				//Step 5:
				int cost = 0;
				if (si == tj) {
					cost = 0;
				} else {
					cost = 1;
				}
				
				//Step 6:
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
			}
		}
		
		//Step 7:
		return d[n][m];
	}

	private static int min(final int a, final int b, final int c) {
		int m = a;
		if (b < m) {
			m = b;
		}
		if (c < m) {
			m = c;
		}
		return m;
	}
	
}
