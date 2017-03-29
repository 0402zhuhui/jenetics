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
package org.jenetics.xml;

import static org.jenetics.xml.stream.Writer.attr;
import static org.jenetics.xml.stream.Writer.elem;
import static org.jenetics.xml.stream.Writer.elems;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jenetics.BitChromosome;
import org.jenetics.BoundedChromosome;
import org.jenetics.BoundedGene;
import org.jenetics.CharacterChromosome;
import org.jenetics.Chromosome;
import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.Gene;
import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.LongChromosome;
import org.jenetics.xml.stream.Writer;
import org.jenetics.xml.stream.XML;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class Writers {

	public static final Writer<BitChromosome> BIT_CHROMOSOME = null;

	public static final Writer<CharacterChromosome> CHARACTER_CHROMOSOME = null;

	public static final Writer<IntegerChromosome>
	INTEGER_CHROMOSOME = boundedChromosome("integer-chromosome");

	public static final Writer<LongChromosome>
	LONG_CHROMOSOME = boundedChromosome("long-chromosome");

	public static final Writer<DoubleChromosome>
	DOUBLE_CHROMOSOME = boundedChromosome("double-chromosome");

	private Writers() {
	}

	public static Writer<IntegerChromosome> integerChromosome() {
		return INTEGER_CHROMOSOME;
	}

	public static Writer<LongChromosome> longChromosome() {
		return LONG_CHROMOSOME;
	}

	public static Writer<DoubleChromosome> doubleChromosome() {
		return DOUBLE_CHROMOSOME;
	}

	private static <
		A extends Comparable<? super A>,
		G extends BoundedGene<A, G>,
		C extends BoundedChromosome<A, G>
	>
	Writer<C> boundedChromosome(final String root) {
		return elem(root,
			attr("min", BoundedChromosome<A, G>::getMin),
			attr("max", BoundedChromosome<A, G>::getMax),
			attr("length", BoundedChromosome<A, G>::length),
			elems("allele", ch -> ch.toSeq().map(G::getAllele))
		);
	}

	public static <
		A ,
		G extends Gene<A, G>,
		C extends Chromosome<G>
	>
	Writer<Genotype<G>> genotype(final Writer<C> writer) {
		return elem("genotype",
			attr("length", Genotype<G>::length),
			attr("ngenes", Genotype<G>::getNumberOfGenes),
			elems(gt -> gt.toSeq().map(Writers::cast), writer)
		);
	}

	public static <
		A ,
		G extends Gene<A, G>,
		C extends Chromosome<G>
	>
	Writer<Collection<Genotype<G>>> genotypes(final Writer<C> writer) {
		return elems("genotypes", genotype(writer));
	}

	@SuppressWarnings("unchecked")
	private static <A, B> B cast(final A value) {
		return (B)value;
	}

	public static void main(final String[] args) throws Exception {
		final DoubleChromosome ch = DoubleChromosome.of(0, 1, 10);
		final Genotype<DoubleGene> gt = Genotype.of(ch, 5);

		//final Writer<Genotype<DoubleGene>> writer = genotype(DOUBLE_CHROMOSOME);
		//writer.write(gt, XML.writer(System.out, "    "));
		//System.out.flush();

		final List<Genotype<DoubleGene>> types = Stream
			.generate(gt::newInstance)
			.limit(10)
			.collect(Collectors.toList());

		final Writer<Collection<Genotype<DoubleGene>>> writers = genotypes(DOUBLE_CHROMOSOME);
		writers.write(types, XML.writer(System.out, "    "));
		System.out.flush();
	}
}
