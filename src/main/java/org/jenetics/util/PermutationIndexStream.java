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
package org.jenetics.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
final class ArrayPermutation implements IndexStream {
	
	private final byte[] _array;
	private int _pos = 0;
	
	ArrayPermutation(final int length, final Random random) {
		assert(length <= Byte.MAX_VALUE);
		
		_array = new byte[length];
		for (int i = 0; i < length; ++i) {
			_array[i] = (byte)i;
		}
		for (int j = _array.length - 1; j > 0; --j) {
			swap(_array, j, random.nextInt(j + 1));
		}
	}
	
	private static void swap(final byte[] array, final int i, final int j) {
		final byte temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	@Override
	public int next() {
		int next = -1;
		if (_pos < _array.length) {
			next = _array[_pos++];
		}
		
		return next;
	}
}

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class PermutationIndexStream implements IndexStream {
		
	private final int _length;
	private final int _stride;
	
	private int _start;
	private int _pos = 0;
	private int _calls = 0;
	
	
	PermutationIndexStream(final int length, final Random random) {
		_length = length;
		
		// The stride has to be smaller than length and relative prime to length.
		_stride = stride(length, random);
		_start = random.nextInt(length);
		_pos = _start;
	}
	
	private static int stride(final int length, final Random random) {
		int value = length;

		while (value >= length || gcd(length, value) != 1) {
			value = random.nextInt(length/2) + length/2;
		}	
		
		System.out.println("lenght: " + length + ", stride: " + value);
		return value;
	}
	
	private static int gcd(final int a, final int b) {
		int x = a;
		int y = b;
		int mod = x%y;
		
		while (mod != 0) {
			x = y;
			y = mod;
			mod = x%y;
		}
		
		return y;
	}
	
	@Override
	public int next() {
		int next = -1;
		
		if (_calls < _length) {
			if (_pos >= _length) {
				_start = (_start + 1)%_length;
				_pos = _start;
			}

			next = _pos;
			_pos = (_pos + _stride)%_length;
			++_calls;
		}
		
		return next;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Length: %d, start: %d, stride: %d",
				_length, _start, _stride
			);
	}
	
	public static IndexStream valueOf(final int length) {
		if (length >= 0) {
			if (length <= Byte.MAX_VALUE) {
				return new ArrayPermutation(length, new Random());
			} else {
				return new PermutationIndexStream(length, new Random());
			}
		} else {
			throw new IllegalArgumentException(
					"Length must be greater than zero: " + length
				);
		}
	}
	
	
	public static void main(String[] args) {
		for (int length = 129; length < 1000; ++length) {
			System.out.println("Test length " + length);
			for (int i = 0; i < 1000; ++i) {
				test(length);
			}
		}
	}
	
	private static void test(final int length) {
		final Set<Integer> values = new HashSet<Integer>(length);
		final IndexStream stream = PermutationIndexStream.valueOf(length);
		
		int count = 0;
		for (int i = stream.next(); i != -1; i = stream.next()) {
			if (values.contains(i)) {
				System.out.println((count++) + " --> " + i);
				System.out.println("Double value: " + i);
				System.out.println(stream);
				System.exit(1);
			}
			values.add(i);
		}
		
		for (int i = 0; i < length; ++i) {
			if (!values.contains(i)) {
				System.out.println("Missing value: " + i);
				System.out.println(stream);
				System.exit(1);
			}
		}
	}

}

