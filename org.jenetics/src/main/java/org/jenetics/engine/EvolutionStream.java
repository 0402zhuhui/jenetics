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
package org.jenetics.engine;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jenetics.Gene;

/**
 * The {@code EvolutionStream} class extends the Java {@link Stream} and adds a
 * method for limiting the evolution by a given predicate.
 *
 * @see java.util.stream.Stream
 * @see Engine
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.1
 */
public interface EvolutionStream<
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
	extends Stream<EvolutionResult<G, C>>
{

	/**
	 * Returns a stream consisting of the elements of this stream, truncated
	 * when the given {@code proceed} predicate returns {@code false}.
	 * <p>
	 * <i>General usage example:</i>
	 * [code]
	 * final Phenotype&lt;DoubleGene, Double&gt; result = engine.stream()
	 *      // Truncate the evolution stream after 5 "steady" generations.
	 *     .limit(bySteadyFitness(5))
	 *      // The evolution will stop after maximal 100 generations.
	 *     .limit(100)
	 *     .collect(toBestPhenotype());
	 * [/code]
	 *
	 * @see limit
	 *
	 * @param proceed the predicate which determines whether the stream is
	 *        truncated or not. <i>If the predicate returns {@code false}, the
	 *        evolution stream is truncated.</i>
	 * @return the new stream
	 * @throws NullPointerException if the given predicate is {@code null}.
	 */
	public EvolutionStream<G, C>
	limit(final Predicate<? super EvolutionResult<G, C>> proceed);

	/**
	 * Create a new {@code EvolutionStream} from the given {@code start}
	 * population and {@code evolution} function. The main purpose of this
	 * factory method is to simplify the creation of an {@code EvolutionStream}
	 * from an own evolution (GA) engine.
	 *
	 * [code]
	 * final Function&lt;
	 *     EvolutionStart&lt;DoubleGene, Double&gt;,
	 *     EvolutionResult&lt;DoubleGene, Double&gt;&gt; engine = new MySpecialEngine();
	 * final Supplier&lt;EvolutionStart&lt;DoubleGene, Double&gt;&gt; start = ...
	 *
	 * final EvolutionStream&lt;DoubleGene, Double&gt; stream =
	 *     EvolutionStream.of(start, engine);
	 * [/code]
	 *
	 * @since 3.1
	 *
	 * @param <G> the gene type
	 * @param <C> the fitness type
	 * @param start the evolution start
	 * @param evolution the evolution function
	 * @return a new {@code EvolutionStream} with the given {@code start} and
	 *         {@code evolution} function
	 * @throws java.lang.NullPointerException if one of the arguments is
	 *         {@code null}
	 */
	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	EvolutionStream<G, C> of(
		final Supplier<EvolutionStart<G, C>> start,
		final Function<? super EvolutionStart<G, C>, EvolutionResult<G, C>> evolution
	) {
		return new EvolutionStreamImpl<>(start, evolution);
	}

}
