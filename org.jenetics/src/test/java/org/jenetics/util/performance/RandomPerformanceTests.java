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
package org.jenetics.util.performance;

import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.jenetics.util.LCG64ShiftRandom;
//import org.jenetics.util.XORShiftRandom;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date: 2013-07-29 $</em>
 */
public class RandomPerformanceTests {


	public static void main(final String[] args) {
		final int loops = Integer.MAX_VALUE;//1_000_000_000;

		test(new Random(), loops);
		test(ThreadLocalRandom.current(), loops);
		//test(new XORShiftRandom(), loops);
		test(new LCG64ShiftRandom(), loops);
	}

	private static void test(final Random random, final int loops) {
		System.out.println(String.format(
			"%-20s %s; %s; %s; %s",
			random.getClass().getSimpleName() + ":",
			testNextInt(random, loops),
			testNextLong(random, loops),
			testNextFloat(random, loops),
			testNextDouble(random, loops)
		));
	}

	private static String testNextInt(final Random random, final int loops) {
		long start = System.nanoTime();
		for (int i = loops; --i >= 0;) {
			random.nextInt();
		}
		long end = System.nanoTime();

		final NumberFormat format = NumberFormat.getIntegerInstance();
		return String.format("%11s i/sec", format.format(perSec(loops, start, end)));
	}

	private static String testNextLong(final Random random, final int loops) {
		long start = System.nanoTime();
		for (int i = loops; --i >= 0;) {
			random.nextLong();
		}
		long end = System.nanoTime();

		final NumberFormat format = NumberFormat.getIntegerInstance();
		return String.format("%11s l/sec", format.format(perSec(loops, start, end)));
	}

	private static String testNextFloat(final Random random, final int loops) {
		long start = System.nanoTime();
		for (int i = loops; --i >= 0;) {
			random.nextFloat();
		}
		long end = System.nanoTime();

		final NumberFormat format = NumberFormat.getIntegerInstance();
		return String.format("%11s f/sec", format.format(perSec(loops, start, end)));
	}

	private static String testNextDouble(final Random random, final int loops) {
		long start = System.nanoTime();
		for (int i = loops; --i >= 0;) {
			random.nextDouble();
		}
		long end = System.nanoTime();

		final NumberFormat format = NumberFormat.getIntegerInstance();
		return String.format("%11s d/sec", format.format(perSec(loops, start, end)));
	}

	private static long perSec(final long loops, final long start, final long end) {
		final double seconds = (end - start)/1_000_000_000.0;
		return (long)(loops/seconds);
	}

}
