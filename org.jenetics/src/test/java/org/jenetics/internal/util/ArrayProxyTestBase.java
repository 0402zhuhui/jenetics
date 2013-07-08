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

import java.util.Random;

import org.jenetics.util.math;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date: 2013-07-08 $</em>
 */
public abstract class ArrayProxyTestBase<T> {

	public abstract ArrayProxy<T> newArrayProxy(final int length);

	public abstract T newArrayProxyElement(final Random random);

	public T newArrayProxyElement() {
		return newArrayProxyElement(new Random(math.random.seed()));
	}

	@Test
	public void uncheckedOffsetGetSet() {
		final long seed = math.random.seed();
		final Random random = new Random(seed);

		final ArrayProxy<T> proxy = newArrayProxy(10_000);
		for (int i = 0; i < proxy._length; ++i) {
			proxy.uncheckedOffsetSet(i, newArrayProxyElement(random));
		}

		random.setSeed(seed);
		for (int i = 0; i < proxy._length; ++i) {
			final T actual = proxy.uncheckedOffsetGet(i);
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(actual, expected);
		}
	}

	@Test
	public void uncheckedGetSet() {
		final long seed = math.random.seed();
		final Random random = new Random(seed);

		final ArrayProxy<T> proxy = newArrayProxy(10_000);
		for (int i = 0; i < proxy._length; ++i) {
			proxy.uncheckedSet(i, newArrayProxyElement(random));
		}

		random.setSeed(seed);
		for (int i = 0; i < proxy._length; ++i) {
			final T actual = proxy.uncheckedGet(i);
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(actual, expected);
		}
	}

	@Test
	public void getSet() {
		final long seed = math.random.seed();
		final Random random = new Random(seed);

		final ArrayProxy<T> proxy = newArrayProxy(10_000);
		for (int i = 0; i < proxy._length; ++i) {
			proxy.set(i, newArrayProxyElement(random));
		}

		random.setSeed(seed);
		for (int i = 0; i < proxy._length; ++i) {
			final T actual = proxy.get(i);
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(actual, expected);
		}
	}

	@Test(
		dataProvider = "outofboundsdata",
		expectedExceptions = IndexOutOfBoundsException.class
	)
	public void outOfBoundsGet(final Integer length, final Integer index) {
		final ArrayProxy<T> proxy = newArrayProxy(length);
		proxy.get(index);
	}

	@Test(
		dataProvider = "outofboundsdata",
		expectedExceptions = IndexOutOfBoundsException.class
	)
	public void outOfBoundsSet(final Integer length, final Integer index) {
		final ArrayProxy<T> proxy = newArrayProxy(length);
		proxy.get(index);
	}

	@DataProvider(name = "outofboundsdata")
	public Object[][] getOutOfBoundsData() {
		return new Object[][] {
			{3, -1},
			{3, 3},
			{10, -234},
			{10, 233},
			{100, -292929},
			{100, 200}
		};
	}

	@Test(dataProvider = "subintintdata")
	public void subIntInt(
		final Integer length,
		final Integer start,
		final Integer end
	) {
		final long seed = math.random.seed();
		final Random random = new Random(seed);

		final ArrayProxy<T> proxy = newArrayProxy(length);
		for (int i = 0; i < proxy._length; ++i) {
			proxy.set(i, newArrayProxyElement(random));
		}

		random.setSeed(seed);
		for (int i = 0; i < start; ++i) {
			final T actual = proxy.get(i);
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(actual, expected);
		}

		final ArrayProxy<T> sub = proxy.sub(start, end);
		for (int i = 0; i < (end - start); ++i) {
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(sub.get(i), expected);
			Assert.assertEquals(proxy.get(i + start), expected);

			Assert.assertEquals(sub.uncheckedGet(i), expected);
			Assert.assertEquals(proxy.uncheckedGet(i + start), expected);

			Assert.assertEquals(
				sub.uncheckedOffsetGet(i + start + proxy._start),
				expected
			);
			Assert.assertEquals(
				proxy.uncheckedOffsetGet(i + start + proxy._start),
				expected
			);
		}
	}

	@Test(dataProvider = "subintintdata")
	public void subSubIntInt(
		final Integer length,
		final Integer start,
		final Integer end
	) {
		final long seed = math.random.seed();
		final Random random = new Random(seed);

		final ArrayProxy<T> proxy = newArrayProxy(length + 10).sub(5, length + 5);
		for (int i = 0; i < proxy._length; ++i) {
			proxy.set(i, newArrayProxyElement(random));
		}

		random.setSeed(seed);
		for (int i = 0; i < start; ++i) {
			final T actual = proxy.get(i);
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(actual, expected);
		}

		final ArrayProxy<T> sub = proxy.sub(start, end);
		for (int i = 0; i < (end - start); ++i) {
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(sub.get(i), expected);
			Assert.assertEquals(proxy.get(i + start), expected);

			Assert.assertEquals(sub.uncheckedGet(i), expected);
			Assert.assertEquals(proxy.uncheckedGet(i + start), expected);

			Assert.assertEquals(
				sub.uncheckedOffsetGet(i + start + proxy._start),
				expected
			);
			Assert.assertEquals(
				proxy.uncheckedOffsetGet(i + start + proxy._start),
				expected
			);
		}
	}

