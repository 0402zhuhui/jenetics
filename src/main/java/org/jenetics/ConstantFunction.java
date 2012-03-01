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
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *
 */
package org.jenetics;

import static org.jenetics.util.object.eq;
import static org.jenetics.util.object.hashCodeOf;
import static org.jenetics.util.object.nonNull;

import javolution.lang.Immutable;

import org.jenetics.util.Function;

/**
 *  A {@link Function} which always returns a given constant value.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 *
 * @param <G> the gene type.
 */
public final class ConstantFunction<
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
	implements
		Function<Genotype<G>, C>,
		Immutable
{

	private final C _value;

	/**
	 * Create a new <i>constant</i> fitness function with the given value. This
	 * value is returned for every {@link #apply(Genotype)} call.
	 *
	 * @param value the constant value.
	 * @throws NullPointerException if the given {@code value} is {@code null}.
	 */
	public ConstantFunction(final C value) {
		_value = nonNull(value, "Constant value");
	}

	/**
	 * Always return the given constant value.
	 */
	@Override
	public C apply(final Genotype<G> genotype) {
		return _value;
	}

	@Override
	public int hashCode() {
		return hashCodeOf(getClass()).and(_value).value();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ConstantFunction<?, ?>)) {
			return false;
		}

		final ConstantFunction<?, ?> f = (ConstantFunction<?, ?>)obj;
		return eq(_value, f._value);
	}

	@Override
	public String toString() {
		return String.format("%s[%s]", getClass().getSimpleName(), _value);
	}

}
