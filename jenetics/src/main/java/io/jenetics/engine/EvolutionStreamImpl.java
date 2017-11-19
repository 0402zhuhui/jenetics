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
package io.jenetics.engine;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import io.jenetics.Gene;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version !__version__!
 */
final class EvolutionStreamImpl<
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
	extends StreamProxy<EvolutionResult<G, C>>
	implements EvolutionStream<G, C>
{

	private final LimitSpliterator<EvolutionResult<G, C>> _spliterator;

	private EvolutionStreamImpl(
		final LimitSpliterator<EvolutionResult<G, C>> spliterator
	) {
		super(StreamSupport.stream(spliterator, false));
		_spliterator = spliterator;
	}

	EvolutionStreamImpl(
		final Supplier<EvolutionStart<G, C>> start,
		final Function<? super EvolutionStart<G, C>, EvolutionResult<G, C>> evolution
	) {
		this(new EvolutionSpliterator<>(start, evolution));
	}

	@Override
	public EvolutionStream<G, C>
	limit(final Predicate<? super EvolutionResult<G, C>> proceed) {
		return new EvolutionStreamImpl<>(_spliterator.limit(proceed));
	}

}
