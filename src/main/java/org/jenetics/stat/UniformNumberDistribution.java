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
package org.jenetics.stat;

import java.util.List;

import javolution.text.Text;
import javolution.util.FastList;

import org.jenetics.util.Validator;
import org.jscience.mathematics.function.Function;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.number.Float64;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class UniformNumberDistribution<
	N extends Number & Comparable<? super N>
>
	implements Distribution<N> 
{

	static final class CDF<N extends Number & Comparable<? super N>> 
		extends Function<N, Float64> 
	{
		private static final long serialVersionUID = 1L;
		
		private final List<Variable<N>> 
			_variables = new FastList<Variable<N>>(1);
		
		private final double _min;
		private final double _max;
		private final double _divisor;
		
		public CDF(final Domain<N> domain) {
			_min = domain.getMin().doubleValue();
			_max = domain.getMax().doubleValue();
			_divisor = _max - _min;
			
			_variables.add(new Variable.Local<N>("x"));
		}
		
		@Override
		public Float64 evaluate() {
			final double x = getVariables().get(0).get().doubleValue();
			return Float64.valueOf((x - _min)/_divisor);
		}

		@Override
		public List<Variable<N>> getVariables() {
			return _variables;
		}

		@Override
		public Text toText() {
			return null;
		}
		
	}


	private final Domain<N> _domain;

	/**
	 * Create a new uniform distribution with the given {@code domain}.
	 *
	 * @param domain the domain of the distribution.
	 * @throws NullPointerException if the {@code domain} is {@code null}.
	 */
	public UniformNumberDistribution(final Domain<N> domain) {
		_domain = Validator.nonNull(domain, "Domain");
	}

	/**
	 * Create a new uniform distribution with the given min and max values.
	 *
	 * @param min the minimum value of the domain.
	 * @param max the maximum value of the domain.
	 * @throws IllegalArgumentException if {@code min >= max}
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public UniformNumberDistribution(final N min, final N max) {
		this(new Domain<N>(min, max));
	}

	@Override
	public Domain<N> getDomain() {
		return _domain;
	}

	@Override
	public Function<N, Float64> cdf() {
		return new CDF<N>(_domain);
	}

	@Override
	public Function<N, Float64> pdf() {
		return null;
	}

}









