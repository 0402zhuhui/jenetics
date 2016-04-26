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
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.tool.problem;

import java.awt.Point;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.jenetics.EnumGene;
import org.jenetics.Optimize;
import org.jenetics.engine.Codec;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.codecs;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class TSM {
	// The locations to visit.
	static final Point[] POINTS = new Point[] {
		new Point(0, 0), new Point(1, 2), new Point(4, 5) // ...
	};

	// The permutation codec.
	static final Codec<Point[], EnumGene<Point>> CODEC =
		codecs.ofPermutation(POINTS);

	// The fitness function (in the problem domain).
	static double dist(final Point[] p) {
		return IntStream.range(0, p.length)
			.mapToDouble(i -> p[i].distance(p[(i + i)%p.length]))
			.sum();
	}

	// The evolution engine.
	static final Engine<EnumGene<Point>, Double> ENGINE = Engine
		.builder(TSM::dist, CODEC)
		.optimize(Optimize.MINIMUM)
		.build();

	// Find the solution.
	public static void main(final String[] args) {
		final Point[] result = CODEC.decoder().apply(
			ENGINE.stream()
				.limit(10)
				.collect(EvolutionResult.toBestGenotype())
		);

		System.out.println(Arrays.toString(result));
	}
}
