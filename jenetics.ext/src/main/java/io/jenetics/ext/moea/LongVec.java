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
package io.jenetics.ext.moea;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class LongVec implements Vec<long[]>, Serializable {
	private static final long serialVersionUID = 1L;
	private final long[] _data;

	LongVec(final long[] data) {
		Vecs.checkVecLength(data.length);
		_data = data;
	}

	@Override
	public long[] data() {
		return _data;
	}

	@Override
	public int length() {
		return _data.length;
	}

	@Override
	public ElementComparator<long[]> comparator() {
		return (u, v, i) -> Long.compare(u[i], v[i]);
	}

	@Override
	public ElementDistance<long[]> distance() {
		return (u, v, i) -> u[i] - v[i];
	}

	@Override
	public Comparator<long[]> dominance() {
		return Vec::dominance;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(_data);
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this ||
			obj instanceof LongVec &&
				Arrays.equals(((LongVec) obj)._data, _data);
	}

	@Override
	public String toString() {
		return Arrays.toString(_data);
	}
}
