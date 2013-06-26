/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
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
package org.jenetics.internal.util;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since @__new_version__@
 * @version @__new_version__@ &mdash; <em>$Date$</em>
 */
public class ArrayProxyImpl<T> extends ArrayProxy<T> {

	final Object[] _array;

	public ArrayProxyImpl(final Object[] array, final int start, final int end) {
		super(start, end);
		_array = array;
	}

	public ArrayProxyImpl(final int length) {
		this(new Object[length], 0, length);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T uncheckedOffsetGet(final int absoluteIndex) {
		return (T)_array[absoluteIndex];
	}

	@Override
	public ArrayProxyImpl<T> sub(final int start, final int end) {
		return new ArrayProxyImpl<>(_array, start + _start, end + _start);
	}

}
