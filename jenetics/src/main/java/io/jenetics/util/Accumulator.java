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

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;
import java.util.stream.Collector;

/**
 * This interface is in some kind an addition to the {@link Collector} interface
 * of the Java Stream API. It can be used in places where it is needed to collect
 * intermediate results from an existing stream.
 *
 * <pre>{@code
 * final Accumulator<Integer, ISeq<Integer>> accu = Accumulator.of(ISeq.toISeq());
 *
 * final ISeq<ISeq<Integer>> result = IntStream.range(0, 10).boxed()
 *     .peek(accu)
 *     .map(i -> accu.result())
 *     .collect(ISeq.toISeq());
 *
 * result.forEach(System.out::println);
 * }</pre>
 * The code above gives you the following output.
 * <pre>
 * [0]
 * [0,1]
 * [0,1,2]
 * [0,1,2,3]
 * [0,1,2,3,4]
 * [0,1,2,3,4,5]
 * [0,1,2,3,4,5,6]
 * [0,1,2,3,4,5,6,7]
 * [0,1,2,3,4,5,6,7,8]
 * [0,1,2,3,4,5,6,7,8,9]
 * </pre>
 *
 * @param <T> the type of input elements to the accumulate operation
 * @param <R> the result type of the accumulated operation
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public interface Accumulator<T, R> extends Consumer<T> {

	/**
	 * Return the current result of the accumulated elements. The accumulated
	 * elements are not changed by this method.
	 *
	 * @return the current result of the accumulated elements
	 */
	R result();

	/**
	 * Create a new accumulator from the given {@code collector}.
	 *
	 * <pre>{@code
	 * final Accumulator<Integer, ISeq<Integer>> accu = Accumulator.of(ISeq.toISeq());
	 * }</pre>
	 *
	 * @param collector the collector which is used for accumulation and creation
	 *        the result value.
	 * @param <T> the type of input elements to the reduction operation
	 * @param <A> the mutable accumulation type of the reduction operation
	 *            (often hidden as an implementation detail)
	 * @param <R> the result type of the reduction operation
	 * @return a new accumulator which is backed by the given {@code collector}
	 * @throws NullPointerException if the given {@code collector} is {@code null}
	 */
	static <T, A, R> Accumulator<T, R> of(final Collector<T, A, R> collector) {
		requireNonNull(collector);

		return new Accumulator<>() {
			private A _collection;

			@Override
			public void accept(final T value) {
				if (_collection == null) {
					_collection = collector.supplier().get();
				}
				collector.accumulator().accept(_collection, value);
			}

			@Override
			public R result() {
				return collector.finisher().apply(_collection);
			}
		};
	}

}
