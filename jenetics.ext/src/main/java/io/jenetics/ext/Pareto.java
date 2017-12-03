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
package io.jenetics.ext;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.stream.Collector;

import io.jenetics.Gene;
import io.jenetics.Phenotype;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.util.ISeq;
import io.jenetics.util.MSeq;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class Pareto {

	private Pareto() {
	}

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	Collector<EvolutionResult<G, C>, ?, ISeq<Phenotype<G, C>>>
	toParetoSet(final int maxElements) {
		return null;
		/*
		return Collector.of(
			MinMax::<EvolutionResult<G, C>>of,
			MinMax::accept,
			MinMax::combine,
			mm -> mm.getMax() != null
				? mm.getMax().withTotalGenerations(mm.getCount())
				: null
		);*/
	}


	/*
	 * Reference: E. Zitzler and L. Thiele
	 * Multiobjective Evolutionary Algorithms: A Comparative Case Study and the Strength Pareto Approach,
	 * IEEE Transactions on Evolutionary Computation, vol. 3, no. 4,
	 * pp. 257-271, 1999.
	 */
	public static <C extends Comparable<? super C>> ISeq<C>
	frontOf(final Iterable<? extends C> elements) {
		final MSeq<C> front = MSeq.of(elements);

		int n = front.size();
		int i = 0;
		while (i < n) {
			int j = i + 1;
			while (j < n) {
				if (front.get(i).compareTo(front.get(j)) > 0) {
					--n;
					front.swap(j, n);
				} else if (front.get(j).compareTo(front.get(i)) > 0) {
					--n;
					front.swap(i, n);
					--i;
					break;
				} else {
					++j;
				}
			}
			++i;
		}

		return front.subSeq(0, n).copy().toISeq();
	}

	public static <C extends Comparable<? super C>> int[]
	ranks(final ISeq<C> elements) {
		return null;
	}

	static  <G extends Gene<?, G>, T> ISeq<Phenotype<G, MOF<T>>>
	merge(final ISeq<Phenotype<G, MOF<T>>> a, final ISeq<Phenotype<G, MOF<T>>> b) {
		return ISeq.empty();
	}

	/**
	 * Calculates the <a href="https://en.wikipedia.org/wiki/Pareto_efficiency">
	 *     <b>Pareto Dominance</b></a> of the two vectors <b>u</b> and <b>v</b>.
	 *
	 * @param u the first vector
	 * @param v the second vector
	 * @param comparator the element comparator which is used for calculating
	 *        the dominance
	 * @param <T> the element type of vector <b>u</b> and <b>v</b>
	 * @return {@code 1} if <b>u</b> ≻ <b>v</b>, {@code -1} if <b>v</b> ≻
	 *         <b>u</b> and {@code 0} otherwise
	 * @throws NullPointerException if one of the arguments is {@code null}
	 * @throws IllegalArgumentException if {@code u.length != v.length}
	 */
	public static <T> int
	dominates(final T[] u, final T[] v, final Comparator<? super T> comparator) {
		requireNonNull(comparator);
		if (u.length != v.length) {
			throw new IllegalArgumentException();
		}

		boolean adom = false;
		boolean bdom = false;

		for (int i = 0; i < u.length; ++i) {
			final int cmp = comparator.compare(u[i], v[i]);

			if (cmp > 0) {
				adom = true;
				if (bdom) {
					return 0;
				}
			} else if (cmp < 0) {
				bdom = true;
				if (adom) {
					return 0;
				}
			}
		}

		if (adom == bdom) {
			return 0;
		} else if (adom) {
			return 1;
		} else {
			return -1;
		}
	}

}