	@Test(dataProvider = "subintintdata")
	public void subSubSubIntInt(
		final Integer length,
		final Integer start,
		final Integer end
	) {
		final long seed = math.random.seed();
		final Random random = new Random(seed);

		final ArrayProxy<T> proxy = newArrayProxy(length + 20)
										.sub(5, length + 15)
										.sub(5, length + 10);
		for (int i = 0; i < proxy._length; ++i) {
			proxy.set(i, newArrayProxyElement(random));
		}

		random.setSeed(seed);
		for (int i = 0; i < start; ++i) {
			final T actual = proxy.get(i);
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(actual, expected);
		}

		final ArrayProxy<T> sub = proxy.sub(start, end);
		for (int i = 0; i < (end - start); ++i) {
			final T expected = newArrayProxyElement(random);

			Assert.assertEquals(sub.get(i), expected);
			Assert.assertEquals(proxy.get(i + start), expected);

			Assert.assertEquals(sub.uncheckedGet(i), expected);
			Assert.assertEquals(proxy.uncheckedGet(i + start), expected);

			Assert.assertEquals(
				sub.uncheckedOffsetGet(i + start + proxy._start),
				expected
			);
			Assert.assertEquals(
				proxy.uncheckedOffsetGet(i + start + proxy._start),
				expected
			);
		}
	}

	@DataProvider(name = "subintintdata")
	public Object[][] getSubIntIntData() {
		return new Object[][] {
			{1000, 0, 1000},
			{1000, 2, 1000},
			{1000, 100, 900},
			{1000, 5, 600},
			{10000, 8800, 9400},
			{10000, 4, 9800}
		};
	}


	@Test(dataProvider = "swapparameter")
	public void swapIntIntArrayProxyInt(
		final Integer length, final Integer start, final Integer end,
		final Integer otherLength, final Integer otherStart
	) {
		final long thatSeed = math.random.seed();
		final long otherSeed = math.random.seed();
		final Random thatRandom = new Random(thatSeed);
		final Random otherRandom = new Random(otherSeed);

		final ArrayProxy<T> that = newArrayProxy(length);
		final ArrayProxy<T> other = newArrayProxy(otherLength);
		final ArrayProxy<T> thatCopy = newArrayProxy(length);
		final ArrayProxy<T> otherCopy = newArrayProxy(otherLength);

		for (int i = 0; i < length; ++i) {
			that.set(i, newArrayProxyElement(thatRandom));
		}
		for (int i = 0; i < otherLength; ++i) {
			other.set(i, newArrayProxyElement(otherRandom));
		}

		thatRandom.setSeed(thatSeed);
		otherRandom.setSeed(otherSeed);
		for (int i = 0; i < length; ++i) {
			thatCopy.set(i, newArrayProxyElement(thatRandom));
		}
		for (int i = 0; i < otherLength; ++i) {
			otherCopy.set(i, newArrayProxyElement(otherRandom));
		}


		that.swap(start, end, other, otherStart);

		for (int j = start; j < end; ++j) {
			Assert.assertEquals(that.get(j), otherCopy.get(j + otherStart - start));
		}
		for (int j = 0; j < (end - start); ++j) {
			Assert.assertEquals(other.get(j + otherStart), thatCopy.get(j + start));
		}

	}

	@DataProvider(name = "swapparameter")
	public Object[][] getSwapParameter() {
		return new Object[][] {
			{10, 1, 5, 15, 4},
			{10, 5, 7, 3, 1}
		};
	}

	@Test(
		dataProvider = "swapparameteroutofbounds",
		expectedExceptions = IndexOutOfBoundsException.class
	)
	public void swapIntIntArrayProxyIntIndexOutOfBounds(
		final Integer length, final Integer start, final Integer end,
		final Integer otherLength, final Integer otherStart
	) {
		final long thatSeed = math.random.seed();
		final long otherSeed = math.random.seed();
		final Random thatRandom = new Random(thatSeed);
		final Random otherRandom = new Random(otherSeed);

		final ArrayProxy<T> that = newArrayProxy(length);
		final ArrayProxy<T> other = newArrayProxy(otherLength);
		final ArrayProxy<T> thatCopy = newArrayProxy(length);
		final ArrayProxy<T> otherCopy = newArrayProxy(otherLength);

		for (int i = 0; i < length; ++i) {
			that.set(i, newArrayProxyElement(thatRandom));
		}
		for (int i = 0; i < otherLength; ++i) {
			other.set(i, newArrayProxyElement(otherRandom));
		}

		thatRandom.setSeed(thatSeed);
		otherRandom.setSeed(otherSeed);
		for (int i = 0; i < length; ++i) {
			thatCopy.set(i, newArrayProxyElement(thatRandom));
		}
		for (int i = 0; i < otherLength; ++i) {
			otherCopy.set(i, newArrayProxyElement(otherRandom));
		}


		try {
			that.swap(start, end, other, otherStart);
		} catch (IndexOutOfBoundsException e) {
			// The arrays should not be changed.
			for (int i = 0; i < length; ++i) {
				Assert.assertEquals(that.get(i), thatCopy.get(i));
			}
			for (int i = 0; i < length; ++i) {
				Assert.assertEquals(other.get(i), otherCopy.get(i));
			}

			throw e;
		}

	}

	@DataProvider(name = "swapparameteroutofbounds")
	public Object[][] getSwapParameterOutOfBounds() {
		return new Object[][] {
			{10, -1, 5, 15, 4},
			{10, 2, 7, 3, 1}
		};
	}

}






























