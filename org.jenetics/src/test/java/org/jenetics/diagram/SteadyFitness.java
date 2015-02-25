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
package org.jenetics.diagram;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.jenetics.engine.limit.bySteadyFitness;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.DoubleGene;
import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.SinglePointCrossover;
import org.jenetics.TournamentSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStream;
import org.jenetics.stat.DoubleMomentStatistics;
import org.jenetics.stat.Quantile;
import org.jenetics.util.RandomRegistry;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
public class SteadyFitness {

	private static Double eval(final Genotype<DoubleGene> gt) {
		final double x = gt.getGene().doubleValue();
		return cos(0.5 + sin(x))*cos(x);
	}

	private static Engine<BitGene, Double> engine() {
		final int nitems = 50;
		final double kssize = nitems*100.0/3.0;

		final FF ff = new FF(
			Stream.generate(Item::random)
				.limit(nitems)
				.toArray(Item[]::new),
			kssize
		);

		// Configure and build the evolution engine.
		return Engine
			.builder(ff, BitChromosome.of(nitems, 0.5))
			.populationSize(250)
			.survivorsSelector(new TournamentSelector<>(5))
			.offspringSelector(new RouletteWheelSelector<>())
			.alterers(
				new Mutator<>(0.115),
				new SinglePointCrossover<>(0.16))
			.build();
	}

	public static void main(final String[] args) throws IOException {
		final SteadyFitnessTermination<BitGene> test =
			new SteadyFitnessTermination<>(engine(), 150);

		final String[] header = new String[]{
			"1: Generation",
			"2: Total generation median", "3: lower quartile", "4: upper quartile", "5: min", "6: max",
			"7: Fitness median", "8: lower quartile", "9: upper quartile", "10: min", "11: max"
		};
		final Stream<String[]> headerStream = Stream.<String[]>builder().add(header).build();

		IntStream generations = IntStream.range(1, 50)
			.map(i -> Math.max((int) Math.pow(1.115, i), i));

		Object[][] data = Stream
			.concat(
				headerStream,
				generations.mapToObj(test::run))
			.toArray(Object[][]::new);

		SteadyFitnessTermination.write(
			new File("org.jenetics/src/test/scripts/diagram/steady_fitness_termination.dat"),
			data
		);
	}

}

// This class represents a knapsack item, with a specific
// "size" and "value".
final class Item {
	public final double size;
	public final double value;

	Item(final double size, final double value) {
		this.size = size;
		this.value = value;
	}

	// Create a new random knapsack item.
	static Item random() {
		final Random r = RandomRegistry.getRandom();
		return new Item(r.nextDouble()*100, r.nextDouble()*100);
	}

	// Create a new collector for summing up the knapsack items.
	static Collector<Item, ?, Item> toSum() {
		return Collector.of(
			() -> new double[2],
			(a, b) -> {a[0] += b.size; a[1] += b.value;},
			(a, b) -> {a[0] += b[0]; a[1] += b[1]; return a;},
			r -> new Item(r[0], r[1])
		);
	}
}

// The knapsack fitness function class, which is parametrized with
// the available items and the size of the knapsack.
final class FF
	implements Function<Genotype<BitGene>, Double>
{
	private final Item[] items;
	private final double size;

	public FF(final Item[] items, final double size) {
		this.items = items;
		this.size = size;
	}

	@Override
	public Double apply(final Genotype<BitGene> gt) {
		final Item sum = ((BitChromosome)gt.getChromosome()).ones()
			.mapToObj(i -> items[i])
			.collect(Item.toSum());

		return sum.size <= this.size ? sum.value : 0;
	}
}
