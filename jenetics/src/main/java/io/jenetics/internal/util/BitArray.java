/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.internal.util;

import static java.lang.String.format;

import java.math.BigInteger;
import java.util.Objects;

/**
 * This class represents a fixed sized array of <em>bit</em> or <em>boolean</em>
 * values, backed by a {@code byte[]} array. The order of the bit values is shown
 * if the drawing.
 * <pre> {@code
 *  Byte:       3        2        1        0
 *              |        |        |        |
 *  Array: |11110011|10011101|01000000|00101010|
 *          |                 |        |      |
 *  Bit:    23                15       7      0
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public final class BitArray {

	private static final int[] BITS = {1, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

	final byte[] _data;
	private final int _begin;
	private final int _end;

	/**
	 * Create a new bit-array with the given {@code data} values and
	 * {@code begin} and {@code end} <em>bit</em> indexes.
	 *
	 * @param data the {@code byte[]} array which contains the bit data
	 * @param begin the start bit index (inclusively)
	 * @param end the end bit index (exclusively)
	 * @throws NullPointerException if the given {@code data} array is
	 *         {@code null}
	 * @throws IllegalArgumentException if the {@code begin} and {@code end}
	 *         indexes are not within the valid range
	 */
	BitArray(final byte[] data, final int begin, final int end) {
		if (data.length == 0) {
			throw new IllegalArgumentException("Byte array must not be empty.");
		}
		if (begin < 0) {
			throw new IllegalArgumentException(
				"Begin index is smaller then zero: " + begin
			);
		}
		if (end < begin || end > data.length*Byte.SIZE) {
			throw new IllegalArgumentException(format(
				"End index is not within the valid range of [%d, %d]: %d",
				begin, data.length*Byte.SIZE, end
			));
		}

		_data = data;
		_begin = begin;
		_end = end;
	}

	/**
	 * Create a new bit-array with the given {@code data} values.
	 *
	 * @param data the {@code byte[]} array which contains the bit data
	 * @throws NullPointerException if the given {@code data} array is
	 *         {@code null}
	 */
	BitArray(final byte[] data) {
		this(data, 0, data.length*Byte.SIZE);
	}

	/**
	 * Return the length of the bit-array.
	 *
	 * @return the length of the bit array
	 */
	public int length() {
		return _end - _begin;
	}

	/**
	 * Return the number of set bits of this bit-array.
	 *
	 * @return the number of set bits
	 */
	public int bitCount() {
		return Bits.count(_data, _begin,_end);
	}

	/**
	 * Sets the specified bit {@code value} at the given bit {@code index}.
	 *
	 * @param index the bit index
	 * @param value the bit value
	 * @throws IndexOutOfBoundsException if the index is not within the valid
	 *         range of {@code [0, length())}
	 */
	public void set(final int index, final boolean value) {
		Objects.checkIndex(index, length());
		Bits.set(_data, _begin + index, value);
	}

	/**
	 * Inverts {@code this} bit-array.
	 */
	public void invert() {
		Bits.invert(_data);
	}

	/**
	 * Return the bit value at the given bit {@code index}.
	 *
	 * @param index the bit index
	 * @return the bit value
	 * @throws IndexOutOfBoundsException if the index is not within the valid
	 *         range of {@code [0, length())}
	 */
	public boolean get(final int index) {
		Objects.checkIndex(index, length());
		return Bits.get(_data, _begin + index);
	}

	/**
	 * Check if the integer, represented by this bit-array (in two's complement
	 * representation), is negative.
	 *
	 * @return {@code true} if <em>this</em> integer is negative, {@code false}
	 *         otherwise
	 */
	public boolean isNegative() {
		return get(length() - 1);
	}

	/**
	 * Return the value of this bit-array as {@link BigInteger} value. This
	 * bit-array can be recreated by the returned {@code BigInteger} value. But
	 * only with the same {@link #length()} of {@code this} bit-array.
	 *
	 * <pre>{@code
	 * final var bits = BitArray.of("1111111010100110010110110010011110110101");
	 * final var bint = bits.toBigInteger();
	 * assert BitArray.of(bint, bits.length()).equals(bits);
	 * }</pre>
	 *
	 * @see #of(BigInteger, int)
	 *
	 * @return a new {@code BigInteger} object, which represents the integer
	 *         value of this bit-array
	 */
	public BigInteger toBigInteger() {
		return new BigInteger(toTowsComplementByteArray());
	}

	/**
	 * Returns the two's complement binary representation of this
	 * big integer. The output array is in <i>big-endian</i>
	 * byte-order: the most significant byte is at the offset position.
	 *
	 * <p>Note: This representation is consistent with {@code java.lang.BigInteger}
	 * byte array representation and can be used for conversion between the two
	 * classes.</p>
	 *
	 * @return the two's complement byte array representation
	 * @throws IndexOutOfBoundsException
	 *         if {@code bytes.length < (int)Math.ceil(length()/8.0)}
	 * @throws NullPointerException it the give array is {@code null}.
	 */
	byte[] toTowsComplementByteArray() {
		final byte[] array = Bits.newArray(length());

		for (int i = 0; i < length(); ++i) {
			Bits.set(array, i, get(i));
		}
		if (isNegative()) {
			for (int i = 0, n = array.length*8; i < n - length(); ++i) {
				Bits.set(array, length() + i);
			}
		}
		Bits.reverse(array);
		return array;
	}

	public byte[] toByteArray() {
		final byte[] array = Bits.newArray(length());
		for (int i = 0; i < length(); ++i) {
			Bits.set(array, i, get(i));
		}
		return array;
	}

	/**
	 * Create a new copy of {@code this} bit-array.
	 *
	 * @return a new copy of {@code this} bit-array
	 */
	public BitArray copy() {
		final byte[] array = Bits.newArray(length());
		for (int i = 0; i < length(); ++i) {
			Bits.set(array, i, get(i));
		}

		return new BitArray(array, 0, length());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof BitArray &&
			equals((BitArray)obj);
	}

	private boolean equals(final BitArray array) {
		if (array.length() != length()) return false;
		for (int i = 0; i < length(); ++i) {
			if (get(i) != array.get(i)) return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final char[] chars = new char[length()];
		for (int i = 0; i < chars.length; ++i) {
			chars[chars.length - 1 - i] = get(i) ? '1' : '0';
		}

		return new String(chars);
	}

	/**
	 * Convert a binary representation of {@code this} bit-array to a string. The
	 * string has the following format:
	 * <pre>
	 *  Byte:       3        2        1        0
	 *              |        |        |        |
	 *  Array: "11110011|10011101|01000000|00101010"
	 *          |                 |        |      |
	 *  Bit:    23                15       7      0
	 * </pre>
	 *
	 * @return the binary representation of {@code this} bit array.
	 */
	public String toByteString() {
		final byte[] array = Bits.newArray(length());
		for (int i = 0; i < length(); ++i) {
			Bits.set(array, i, get(i));
		}
		return Bits.toByteString(array);
	}

	/* *************************************************************************
	 * Static factory methods.
	 * ************************************************************************/

	/**
	 * Creates a new bit-array from the given {@code value} and the given
	 * {@code length}. It is guaranteed, that the created bit-array will
	 * represent the given {@link BigInteger}, as long as the {@code length}
	 * is big enough to store the whole value. If the length is shorter then
	 * required, the higher order bits will be truncated.
	 *
	 * <pre>{@code
	 * final var length = 2048;
	 * final var bint = BigInteger.probablePrime(length, new Random());
	 * final var bits = BitArray.of(bint, length + 1);
	 * assert bits3.toBigInteger().equals(bint);
	 * }</pre>
	 *
	 * @see #toBigInteger()
	 *
	 * @param value the integer value
	 * @param length the length of the created bit-array
	 * @return a newly created bit-array which represent the given {@code value}
	 * @throws NullPointerException if the given {@code value} is {@code null}
	 * @throws NegativeArraySizeException if the {@code length} is negative
	 */
	public static BitArray of(final BigInteger value, final int length) {
		final byte[] array = Bits.newArray(length);
		final byte[] data = value.toByteArray();

		Bits.reverse(data);
		if (value.signum() < 0) {
			java.util.Arrays.fill(array, (byte)-1);
		}
		for (int i = 0, n = Math.min(length, data.length*8); i < n; ++i) {
			Bits.set(array, i, Bits.get(data, i));
		}

		return new BitArray(array, 0, length);
	}

	/**
	 * Create a new bit-array from the given {@link BigInteger} value.
	 *
	 * @see #of(BigInteger, int)
	 *
	 * @param value the integer value
	 * @return a newly created bit-array which represent the given {@code value}
	 * @throws NullPointerException if the given {@code value} is {@code null}
	 */
	public static BitArray of(final BigInteger value) {
		final byte[] data = value.toByteArray();
		final byte[] array = new byte[data.length];

		Bits.reverse(data);
		if (value.signum() < 0) {
			java.util.Arrays.fill(array, (byte)-1);
		}
		for (int i = 0, n = data.length*Byte.SIZE; i < n; ++i) {
			Bits.set(array, i, Bits.get(data, i));
		}

		return new BitArray(array, 0, data.length*Byte.SIZE);
	}

	/**
	 * Creates a new bit-array from the given string {@code value}. The string,
	 * created by the {@link #toString()} method, will be equals to the given
	 * input {@code value}.
	 *
	 * <pre>{@code
	 * final var string = "11111110101001100101101100100111101101011101";
	 * final var bits = BitArray.of(string);
	 * assert bits.toString().equals(string);
	 * }</pre>
	 *
	 * @see #toString()
	 *
	 * @param value the given input string, consisting only of '0's and '1's
	 * @return a new bit-array from the given input {@code value}
	 * @throws IllegalArgumentException if the given input {@code value} is
	 *         empty
	 */
	public static BitArray of(final CharSequence value) {
		final byte[] data = toByteArray(value, value.length());
		return new BitArray(data, 0, value.length());
	}

	/**
	 * Creates a new bit-array from the given string {@code value}. The string,
	 * created by the {@link #toString()} method, will be equals to the given
	 * input {@code value}.
	 *
	 * @see #of(CharSequence)
	 * @see #toString()
	 *
	 * @param value the given input string, consisting only of '0's and '1's
	 * @param length the length of the created bit-array
	 * @return a new bit-array from the given input {@code value}
	 * @throws IllegalArgumentException if the given input {@code value} is
	 *         empty
	 */
	public static BitArray of(final CharSequence value, final int length) {
		final byte[] data = toByteArray(value, length);
		return new BitArray(data, 0, value.length());
	}

	private static byte[] toByteArray(final CharSequence chars, final int length) {
		final byte[] array = Bits.newArray(length);
		for (int i = 0, j = length - 1;
			 i < array.length;
			 i++, j -= Byte.SIZE)
		{
			for (int bits = 0; bits < BITS.length && (j - bits) >= 0; ++bits) {
				if (chars.charAt(j - bits) == '1') {
					array[i] |= BITS[bits];
				}
			}
		}
		return array;
	}

	/**
	 * Create a new bit-array with the given {@code data} values and
	 * {@code begin} and {@code end} <em>bit</em> indexes.
	 *
	 * @param data the {@code byte[]} array which contains the bit data
	 * @param begin the start bit index (inclusively)
	 * @param end the end bit index (exclusively)
	 * @return a newly created bit-array
	 * @throws NullPointerException if the given {@code data} array is
	 *         {@code null}
	 * @throws IllegalArgumentException if the {@code begin} and {@code end}
	 *         indexes are not within the valid range
	 */
	public static BitArray of(final byte[] data, final int begin, final int end) {
		return new BitArray(data.clone(), begin, end);
	}

	/**
	 * Create a new bit-array with the given {@code data} values and
	 * {@code length}.
	 *
	 * @param data the {@code byte[]} array which contains the bit data
	 * @param length the bit length
	 * @return a newly created bit-array
	 * @throws NullPointerException if the given {@code data} array is
	 *         {@code null}
	 * @throws IllegalArgumentException if the {@code length} is greater than
	 *         {@code data.length*Byte.SIZE}
	 */
	public static BitArray of(final byte[] data, final int length) {
		return of(data, 0, length);
	}

	/**
	 * Create a new bit-array with the given {@code data} values and
	 * {@code length}.
	 *
	 * @param data the {@code byte[]} array which contains the bit data
	 * @return a newly created bit-array
	 * @throws NullPointerException if the given {@code data} array is
	 *         {@code null}
	 */
	public static BitArray of(final byte[] data) {
		return of(data, 0, data.length*Byte.SIZE);
	}

	/**
	 * Crate a new bit-array with the given length. All bits are set to '0'.
	 *
	 * @param length the length of the bit-array
	 * @return a newly created bit-array with the given {@code length}
	 * @throws IllegalArgumentException if the given {@code length} is smaller
	 *         then one
	 */
	public static BitArray ofLength(final int length) {
		return new BitArray(Bits.newArray(length), 0, length);
	}

	/**
	 * Create a new bit-array which can store at least the number
	 * of bits as defined by the given {@code length} parameter. The returned
	 * byte array is initialized with ones according to the given ones
	 * probability {@code p}.
	 *
	 * @param length the number of bits, the returned bit-array can store.
	 * @param p the ones probability of the returned byte array.
	 * @return the new byte array.s
	 * @throws IllegalArgumentException if {@code p} is not a valid probability.
	 */
	public static BitArray ofLength(final int length, final double p) {
		return new BitArray(Bits.newArray(length, p), 0, length);
	}

}
