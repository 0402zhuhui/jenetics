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
 * @version @__new_version__@ &mdash; <em>$Date: 2013-07-03 $</em>
 */
public class ArrayProxyImpl<T> extends ArrayProxy<T> {

	Object[] _array;

	private boolean _sealed = false;

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
	public void uncheckedOffsetSet(final int absoluteIndex, final T value) {
		_array[absoluteIndex] = value;
	}

	@Override
	public ArrayProxyImpl<T> sub(final int start, final int end) {
		return new ArrayProxyImpl<>(_array, start + _start, end + _start);
	}

	@Override
	public void swap(
		final int start,
		final int end,
		final ArrayProxy<T> other,
		final int otherStart
	) {
		cloneIfSealed();
		other.cloneIfSealed();

		for (int i = (end - start); --i >= 0;) {
			final T temp = uncheckedGet(i + start);
			uncheckedSet(i + start, other.uncheckedGet(otherStart + i));
			other.uncheckedSet(otherStart + i, temp);
		}
	}

	@Override
	public void cloneIfSealed() {
		if (_sealed) {
			_array = _array.clone();
			_sealed = false;
		}
	}

	@Override
	public ArrayProxyImpl<T> seal() {
		_sealed = true;
		return new ArrayProxyImpl<>(_array, _start, _end);
	}

	@Override
	public ArrayProxyImpl<T> copy() {
		final ArrayProxyImpl<T> proxy = new ArrayProxyImpl<>(_length);
		System.arraycopy(_array, _start, proxy._array, 0, _length);
		return proxy;
	}

}
