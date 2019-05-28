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

import java.util.Random;

import io.jenetics.Chromosome;
import io.jenetics.Gene;
import io.jenetics.Mutator;
import io.jenetics.MutatorResult;
import io.jenetics.internal.math.probability;
import io.jenetics.util.MSeq;

/**
 * The Partial Transfer Shuffle (PSM), change part of the order of the genes in
 * the chromosome.
 *
 * @see <a href="https://arxiv.org/ftp/arxiv/papers/1203/1203.3099.pdf">
 *     Analyzing the Performance of Mutation Operators to Solve the Travelling
 *     Salesman Problem</a>
 * @see <a href="https://www.researchgate.net/publication/282732991_A_New_Mutation_Operator_for_Solving_an_NP-Complete_Problem_Travelling_Salesman_Problem">
 *     A New Mutation Operator for Solving an NP-Complete Problem: Travelling Salesman Problem</a>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class PartialShuffleMutator<
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
	extends Mutator<G, C>
{

	/**
	 * Create a new <em>PSM</em> with the given mutation probability.
	 *
	 * @param probability the mutation probability
	 */
	public PartialShuffleMutator(final double probability) {
		super(probability);
	}

	/**
	 * Create a new <em>PSM</em> with the {@link #DEFAULT_ALTER_PROBABILITY}.
	 */
	public PartialShuffleMutator() {
	}

	/**
	 * Partially changes the gene order of the given chromosome.
	 *
	 * @param chromosome the chromosome to mutate
	 * @param p the mutation probability for the underlying genetic objects
	 * @param random the random engine used for the genotype mutation
	 * @return the mutation result
	 */
	@Override
	protected MutatorResult<Chromosome<G>> mutate(
		final Chromosome<G> chromosome,
		final double p,
		final Random random
	) {
		int mutations = 0;
		MSeq<G> genes = null;

		for (int i = 0; i < chromosome.length(); ++i) {
			final int P = probability.toInt(p);
			if (random.nextInt() < P) {
				if (genes == null) genes = chromosome.toSeq().copy();

				final int j = random.nextInt(chromosome.length());
				genes.swap(i, j);
				++mutations;
			}
		}

		return genes != null
			? MutatorResult.of(chromosome.newInstance(genes.toISeq()), mutations)
			: MutatorResult.of(chromosome);
	}

}
