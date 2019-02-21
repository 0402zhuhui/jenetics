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

import static java.util.Objects.requireNonNull;

import java.util.stream.IntStream;

import io.jenetics.util.ISeq;
import io.jenetics.util.MSeq;
import io.jenetics.util.Seq;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class SectionAlterer<
	G extends Gene<?, G>,
	C extends Comparable<? super C>
>
	implements Alterer<G, C>
{

	private final Alterer<G, C> _alterer;
	private final Section _section;

	private SectionAlterer(final Alterer<G, C> alterer, final Section section) {
		_alterer = requireNonNull(alterer);
		_section = requireNonNull(section);
	}

	@Override
	public AltererResult<G, C>
	alter(final Seq<Phenotype<G, C>> population, final long generation) {
		final Seq<Phenotype<G, C>> split  = _section.split(population);
		final AltererResult<G, C> result = _alterer.alter(split, generation);

		return AltererResult.of(
			_section.merge(result.getPopulation(), population),
			result.getAlterations()
		);
	}

	public static <G extends Gene<?, G>, C extends Comparable<? super C>>
	SectionAlterer<G, C> of(final Alterer<G, C> alterer, final int... indices) {
		return new SectionAlterer<>(alterer, new Section(indices));
	}

	static final class Section {
		final int[] indices;

		Section(final int[] indices) {
			this.indices = indices;
		}

		<G extends Gene<?, G>, C extends Comparable<? super C>>
		Seq<Phenotype<G, C>> split(final Seq<Phenotype<G, C>> population) {
			return population.map(this::split);
		}

		<G extends Gene<?, G>, C extends Comparable<? super C>>
		Phenotype<G, C> split(final Phenotype<G, C> phenotype) {
			final ISeq<Chromosome<G>> chromosomes = IntStream.of(indices)
				.mapToObj(phenotype.getGenotype()::get)
				.collect(ISeq.toISeq());

			final Genotype<G> genotype = Genotype.of(chromosomes);

			return phenotype.isEvaluated()
				? Phenotype.of(
					genotype,
					phenotype.getGeneration(),
					phenotype.getFitness())
				: Phenotype.of(genotype, phenotype.getGeneration());
		}

		<G extends Gene<?, G>, C extends Comparable<? super C>>
		ISeq<Phenotype<G, C>> merge(
			final Seq<Phenotype<G, C>> section,
			final Seq<Phenotype<G, C>> population
		) {
			assert section.length() == population.length();

			return IntStream.range(0, section.length())
				.mapToObj(i -> merge(section.get(i), population.get(i)))
				.collect(ISeq.toISeq());
		}

		<G extends Gene<?, G>, C extends Comparable<? super C>>
		Phenotype<G, C> merge(
			final Phenotype<G, C> section,
			final Phenotype<G, C> phenotype
		) {
			final MSeq<Chromosome<G>> chromosomes = phenotype.getGenotype()
				.toSeq()
				.copy();

			for (int i = 0; i < indices.length; ++i) {
				chromosomes.set(indices[i], section.getGenotype().get(i));
			}

			final Genotype<G> genotype = Genotype.of(chromosomes);

			return phenotype.isEvaluated()
				? Phenotype.of(
					genotype,
					phenotype.getGeneration(),
					phenotype.getFitness())
				: Phenotype.of(genotype, phenotype.getGeneration());
		}

		static Section of(final int... indices) {
			return new Section(indices);
		}

	}


}
