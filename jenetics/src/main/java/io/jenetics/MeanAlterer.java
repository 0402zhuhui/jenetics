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
package io.jenetics;

import static java.lang.Math.min;
import static java.lang.String.format;

import java.util.Random;

import io.jenetics.util.BaseSeq;
import io.jenetics.util.MSeq;
import io.jenetics.util.Mean;
import io.jenetics.util.RandomRegistry;

/**
 * <p>
 * The order ({@link #getOrder()}) of this Recombination implementation is two.
 * </p>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 1.0
 * @version 5.2
 */
public class MeanAlterer<
	G extends Gene<?, G> & Mean<G>,
	C extends Comparable<? super C>
>
	extends Recombinator<G, C>
{

	/**
	 * Constructs an alterer with a given recombination probability.
	 *
	 * @param probability the crossover probability.
	 * @throws IllegalArgumentException if the {@code probability} is not in the
	 *         valid range of {@code [0, 1]}.
	 */
	public MeanAlterer(final double probability) {
		super(probability, 2);
	}

	/**
	 * Create a new alterer with alter probability of {@code 0.05}.
	 */
	public MeanAlterer() {
		this(0.05);
	}

	@Override
	protected int recombine(
		final MSeq<Phenotype<G, C>> population,
		final int[] individuals,
		final long generation
	) {
		final Random random = RandomRegistry.getRandom();

		final Phenotype<G, C> pt1 = population.get(individuals[0]);
		final Phenotype<G, C> pt2 = population.get(individuals[1]);
		final Genotype<G> gt1 = pt1.genotype();
		final Genotype<G> gt2 = pt2.genotype();

		//Choosing the Chromosome index for crossover.
		final int cindex = random.nextInt(min(gt1.length(), gt2.length()));

		final MSeq<Chromosome<G>> c1 = MSeq.of(gt1);

		// Calculate the mean value of the gene array.
		final MSeq<G> mean = mean(c1.get(cindex), gt2.get(cindex));

		c1.set(cindex, c1.get(cindex).newInstance(mean.toISeq()));
		population.set(individuals[0], Phenotype.of(Genotype.of(c1), generation));

		return 1;
	}

	private static <G extends Gene<?, G> & Mean<G>>
	MSeq<G> mean(final BaseSeq<G> a, final BaseSeq<G> b) {
		final MSeq<G> result = MSeq.ofLength(a.length());
		for (int i = a.length(); --i >= 0;) {
			result.set(i, a.get(i).mean(b.get(i)));
		}
		return result;
	}

	@Override
	public String toString() {
		return format("%s[p=%f]", getClass().getSimpleName(), _probability);
	}

}
