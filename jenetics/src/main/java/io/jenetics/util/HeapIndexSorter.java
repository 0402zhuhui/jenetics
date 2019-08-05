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
package io.jenetics.util;

import static io.jenetics.internal.util.array.swap;

/**
 * Implementing the index sorter with the heap sort algorithm.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
enum HeapIndexSorter implements ProxySorter {

	INSTANCE;

	/**
	 * Implementation of the heap index sort algorithm.
	 *
	 * @param array the array which is sorted
	 * @param length the array length
	 * @param comp the array element comparator
	 * @param <T> the array type
	 * @return the sorted index array
	 */
	@Override
	public <T> int[] sort(
		final T array,
		final int length,
		final ProxyComparator<? super T> comp
	) {
		final int[] indexes = IndexSorters.indexes(length);

		// Heapify
		for (int k = length/2; k >= 0; --k) {
			sink(array, indexes, comp, k, length);
		}

		// Sort down.
		for (int i = length; --i >= 1;) {
			swap(indexes, 0, i);
			sink(array, indexes, comp, 0, i);
		}

		return indexes;
	}

	private static <T> void sink(
		final T array,
		final int[] idx,
		final ProxyComparator<T> cmp,
		final int start,
		final int end
	) {
		int m = start;
		while (2*m < end) {
			int j = 2*m;
			if (j < end - 1 && cmp.compare(array, idx[j], idx[j + 1]) < 0) ++j;
			if (cmp.compare(array, idx[m], idx[j]) >= 0) break;
			swap(idx, m, j);
			m = j;
		}
	}

}
