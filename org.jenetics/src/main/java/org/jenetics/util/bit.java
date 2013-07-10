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
 * 	 Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *
 */
package org.jenetics.util;

import static java.lang.Math.min;

import org.jscience.mathematics.number.LargeInteger;


/**
 * Some bit utils. All operation assume <a href="http://en.wikipedia.org/wiki/Endianness">
 * <b>little-endian</b></a> byte order.
 *
 * <pre>
 *  Byte:       3        2        1        0
 *              |        |        |        |
 *  Array: |11110011|10011101|01000000|00101010|
 *          |                 |        |      |
 *  Bit:    23                15       7      0
 * </pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since 1.0
 * @version 1.4 &mdash; <em>$Date$</em>
 */
public final class bit extends StaticObject {
	private bit() {}

	/**
	 * Lookup table for counting the number of set bits in an {@code byte} value.
	 */
	private static final byte[] BIT_SET_TABLE = new byte[] {
		(byte)1, (byte)2, (byte)2, (byte)3, (byte)2, (byte)3, (byte)3, (byte)4,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)4, (byte)5, (byte)5, (byte)6, (byte)5, (byte)6, (byte)6, (byte)7,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)4, (byte)5, (byte)5, (byte)6, (byte)5, (byte)6, (byte)6, (byte)7,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)4, (byte)5, (byte)5, (byte)6, (byte)5, (byte)6, (byte)6, (byte)7,
		(byte)4, (byte)5, (byte)5, (byte)6, (byte)5, (byte)6, (byte)6, (byte)7,
		(byte)5, (byte)6, (byte)6, (byte)7, (byte)6, (byte)7, (byte)7, (byte)8,
		(byte)0, (byte)1, (byte)1, (byte)2, (byte)1, (byte)2, (byte)2, (byte)3,
		(byte)1, (byte)2, (byte)2, (byte)3, (byte)2, (byte)3, (byte)3, (byte)4,
		(byte)1, (byte)2, (byte)2, (byte)3, (byte)2, (byte)3, (byte)3, (byte)4,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)1, (byte)2, (byte)2, (byte)3, (byte)2, (byte)3, (byte)3, (byte)4,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)1, (byte)2, (byte)2, (byte)3, (byte)2, (byte)3, (byte)3, (byte)4,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)2, (byte)3, (byte)3, (byte)4, (byte)3, (byte)4, (byte)4, (byte)5,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)3, (byte)4, (byte)4, (byte)5, (byte)4, (byte)5, (byte)5, (byte)6,
		(byte)4, (byte)5, (byte)5, (byte)6, (byte)5, (byte)6, (byte)6, (byte)7
	};

	private static final int BIT_SET_TABLE_INDEX_OFFSET = 128;

	/**
	 * Return the (boolean) value of the byte array at the given bit index.
	 *
	 * @param data the byte array.
	 * @param index the bit index.
	 * @return the value at the given bit index.
	 * @throws IndexOutOfBoundsException if the index is
	 *          {@code index >= max || index < 0}.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static boolean get(final byte[] data, final int index) {
		return (data[index >>> 3] & (1 << (index & 7))) != 0;
	}

	/**
	 * Set the bit in the given byte array at the bit position (not the index
	 * within the byte array) to the specified value.
	 *
	 * @param data the byte array.
	 * @param index the bit index within the byte array.
	 * @param value the value to set.
	 * @return the given data array.
	 * @throws IndexOutOfBoundsException if the index is
	 *         {@code index >= max || index < 0}.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] set(
		final byte[] data,
		final int index,
		final boolean value
	) {
		return value ? set(data, index) : unset(data, index);
	}

	/**
	 * Set the bit in the given byte array at the bit position (not the index
	 * within the byte array) to {@code true}.
	 *
	 * @param data the byte array.
	 * @param index the bit index within the byte array.
	 * @return the given data array.
	 * @throws IndexOutOfBoundsException if the index is
	 *          {@code index >= max || index < 0}.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] set(final byte[] data, final int index) {
		data[index >>> 3] |= 1 << (index & 7);
		return data;
	}

	/**
	 * Set the bit in the given byte array at the bit position (not the index
	 * within the byte array) to {@code false}.
	 *
	 * @param data the byte array.
	 * @param index the bit index within the byte array.
	 * @return the given data array.
	 * @throws IndexOutOfBoundsException if the index is
	 *          {@code index >= max || index < 0}.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] unset(final byte[] data, final int index) {
		data[index >>> 3] &= ~(1 << (index & 7));
		return data;
	}

	/**
	 * Swap a given range with a range of the same size with another array.
	 *
	 * <pre>
	 *                start            end
	 *                  |               |
	 * data:      +---+---+---+---+---+---+---+---+---+---+---+---+
	 *              +---------------+
	 *                              +---------------+
	 * otherData: +---+---+---+---+---+---+---+---+---+---+---+---+
	 *                              |
	 *                          otherStart
	 * </pre>
	 *
	 * @param data the first byte array which are used for swapping.
	 * @param start the start bit index of the {@code data} byte array,
	 *        inclusively.
	 * @param end the end bit index of the {@code data} byte array, exclusively.
	 * @param otherData the other byte array to swap the elements with.
	 * @param otherStart the start index of the {@code otherData} byte array.
	 * @throws IndexOutOfBoundsException if {@code start > end}.
	 * @throws IndexOutOfBoundsException if {@code start < 0 ||
	 *         end >= data.length*8 || otherStart < 0 ||
	 *         otherStart + (end - start) >= otherData.length*8}
	 */
	public static void swap(
		final byte[] data, final int start, final int end,
		final byte[] otherData, final int otherStart
	) {
		for (int i = (end - start); --i >= 0;) {
			final boolean temp = get(data, i + start);
			set(data, i + start, get(otherData, otherStart + i));
			set(otherData, otherStart + i, temp);
		}

	}

	/**
	 * Returns the number of one-bits in the given {@code byte} array.
	 *
	 * @param data the {@code byte} array for which the one bits should be
	 *        counted.
	 * @return the number of one bits in the given {@code byte} array.
	 */
	public static int count(final byte[] data) {
		int count = 0;
		for (int i = data.length; --i >= 0;) {
			count += count(data[i]);
		}
		return count;
	}

	/**
	 * Returns the number of one-bits in the given {@code byte} {@code value}.
	 *
	 * @param value the value for which the one bits should be counted.
	 * @return the number of one bits in the given value
	 */
	public static int count(final byte value) {
		return BIT_SET_TABLE[value + BIT_SET_TABLE_INDEX_OFFSET];
	}

	/**
	 * Shifting all bits in the given <code>data</code> array the given
	 * {@code shift} to the right. The bits on the left side are filled with
	 * zeros.
	 *
	 * @param data the data bits to shift.
	 * @param shift the number of bits to shift.
	 * @return the given <code>data</code> array.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] shiftRight(final byte[] data, final int shift) {
		final int bytes = min(shift >>> 3, data.length);
		final int bits = shift & 7;

		if (bytes > 0) {
			for (int i = 0, n = data.length - bytes; i < n; ++i) {
				data[i] = data[i + bytes];
			}
			for (int i = data.length, n = data.length - bytes; --i >= n;) {
				data[i] = (byte)0;
			}
		}
		if (bits > 0 && bytes < data.length) {
			int carry = 0;
			int nextCarry = 0;

			for (int i = data.length; --i >= 0;) {
				int d = data[i] & 0xFF;
				nextCarry = (d << (8 - bits));

				d >>>= bits;
				d |= carry;
				data[i] = (byte)(d & 0xFF);

				carry = nextCarry;
			}
		}


		return data;
	}

	/**
	 * Shifting all bits in the given <code>data</code> array the given
	 * {@code shift} to the left. The bits on the right side are filled with
	 * zeros.
	 *
	 * @param data the data bits to shift.
	 * @param shift the number of bits to shift.
	 * @return the given <code>data</code> array.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] shiftLeft(final byte[] data, final int shift) {
		final int bytes = min(shift >>> 3, data.length);
		final int bits = shift & 7;

		if (bytes > 0) {
			for (int i = 0, n = data.length - bytes; i < n; ++i) {
				data[data.length - 1 - i] = data[data.length - 1 - i - bytes];
			}
			for (int i = 0; i < bytes; ++i) {
				data[i] = (byte)0;
			}
		}
		if (bits > 0 && bytes < data.length) {
			int carry = 0;
			int nextCarry = 0;

			for (int i = bytes; i < data.length; ++i) {
				int d = data[i] & 0xFF;
				nextCarry = (d >>> (8 - bits));

				d <<= bits;
				d |= carry;
				data[i] = (byte)(d & 0xFF);

				carry = nextCarry;
			}
		}

		return data;
	}

	/**
	 * Increment the given <code>data</code> array.
	 *
	 * @param data the given <code>data</code> array.
	 * @return the given <code>data</code> array.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] increment(final byte[] data) {
		boolean carry = true;
		int index = 0;

		while (index < data.length && carry) {
			int d = data[index] & 0xFF;
			++d;
			data[index++] = (byte)d;

			carry = d > 0xFF;
		}

		return data;
	}

	/**
	 * Invert the given <code>data</code> array.
	 *
	 * @param data the given <code>data</code> array.
	 * @return the given <code>data</code> array.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] invert(final byte[] data)	{
		int d = 0;
		for (int i = 0; i < data.length; ++i) {
			d = data[i] & 0xFF;
			d = ~d;
			data[i] = (byte)d;
		}

		return data;
	}

	/**
	 * Make the two's complement of the given <code>data</code> array.
	 *
	 * @param data the given <code>data</code> array.
	 * @return the given <code>data</code> array.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] complement(final byte[] data) {
		return increment(invert(data));
	}

	/**
	 * Flip the bit at the given index.
	 *
	 * @param data the data array.
	 * @param index the index of the bit to flip.
	 * @throws IndexOutOfBoundsException if the index is
	 *          {@code index >= max || index < 0}.
	 * @throws NullPointerException if the {@code data} array is {@code null}.
	 */
	public static byte[] flip(final byte[] data, final int index) {
		return get(data, index) ? unset(data, index) : set(data, index);
	}

	/**
	 * Convert the given {@link LargeInteger} value to an byte array.
	 *
	 * @see #toLargeInteger(byte[])
	 *
	 * @param value the value to convert.
	 * @return the byte array representing the given {@link LargeInteger}.
	 * @throws NullPointerException if the given value is {@code null}.
	 */
	public static byte[] toByteArray(final LargeInteger value) {
		final int bytes = (value.bitLength() >>> 3) + 1;

		final byte[] array = new byte[bytes];
		value.toByteArray(array, 0);
		return reverse(array);
	}

	/**
	 * Convert the given byte array into an {@link LargeInteger}.
	 *
	 * @see #toByteArray(LargeInteger)
	 *
	 * @param array the byte array to convert.
	 * @return the {@link LargeInteger} built from the given byte array.
	 */
	public static LargeInteger toLargeInteger(final byte[] array) {
		reverse(array);
		final LargeInteger li = LargeInteger.valueOf(array, 0, array.length);
		reverse(array);
		return li;
	}


	private static byte[] reverse(final byte[] array) {
		int i = 0;
		int j = array.length;

		while (i < j) {
			swap(array, i++, --j);
		}

		return array;
	}

	private static void swap(final byte[] array, final int i, final int j) {
		final byte temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * Convert a binary representation of the given byte array to a string. The
	 * string has the following format:
	 * <pre>
	 *  Byte:       3        2        1        0
	 *              |        |        |        |
	 *  Array: "11110011|10011101|01000000|00101010"
	 *          |                 |        |      |
	 *  Bit:    23                15       7      0
	 * </pre>
	 * <i>Only the array string is printed.</i>
	 *
	 * @see #fromByteString(String)
	 *
	 * @param data the byte array to convert to a string.
	 * @return the binary representation of the given byte array.
	 */
	public static String toByteString(final byte... data) {
		final StringBuilder out = new StringBuilder();

		if (data.length > 0) {
			for (int j = 7; j >= 0; --j) {
				out.append((data[data.length - 1] >>> j) & 1);
			}
		}
		for (int i = data.length - 2; i >= 0 ;--i) {
			out.append('|');
			for (int j = 7; j >= 0; --j) {
				out.append((data[i] >>> j) & 1);
			}
		}

		return out.toString();
	}

	/**
	 * Convert a string which was created with the {@link #toByteString(byte...)}
	 * method back to an byte array.
	 *
	 * @see #toByteString(byte...)
	 *
	 * @param data the string to convert.
	 * @return the byte array.
	 * @throws IllegalArgumentException if the given data string could not be
	 *          converted.
	 */
	 public static byte[] fromByteString(final String data) {
		final String[] parts = data.split("\\|");
		final byte[] bytes = new byte[parts.length];

		for (int i = 0; i < parts.length; ++i) {
			if (parts[i].length() != 8) {
				throw new IllegalArgumentException(
					"Byte value doesn't contain 8 bit: " + parts[i]
				);
			}

			try {
				bytes[parts.length - 1 - i] = (byte)Integer.parseInt(parts[i], 2);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(e);
			}
		}

		return bytes;
	}

	static long toLong(final byte[] data) {
		return
			(((long)data[0] << 56) +
			((long)(data[1] & 255) << 48) +
			((long)(data[2] & 255) << 40) +
			((long)(data[3] & 255) << 32) +
			((long)(data[4] & 255) << 24) +
			((data[5] & 255) << 16) +
			((data[6] & 255) <<  8) +
			((data[7] & 255) <<  0));
	}

	static byte[] toBytes(final long value) {
		final byte[] bytes = new byte[8];
		bytes[0] = (byte)(value >>> 56);
		bytes[1] = (byte)(value >>> 48);
		bytes[2] = (byte)(value >>> 40);
		bytes[3] = (byte)(value >>> 32);
		bytes[4] = (byte)(value >>> 24);
		bytes[5] = (byte)(value >>> 16);
		bytes[6] = (byte)(value >>>  8);
		bytes[7] = (byte)(value >>>  0);
		return bytes;
	}

	static byte[] writeInt(final int v, final byte[] data, final int start) {
		if (data.length < 4 + start) {
			throw new IllegalArgumentException(
				"Byte array to short: " + data.length
			);
		}

		data[0 + start] = (byte)((v >>> 24) & 0xFF);
		data[1 + start] = (byte)((v >>> 16) & 0xFF);
		data[2 + start] = (byte)((v >>>  8) & 0xFF);
		data[3 + start] = (byte)((v >>>  0) & 0xFF);

		return data;
	}

	static int readInt(final byte[] data, final int start) {
		if (data.length < 4 + start) {
			throw new IllegalArgumentException(
				"Byte array to short: " + data.length
			);
		}

		return ((data[0 + start] << 24) +
				(data[1 + start] << 16) +
				(data[2 + start] << 8) +
				(data[3 + start] << 0));
	}

}




