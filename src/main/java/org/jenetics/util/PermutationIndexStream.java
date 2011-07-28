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

import java.util.Random;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
abstract class PermutationIndexStream implements IndexStream {

	final Random _random;
	final int _length;
	int _pos = 0;
	
	PermutationIndexStream(final int length, final Random random) {
		_random = random;
		_length = length;
	}
	
	public int getLength() {
		return _length;
	}
	
	/**
	 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
	 * @version $Id$
	 */
	final static class ArrayPermutation extends PermutationIndexStream {
		
		private final byte[] _array;
		
		ArrayPermutation(final int length, final Random random) {
			super(length, random);
			
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
	final static class PseudoPermutation extends PermutationIndexStream {
		
		private final int _stride;
		private int _start;
		private int _calls = 0;
		
		PseudoPermutation(final int length, final Random random) {
			super(length, random);
			
			_stride = stride(_length, _random);
			_start = _random.nextInt(length);
			_pos = _start;
			
			assert(_stride < _length);
			assert(gcd(_stride, _length) == 1);
			assert(_start < _length);
		}
		
		// The stride has to be smaller than length and relative prime to length.
		private static int stride(final int length, final Random random) {
			int value = length;

			while (gcd(length, value) != 1) {
				value = random.nextInt(length/2) + length/2;
			}	
			
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
		
	}
	
	
	public static IndexStream valueOf(final int length) {
		return valueOf(length, new Random());
	}
	
	public static IndexStream valueOf(final int length, final Random random) {
		if (length < 0) {
			throw new IllegalArgumentException(
					"Length must be greater than zero: " + length
				);
		}
		
		PermutationIndexStream stream = null;
		if (length <= Byte.MAX_VALUE) {
			stream = new ArrayPermutation(length, random);
		} else {
			stream = new PseudoPermutation(length, random);
		}
		return stream;
	}
	

}










