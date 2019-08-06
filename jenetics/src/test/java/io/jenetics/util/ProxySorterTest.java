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

import java.util.Arrays;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class ProxySorterTest {

	@Test(dataProvider = "sorters")
	public void sort(final ProxySorter sorter, final int size) {
		final int[] array = new Random().ints(1000).toArray();

		final int[] indexes = sorter.sort(array);
		Assert.assertEquals(sorted(array, indexes), expected(array));
	}

	private static int[] sorted(final int[] array, final int[] indexes) {
		final int[] result = array.clone();
		for (int i = 0; i < array.length; ++i) {
			result[i] = array[indexes[i]];
		}
		return result;
	}

	private static int[] expected(final int[] array) {
		final int[] result = array.clone();
		Arrays.sort(result);
		return result;
	}

	@DataProvider(name = "sorters")
	public Object[][] sorters() {
		return new Object[][] {
			{TimProxySorter.INSTANCE, 0},
			{TimProxySorter.INSTANCE, 1},
			{TimProxySorter.INSTANCE, 2},
			{TimProxySorter.INSTANCE, 3},
			{TimProxySorter.INSTANCE, 5},
			{TimProxySorter.INSTANCE, 11},
			{TimProxySorter.INSTANCE, 32},
			{TimProxySorter.INSTANCE, 33},
			{TimProxySorter.INSTANCE, 1_000},
			{TimProxySorter.INSTANCE, 10_000},
			{TimProxySorter.INSTANCE, 100_000},
			{TimProxySorter.INSTANCE, 1_000_000},
			{TimProxySorter.INSTANCE, 10_000_000}
		};
	}

}
