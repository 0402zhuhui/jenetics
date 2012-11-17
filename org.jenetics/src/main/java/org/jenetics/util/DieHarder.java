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
package org.jenetics.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since 1.1
 * @version 1.1 &mdash; <em>$Date$</em>
 */
final class DieHarder {

	private static final class Randomizer implements Runnable {
		private final Random _random;
		private final DataOutputStream _out;

		public Randomizer(final Random random, final DataOutputStream out) {
			_random = random;
			_out = out;
		}

		@Override
		public void run() {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					for (int i = 0; i < 1000; ++i) {
						_out.writeLong(_random.nextLong());
					}
				}
			} catch (IOException ignore) {
			}
		}

	}

	public static void main(final String[] args) throws Exception {
		if ( args.length < 1) {
			System.out.println("Usage: java org.jenetics.util.DieHarder <random-class-name>");
			return;
		}

		final String randomName = args[0];
		Random random = null;
		try {
			random = (Random)Class.forName(randomName).newInstance();
			System.out.println("#=============================================================================#");
			System.out.println("#            Testing: " + randomName);
			System.out.println("#=============================================================================#");
		} catch (Exception e) {
			System.out.println("Can't create random class " + randomName);
			return;
		}

		final long start = System.currentTimeMillis();
		final ProcessBuilder builder = new ProcessBuilder("dieharder", "-a", "-g", "200");
		//final ProcessBuilder builder = new ProcessBuilder("dieharder", "-d", "1", "-g", "200");
		final Process dieharder = builder.start();

		final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
			dieharder.getOutputStream()
		));
		final Thread randomizer = new Thread(new Randomizer(random, out));
		randomizer.start();

		final BufferedReader stdout = new BufferedReader (
			new InputStreamReader(dieharder.getInputStream())
		);
		String line = null;
		while ((line = stdout.readLine()) != null) {
			System.out.println(line);
		}

		dieharder.waitFor();
		randomizer.interrupt();
		final long stop = System.currentTimeMillis();

		System.out.println("#=============================================================================#");
		System.out.println("#            Runtime: " + (stop - start)/1000 + "s");
		System.out.println("#=============================================================================#");

	}

	private static void printt(final String title) {
		System.out.println("#=============================================================================#");
		System.out.println("#            Runtime: " + (stop - start)/1000 + "s");
		System.out.println("#=============================================================================#");
	}

}





