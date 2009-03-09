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

import static java.lang.Math.ceil;
import static org.jenetics.util.ArrayUtils.subset;

import java.util.Random;

import org.jenetics.util.Array;
import org.jenetics.util.Probability;
import org.jenetics.util.RandomRegistry;


/**
 * This class is for mutating a chromosomoes of an given population. There are 
 * two distinct roles mutation plays
 * <ul>
 * 	<li>Exploring the search space. By making small moves mutation allows a
 * 	population to explore the search space. This exploration is often slow
 * 	compared to crossover, but in problems where crossover is disruptive this
 * 	can be an important way to explore the landscape.
 * 	</li>
 * 	<li>Maintaining diversity. Mutation prevents a population from
 * 	correlating. Even if most of the search is being performed by crossover,
 * 	mutation can be vital to provide the diversity which crossover needs.
 * 	</li>
 * </ul>
 * 
 * The mutation rate is the parameter that must be optimized. The optimal value 
 * of the mutation rate depends on the role mutation plays. If mutation is the 
 * only source of exploration (if there is no crossover) then the mutation rate 
 * should be set so that a reasonable neighbourhood of solutions is explored. 
 * </p>
 * The mutation probability is the probability that a specific gene over the 
 * whole population is mutated. The number of available genes of an population
 * is <pre>genes = p*c*g</pre> where {@code p} is the population size, {@code c} 
 * the number of chromosomes of the genotypes and {@code g} the number of genes 
 * in the chromosomes. So the number of genes mutated by the mutation is 
 * <pre>genes*mutation-probability.</pre>
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Mutation.java,v 1.16 2009-03-09 22:31:15 fwilhelm Exp $
 */
public class Mutation<G extends Gene<?, G>> extends Alterer<G> {	
	private static final long serialVersionUID = -7012689808565856577L;

	protected int _mutations = 0;
	
	/**
	 * Default constructor, with probability = 0.01.
	 */
	public Mutation() {
		this(Probability.valueOf(0.01));
	}
	
	/**
	 * Construct a Mutation object which a given mutation probability.
	 * 
	 * @param probability Mutation probability. The given probability is
	 * 	      devided by the number of chromosomes of the genotype to form
	 * 	      the concrete mutation probability.
	 * @throws NullPointerException if the <code>probability</code> is 
	 * 		<code>null</code>.
	 */
	public Mutation(final Probability probability) {
		super(probability);
	}

	/**
	 * Construct a Mutation object which a given mutation probability
	 * 
	 * @param probability Mutation probability. The given probability is
	 *        devided by the number of chromosomes of the genotype to form
	 *        the concrete mutation probability.
	 * @param component The next Alterers in Alterer-Chain.
	 */
	public Mutation(final Probability probability, final Alterer<G> component) {
		super(probability, component);
	}
	
	/**
	 * Return the number of mutations performed by this mutation object.
	 * 
	 * @return the number of mutations performed so far.
	 */
	public int getMutations() {
		return _mutations;
	}
 
	/**
	 * Concrete implementation of the alter method.
	 */
	@Override
	protected <C extends Comparable<C>> void change(
		final Population<G, C> population, final int generation
	) {
		assert(population != null) : "Not null is guaranteed from base class.";
		
		final Random random = RandomRegistry.getRandom();
		final int[] elements = subset(
				population.size(), 
				(int)ceil(population.size()*_probability.doubleValue()), 
				random
			);
						
		for (int i = 0; i < elements.length; ++i) {
			final Phenotype<G, C> phenotype = population.get(elements[i]);
			final Genotype<G> genotype = phenotype.getGenotype(); 
			
			population.set(
				elements[i], 
				phenotype.newInstance(mutate(genotype), generation)
			);
		}
	}
	
	private Genotype<G> mutate(final Genotype<G> genotype) {
		final Random random = RandomRegistry.getRandom();
		final int[] elements = subset(
				genotype.length(), 
				(int)ceil(genotype.length()*_probability.doubleValue()), 
				random
			);
		
		final Array<Chromosome<G>> chromosomes = genotype.getChromosomes().copy(); 
		for (int i = 0; i < elements.length; ++i) {
			final Chromosome<G> chromosome = chromosomes.get(elements[i]);
			final Array<G> genes = chromosome.toArray().copy();
			mutate(genes);
			chromosomes.set(elements[i], chromosome.newInstance(genes));
		}
				
		return Genotype.valueOf(chromosomes);
	}
	
	/**
	 * Template method which gives an (re)implemention of the mutation class the
	 * possibility to perform its own mutation operation, based on a writable
	 * gene array. 
	 * <p/>
	 * The {@link SwapMutation} does it in this way:
	 * [code]
	 * protected void mutate(final Array<G> genes) {
	 *     final Random random = RandomRegistry.getRandom();
	 *     final int subsetSize = (int)Math.ceil(genes.length()*_probability.doubleValue());
	 *     final int[] elements = subset(genes.length(), subsetSize, random);
	 *
	 *     for (int i = 0; i < elements.length; ++i) {
	 *         ArrayUtils.swap(genes, elements[i], random.nextInt(genes.length()));
	 *     }
	 *
	 *     _mutations += elements.length;
	 * }
	 * [/code]
	 * 
	 * @param genes the genes to mutate.
	 */
	protected void mutate(final Array<G> genes) {
		final Random random = RandomRegistry.getRandom();
		final int subsetSize = (int)ceil(genes.length()*_probability.doubleValue());
		final int[] elements = subset(genes.length(), subsetSize, random);
		
		for (int i = 0; i < elements.length; ++i) {
			genes.set(elements[i], genes.get(elements[i]).newInstance());
		}
		
		_mutations += elements.length;
	}

}




