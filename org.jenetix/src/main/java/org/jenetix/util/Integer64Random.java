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
package org.jenetix.util;

import static java.util.Objects.requireNonNull;
import static org.jenetics.util.math.random.nextLong;

import java.util.Random;

import org.jscience.mathematics.number.Integer64;

import org.jenetics.util.RandomRegistry;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since @__new_version__@
 * @version @__new_version__@ &mdash; <em>$Date: 2013-06-14 $</em>
 */
public class Integer64Random implements NumberRandom<Integer64> {

	private final Random _random;

	public Integer64Random(final Random random) {
		_random = requireNonNull(random, "Random");
	}

	public Integer64Random() {
		this(RandomRegistry.getRandom());
	}

	@Override
	public Integer64 next(final Integer64 min, final Integer64 max) {
		return next(_random, min, max);
	}

	public static Integer64 next(
		final Random random,
		final Integer64 min,
		final Integer64 max
	) {
		return Integer64.valueOf(
			nextLong(random, min.longValue(), max.longValue())
		);
	}

}
