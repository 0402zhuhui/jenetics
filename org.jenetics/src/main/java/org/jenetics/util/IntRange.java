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
package org.jenetics.util;

import java.io.Serializable;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class IntRange implements Serializable {

	private static final long serialVersionUID = 1L;

	private final int _min;
	private final int _max;

	private IntRange(final int min, final int max) {
		_min = min;
		_max = max;
	}

	public int getMin() {
		return _min;
	}

	public int getMax() {
		return _max;
	}

	public static IntRange of(final int min, final int max) {
		return new IntRange(min, max);
	}

	@Override
	public int hashCode() {
		return _min + 31*_max;
	}

	@Override
	public boolean equals(final Object other) {
		return other instanceof IntRange &&
			_min == ((IntRange)other)._min &&
			_max == ((IntRange)other)._max;
	}

	@Override
	public String toString() {
		return "[" + _min + ", " + _max + "]";
	}

}
