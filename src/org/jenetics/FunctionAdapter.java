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

import org.jscience.mathematics.function.Function;

/**
 * Adapter class to allow the genetic algorithm to interact with the 
 * {@link Function} object of the <a href="http://jscience.org/">JScience</a> 
 * library.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
public class FunctionAdapter<G extends Gene<?, G>, C extends Comparable<C>> 
	implements FitnessFunction<G, C> 
{
	private static final long serialVersionUID = 8415208410215704341L;

	private final Function<Genotype<G>, C> _adoptee;
	
	/**
	 * Create a new {@link FitnessFunction} with the given 
	 * <a href="http://jscience.org/">JScience</a> {@link Function} object.
	 * 
	 * @param adoptee the <a href="http://jscience.org/">JScience</a>
	 *        {@link Function} object.
	 * @throws NullPointerException if the function {@code adoptee} is 
	 *        {@code null}.
	 */
	public FunctionAdapter(final Function<Genotype<G>, C> adoptee) {
		_adoptee = nonNull(adoptee, "Fitness function");
	}
	
	@Override
	public C evaluate(final Genotype<G> genotype) {
		return _adoptee.evaluate(genotype);
	}

}
