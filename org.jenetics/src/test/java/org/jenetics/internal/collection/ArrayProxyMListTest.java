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
package org.jenetics.internal.collection;

import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.jenetics.internal.math.random;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
public class ArrayProxyMListTest {

	@Test
	public void set() {
		long seed = random.seed();
		final Random random = new Random(seed);

		final Array<Integer> impl = Array.of(ObjectStore.ofLength(1000));
		for (int i = 0; i < impl.length(); ++i) {
			impl.set(i, random.nextInt());
		}

		final List<Integer> list = new ArrayProxyMList<>(impl);

		seed = org.jenetics.internal.math.random.seed();
		random.setSeed(seed);
		for (int i = 0; i < impl.length(); ++i) {
			list.set(i, random.nextInt());
		}

		random.setSeed(seed);
		for (int i = 0; i <impl.length(); ++i) {
			Assert.assertEquals(impl.get(i), list.get(i));
			Assert.assertEquals(list.get(i).intValue(), random.nextInt());
		}
	}

}
