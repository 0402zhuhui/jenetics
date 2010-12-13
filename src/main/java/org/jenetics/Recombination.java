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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 * 	 Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 * 	 
 */
package org.jenetics;

import static org.jenetics.util.ArrayUtils.subset;

import java.util.Random;

import org.jenetics.util.IndexStream;
import org.jenetics.util.RandomRegistry;

/**
 * <p>
 * An EGA combine elements of existing solutions in order to create a new solution, 
 * with some of the properties of each parent. Recombination creates a new 
 * chromosome by combining parts of two (or more) parent chromosomes. This 
 * combination of chromosomes can be made by selecting one or more crossover 
 * points, splitting these chromosomes on the selected points, and merge those 
 * portions of different chromosomes to form new ones.
 * </p>
 * <p>
 * The recombination probability <i>p</i> determines the probability that a given
 * individual of a population is selected for recombination. The (<i>mean</i>)
 * number of changed individuals depend on the concrete implementation and can 
 * be vary from <i>p</i>&middot;<i>N</i> to 
 * <i>p</i>&middot;<i>N</i>&middot;<i>order</i>.
 * </p>
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public abstract class Recombination<G extends Gene<?, G>> 
	extends AbstractAlterer<G> 
{
	
	private final int _order;
	
	/**
	 * Constructs an alterer with a given recombination probability.
	 * 
	 * @param probability The recombination probability.
	 * @throws IllegalArgumentException if the {@code probability} is not in the
	 * 		  valid range of {@code [0, 1]} or the given {@code order} is smaller
	 *         than two.
	 */
	public Recombination(final double probability, final int order) {
		super(probability);
		if (order < 2) {
			throw new IllegalArgumentException(String.format(
					"Order must be greater than one, but was %d.", order
				));
		}
		_order = order;
	}
	
	/**
	 * Return the number of individuals involved in the 
	 * {@link #recombinate(Population, int[], int)} step.
	 * 
	 * @return the number of individuals involved in the recombination step.
	 */
	public int getOrder() {
		return _order;
	}

	@Override
	public final <C extends Comparable<? super C>> int alter(
		final Population<G, C> population, final int generation
	) {
		final Random random = RandomRegistry.getRandom();
		final int order = Math.min(_order, population.size());
		final IndexStream it = randomIndexes(population.size(), _probability);
		
		int alterations = 0;
		for (int i = it.next(); i != -1; i = it.next()) {
			final int[] individuals = subset(population.size(), order, random);
			individuals[0] = i;
			
			alterations += recombinate(population, individuals, generation);
		}

		return alterations;
	}
	
	/**
	 * Recombination template method.
	 * 
	 * @param <C> the fitness result type
	 * @param population the population to recombine
	 * @param individuals the array with the indexes of the individuals which 
	 *        are involved in the <i>recombination</i> step. The length of the
	 *        array is {@link #getOrder()}. The first individual is the 
	 *        <i>primary</i> individual.
	 * @param generation the current generation.
	 * @return the number of genes that has been altered.
	 */
	protected abstract <C extends Comparable<? super C>> int recombinate(
			final Population<G, C> population, 
			final int[] individuals, 
			final int generation
		);
	
	
}






