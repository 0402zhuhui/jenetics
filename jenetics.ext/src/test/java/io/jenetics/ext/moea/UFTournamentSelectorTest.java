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
package io.jenetics.ext.moea;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.jenetics.DoubleGene;
import io.jenetics.Optimize;
import io.jenetics.Phenotype;
import io.jenetics.Selector;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Problem;
import io.jenetics.util.DoubleRange;
import io.jenetics.util.ISeq;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class UFTournamentSelectorTest {

	private static final Problem<double[], DoubleGene, Vec<double[]>> PROBLEM = Problem.of(
		v -> Vec.of(new double[]{v[0]*cos(v[1]), v[0]*sin(v[1])}),
		Codecs.ofVector(
			DoubleRange.of(0, 1),
			DoubleRange.of(0, 2*PI)
		)
	);

	@Test
	public void select() {
		final Selector<DoubleGene, Vec<double[]>> selector =
			UFTournamentSelector.of();
			//new TournamentSelector<>(10);

		final ISeq<Phenotype<DoubleGene, Vec<double[]>>> population =
			Stream.generate(this::phenotype)
				.limit(500)
				.collect(ISeq.toISeq());

		final ISeq<Vec<double[]>> populationFront = Pareto.front(
			population.stream()
				.map(Phenotype::getFitness)
				.collect(ISeq.toISeq())
		);

		final ISeq<Phenotype<DoubleGene, Vec<double[]>>> selected =
			selector.select(population, 100, Optimize.MAXIMUM);

		Assert.assertEquals(
			selected.stream()
				.map(Phenotype::getFitness)
				.collect(Collectors.toSet())
				.size(),
			selected.size()
		);

		final ISeq<Vec<double[]>> selectedFront = Pareto.front(
			selected.stream()
				.map(Phenotype::getFitness)
				.collect(ISeq.toISeq())
		);

		Assert.assertTrue(
			populationFront.size()/(double)population.size() <
				selectedFront.size()/(double)selected.size()
		);

	}

	private Phenotype<DoubleGene, Vec<double[]>> phenotype() {
		return Phenotype.of(
			PROBLEM.codec().encoding().newInstance(),
			1L,
			gt -> PROBLEM.fitness().apply(PROBLEM.codec().decode(gt))
		);
	}

}
