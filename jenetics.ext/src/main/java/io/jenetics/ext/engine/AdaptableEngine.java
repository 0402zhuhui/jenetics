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
package io.jenetics.ext.engine;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;
import java.util.function.Supplier;

import io.jenetics.Gene;
import io.jenetics.engine.Evolution;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.engine.EvolutionStream;
import io.jenetics.engine.EvolutionStreamable;

/**
 * The {@code AdaptableEngine} allows you to create evolution streams, which
 * can evaluate each generation with a different {@link Evolution} engine
 * and/or configuration.
 *
 * <pre> {@code
 *                                           +----------+
 *                                           |   ES[i]  |
 *           +-------------------------------+------+   |
 *           |                                      +---+
 *   (Start) |  EvolutionResult[i-1] -> Engine[i]   |-----------+-->
 *  -----+-->|           ^                          |  Result   |
 *       ^   +-----------|--------------------------+           |
 *       |               |                                      |
 *       +---------------+--------------<-----------------------+
 * }</pre>
 *
 * @see ConcatEngine
 * @see CyclicEngine
 *
 * @param <G> the gene type
 * @param <C> the fitness type
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class AdaptableEngine<
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
	implements
		Evolution<G, C>,
		EvolutionStreamable<G, C>
{

	private final Function<
		? super EvolutionStart<G, C>,
		? extends Evolution<G, C>> _evolution;

	/**
	 * Create a new adaptable evolution engine with the given {@code evolution}
	 * function. It allows you to evaluate the a generation, depending on the
	 * actual evolution start.
	 *
	 * @param evolution the <em>adaptable</em> {@code evolution} function
	 */
	public AdaptableEngine(
		final Function<
			? super EvolutionStart<G, C>,
			? extends Evolution<G, C>> evolution
	) {
		_evolution = requireNonNull(evolution);
	}

	@Override
	public EvolutionResult<G, C> evolve(final EvolutionStart<G, C> start) {
		return _evolution.apply(start).evolve(start);
	}

	@Override
	public EvolutionStream<G, C>
	stream(final Supplier<EvolutionStart<G, C>> start) {
		return EvolutionStream.ofEvolution(start, this);
	}

}
