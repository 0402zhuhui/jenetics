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

import static org.jenetics.util.Validator.nonNull;

/**
 * In truncation selection individuals are sorted according to their fitness. 
 * Only the best individuals are selected. 
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Truncation_selection">
 * 			Wikipedia: Truncation selection
 *      </a>
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class TruncationSelector<G extends Gene<?, G>, C extends Comparable<C>>
	implements Selector<G, C>
{
	private static final long serialVersionUID = -238059226601411086L;

	public TruncationSelector() {
	}
	
	/**
	 * This method sorts the population in descending order while calculating the
	 * selection probabilities. (The method {@link Population#sort()} is called
	 * by this method.)
	 * 
	 * @throws IllegalArgumentException if the sample size is greater than the
	 *         population size or {@code count} is greater the the population 
	 *         size.
	 * @throws NullPointerException if the {@code population} is {@code null}.
	 */
	@Override
	public Population<G, C> select(
		final Population<G, C> population, 
		final int count,
		final Optimization opt
	) {		
		nonNull(population, "Population");
		nonNull(opt, "Optimization");
		if (count < 0) {
			throw new IllegalArgumentException(String.format(
				"Selection count must be greater or equal then zero, but was %s",
				count
			));
		}
		if (count > population.size()) {
			throw new IllegalArgumentException(String.format(
				"Selection size greater than population size: %s > %s",
				count, population.size()
			));
		}
		
		population.sort();
		
		final Population<G, C> selected = new Population<G, C>(count);
		switch (opt) {
			case MAXIMIZE:
				for (int i = 0; i < count; ++i) {
					selected.add(population.get(i));
				}
				break;
			case MINIMIZE:
				final int n = population.size() - count;
				for (int i = population.size() - 1; i >= n; --i) {
					selected.add(population.get(i));
				}
				break;
		}
		
		return selected;
	}

}











