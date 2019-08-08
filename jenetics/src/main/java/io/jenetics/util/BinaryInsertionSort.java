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

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class BinaryInsertionSort {
	private BinaryInsertionSort() {
	}

	// Sorting method used for testing.
	static <T> int[] sort(
		final T array,
		final int length,
		final ProxySorter.Comparator<? super T> cmp
	) {
		final int[] proxy = ProxySorter.indexes(length);
		if (length < 2) {
			return proxy;
		}

		final int begin = countRunAndMakeAscending(array, 0, length, proxy, cmp);
		binarySort(array, 0, length, begin, proxy, cmp);
		return proxy;
	}

	// Sorting method used by Tim sorter.
	static <T> void sort(
		final T array,
		final int low,
		final int high,
		final int[] proxy,
		final ProxySorter.Comparator<? super T> cmp
	) {
		final int begin = countRunAndMakeAscending(array, low, high, proxy, cmp);
		binarySort(array, low, high, 0, proxy, cmp);
	}

	@SuppressWarnings("fallthrough")
	private static <T> void binarySort(
		final T array,
		final int low,
		final int high,
		final int begin,
		final int[] proxy,
		final ProxySorter.Comparator<? super T> cmp
	) {
		assert low <= begin && begin <= high;

		int start = begin;
		if (start == low) {
			start++;
		}

		for ( ; start < high; start++) {
			int pivot = proxy[start];

			int left = low;
			int right = start;
			assert left <= right;

			while (left < right) {
				final int mid = (left + right) >>> 1;
				if (cmp.compare(array, pivot, proxy[mid]) < 0) {
					right = mid;
				} else {
					left = mid + 1;
				}
			}
			assert left == right;

			int n = start - left;  // The number of elements to move
			switch(n) {
				case 2:
					proxy[left + 2] = proxy[left + 1];
				case 1:
					proxy[left + 1] = proxy[left];
					break;
				default:
					System.arraycopy(proxy, left, proxy, left + 1, n);
			}
			proxy[left] = pivot;
		}
	}

	static <T> int countRunAndMakeAscending(
		final T array,
		final int lo,
		final int hi,
		final int[] proxy,
		final ProxySorter.Comparator<? super T> c
	) {
		assert lo < hi;

		int runHi = lo + 1;
		if (runHi == hi) {
			return 1;
		}

		if (c.compare(array, proxy[runHi++], proxy[lo]) < 0) {
			while (runHi < hi && c.compare(array, proxy[runHi], proxy[runHi - 1]) < 0) {
				runHi++;
			}

			reverseRange(proxy, lo, runHi);
		} else {
			while (runHi < hi && c.compare(array, proxy[runHi], proxy[runHi - 1]) >= 0) {
				runHi++;
			}
		}

		return runHi - lo;
	}

	private static void reverseRange(final int[] proxy, int lo, int hi) {
		hi--;
		while (lo < hi) {
			final int t = proxy[lo];
			proxy[lo++] = proxy[hi];
			proxy[hi--] = t;
		}
	}

}
