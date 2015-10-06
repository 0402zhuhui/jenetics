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

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.ExponentialRankSelector;
import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.LinearRankSelector;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.Selector;
import org.jenetics.TournamentSelector;
import org.jenetics.TruncationSelector;
import org.jenetics.engine.Codec;
import org.jenetics.engine.codecs;
import org.jenetics.util.DoubleRange;
import org.jenetics.util.ISeq;
import org.jenetics.util.IntRange;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class SelectorCodecs {

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	Codec<Selector<G, C>, DoubleGene> Generic(final IntRange tournamentSize) {
		return Codec.of(
			codecs.ofScalar(DoubleRange.of(0, 4)),
			ExponentialRankSelector(),
			LinearRankSelector(),
			TournamentSelector(tournamentSize),
			TruncationSelector(),
			(final Double a,
			final Selector<G, C> s1,
			final Selector<G, C> s2,
			final Selector<G, C> s3,
			final Selector<G, C> s4) -> ISeq.of(s1, s2, s3, s4).get(a.intValue())
		);
	}

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	Codec<Selector<G, C>, DoubleGene> ExponentialRankSelector() {
		return Codec.of(
			Genotype.of(DoubleChromosome.of(0, 1)),
			gt -> new ExponentialRankSelector<>(gt.getGene().doubleValue())
		);
	}

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	Codec<Selector<G, C>, DoubleGene> LinearRankSelector() {
		return Codec.of(
			Genotype.of(DoubleChromosome.of(0, 1)),
			gt -> new LinearRankSelector<>(gt.getGene().doubleValue())
		);
	}

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	Codec<Selector<G, C>, DoubleGene> TournamentSelector(final IntRange size) {
		return Codec.of(
			Genotype.of(DoubleChromosome.of(size.doubleRange())),
			gt -> new TournamentSelector<>(gt.getGene().intValue())
		);
	}

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	Codec<Selector<G, C>, DoubleGene> TruncationSelector() {
		return Codec.of(
			Genotype.of(DoubleChromosome.of(0, 1)),
			gt -> new TruncationSelector<>()
		);
	}





	public static <G extends Gene<?, G>, C extends Number & Comparable<? super C>>
	Codec<Selector<G, C>, DoubleGene> RouletteWheelSelector() {
		return Codec.of(
			Genotype.of(DoubleChromosome.of(0, 1)),
			gt -> new RouletteWheelSelector<>()
		);
	}

}
