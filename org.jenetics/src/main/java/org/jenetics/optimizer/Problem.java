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
package org.jenetics.optimizer;

import java.util.function.Function;

import org.jenetics.Gene;
import org.jenetics.engine.Codec;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public interface Problem<
	T,
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
{

	public Function<T, C> fitness();

	public Codec<T, G> codec();

	public static <T, G extends Gene<?, G>, C extends Comparable<? super C>>
	Problem<T, G, C> of(final Function<T, C> fitness, final Codec<T, G>
		codec)  {
		return new Problem<T, G, C>() {
			@Override
			public Codec<T, G> codec() {
				return codec;
			}

			@Override
			public Function<T, C> fitness() {
				return fitness;
			}
		};
	}

}

