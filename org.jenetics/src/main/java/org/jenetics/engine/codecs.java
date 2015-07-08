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

import static java.lang.reflect.Array.newInstance;
import static java.util.Objects.requireNonNull;

import java.awt.geom.AffineTransform;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import org.jenetics.internal.util.require;

import org.jenetics.DoubleChromosome;
import org.jenetics.DoubleGene;
import org.jenetics.EnumGene;
import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.LongChromosome;
import org.jenetics.LongGene;
import org.jenetics.PermutationChromosome;
import org.jenetics.util.DoubleRange;
import org.jenetics.util.ISeq;
import org.jenetics.util.IntRange;
import org.jenetics.util.LongRange;

/**
 * This class contains factory methods for creating common  problem encodings.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class codecs {

	private codecs() {require.noInstance();}

	/**
	 * Return a scalar {@code Codec} for the given range.
	 *
	 * @param domain the domain of the returned {@code Codec}
	 * @return a new scalar {@code Codec} with the given domain.
	 * @throws NullPointerException if the given {@code domain} is {@code null}
	 */
	public static Codec<Integer, IntegerGene> ofScalar(final IntRange domain) {
		requireNonNull(domain);

		return Codec.of(
			Genotype.of(IntegerChromosome.of(domain)),
			gt -> gt.getChromosome().getGene().getAllele()
		);
	}

	/**
	 * Return a scalar {@code Codec} for the given range.
	 *
	 * @param domain the domain of the returned {@code Codec}
	 * @return a new scalar {@code Codec} with the given domain.
	 * @throws NullPointerException if the given {@code domain} is {@code null}
	 */
	public static Codec<Long, LongGene> ofScalar(final LongRange domain) {
		requireNonNull(domain);

		return Codec.of(
			Genotype.of(LongChromosome.of(domain)),
			gt -> gt.getChromosome().getGene().getAllele()
		);
	}

	/**
	 * Return a scalar {@code Codec} for the given range.
	 *
	 * @param domain the domain of the returned {@code Codec}
	 * @return a new scalar {@code Codec} with the given domain.
	 * @throws NullPointerException if the given {@code domain} is {@code null}
	 */
	public static Codec<Double, DoubleGene> ofScalar(final DoubleRange domain) {
		requireNonNull(domain);

		return Codec.of(
			Genotype.of(DoubleChromosome.of(domain)),
			gt -> gt.getChromosome().getGene().getAllele()
		);
	}

	/**
	 * Return a vector {@code Codec} for the given range. All vector values
	 * are restricted by the same domain.
	 *
	 * @param domain the domain of the vector values
	 * @param length the vector length
	 * @return a new vector {@code Codec}
	 * @throws NullPointerException if the given {@code domain} is {@code null}
	 * @throws IllegalArgumentException if the {@code length} is smaller than
	 *         one.
	 */
	public static Codec<int[], IntegerGene> ofVector(
		final IntRange domain,
		final int length
	) {
		requireNonNull(domain);
		require.positive(length);

		return Codec.of(
			Genotype.of(IntegerChromosome.of(domain, length)),
			gt -> ((IntegerChromosome)gt.getChromosome()).toArray()
		);
	}

	/**
	 * Return a vector {@code Codec} for the given range. All vector values
	 * are restricted by the same domain.
	 *
	 * @param domain the domain of the vector values
	 * @param length the vector length
	 * @return a new vector {@code Codec}
	 * @throws NullPointerException if the given {@code domain} is {@code null}
	 * @throws IllegalArgumentException if the {@code length} is smaller than
	 *         one.
	 */
	public static Codec<long[], LongGene> ofVector(
		final LongRange domain,
		final int length
	) {
		requireNonNull(domain);
		require.positive(length);

		return Codec.of(
			Genotype.of(LongChromosome.of(domain, length)),
			gt -> ((LongChromosome)gt.getChromosome()).toArray()
		);
	}

	/**
	 * Return a vector {@code Codec} for the given range. All vector values
	 * are restricted by the same domain.
	 *
	 * @param domain the domain of the vector values
	 * @param length the vector length
	 * @return a new vector {@code Codec}
	 * @throws NullPointerException if the given {@code domain} is {@code null}
	 * @throws IllegalArgumentException if the {@code length} is smaller than
	 *         one.
	 */
	public static Codec<double[], DoubleGene> ofVector(
		final DoubleRange domain,
		final int length
	) {
		requireNonNull(domain);
		require.positive(length);

		return Codec.of(
			Genotype.of(DoubleChromosome.of(domain, length)),
			gt -> ((DoubleChromosome)gt.getChromosome()).toArray()
		);
	}

	/**
	 * Create a vector {@code Codec} for the given ranges. Each vector element
	 * might have a different domain. The vector length is equal to the number
	 * of domains.
	 *
	 * @param domains the domain ranges
	 * @return a new vector {@code Codec}
	 * @throws NullPointerException if one of the arguments is {@code null}
	 * @throws IllegalArgumentException if the {@code domains} array is empty
	 */
	public static Codec<int[], IntegerGene> ofVector(final IntRange... domains) {
		if (domains.length == 0) {
			throw new IllegalArgumentException("Domains must not be empty.");
		}

		final ISeq<IntegerChromosome> chromosomes = Stream.of(domains)
			.map(IntegerGene::of)
			.map(IntegerChromosome::of)
			.collect(ISeq.toISeq());

		return Codec.of(
			Genotype.of(chromosomes),
			gt -> {
				final int[] args = new int[chromosomes.length()];
				for (int i = chromosomes.length(); --i >= 0;) {
					args[i] = gt.getChromosome(i).getGene().intValue();
				}
				return args;
			}
		);
	}

	/**
	 * Create a vector {@code Codec} for the given ranges. Each vector element
	 * might have a different domain. The vector length is equal to the number
	 * of domains.
	 *
	 * @param domains the domain ranges
	 * @return a new vector {@code Codec}
	 * @throws NullPointerException if one of the arguments is {@code null}
	 * @throws IllegalArgumentException if the {@code domains} array is empty
	 */
	public static Codec<long[], LongGene> ofVector(final LongRange... domains) {
		if (domains.length == 0) {
			throw new IllegalArgumentException("Domains must not be empty.");
		}

		final ISeq<LongChromosome> chromosomes = Stream.of(domains)
			.map(LongGene::of)
			.map(LongChromosome::of)
			.collect(ISeq.toISeq());

		return Codec.of(
			Genotype.of(chromosomes),
			gt -> {
				final long[] args = new long[chromosomes.length()];
				for (int i = chromosomes.length(); --i >= 0;) {
					args[i] = gt.getChromosome(i).getGene().longValue();
				}
				return args;
			}
		);
	}

	/**
	 * Create a vector {@code Codec} for the given ranges. Each vector element
	 * might have a different domain. The vector length is equal to the number
	 * of domains.
	 *
	 * @param domains the domain ranges
	 * @return a new vector {@code Codec}
	 * @throws NullPointerException if one of the arguments is {@code null}
	 * @throws IllegalArgumentException if the {@code domains} array is empty
	 */
	public static Codec<double[], DoubleGene> ofVector(
		final DoubleRange... domains
	) {
		if (domains.length == 0) {
			throw new IllegalArgumentException("Domains must not be empty.");
		}

		final ISeq<DoubleChromosome> chromosomes = Stream.of(domains)
			.map(DoubleGene::of)
			.map(DoubleChromosome::of)
			.collect(ISeq.toISeq());

		return Codec.of(
			Genotype.of(chromosomes),
			gt -> {
				final double[] args = new double[chromosomes.length()];
				for (int i = chromosomes.length(); --i >= 0;) {
					args[i] = gt.getChromosome(i).getGene().doubleValue();
				}
				return args;
			}
		);
	}

	/**
	 * Create a permutation {@code Codec} with the given alleles.
	 *
	 * @param alleles the alleles of the permutation
	 * @param <T> the allele type
	 * @return a new permutation {@code Codec}
	 * @throws IllegalArgumentException if the given allele array is empty
	 * @throws NullPointerException if one of the alleles is {@code null}
	 */
	@SafeVarargs
	public static <T> Codec<T[], EnumGene<T>> ofPermutation(final T... alleles) {
		if (alleles.length == 0) {
			throw new IllegalArgumentException("Empty alleles are not allowed.");
		}

		final IntFunction<T[]> newArray = length -> {
			@SuppressWarnings("unchecked")
			final T[] values = (T[])newInstance(alleles[0].getClass(), length);
			return values;
		};

		return Codec.of(
			Genotype.of(PermutationChromosome.of(alleles)),
			gt -> gt.getChromosome().toSeq().stream()
						.map(EnumGene::getAllele)
						.toArray(newArray)
		);
	}

	/**
	 * Create a permutation {@code Codec} of integer in the range
	 * {@code [0, length)}.
	 *
	 * @param length the number of permutation elements
	 * @return a permutation {@code Codec} of integers
	 */
	public static Codec<int[], EnumGene<Integer>> ofPermutation(final int length) {
		return Codec.of(
			Genotype.of(PermutationChromosome.ofInteger(length)),
			gt -> gt.getChromosome().toSeq().stream()
						.mapToInt(EnumGene<Integer>::getAllele)
						.toArray()
		);
	}

	// https://trac.osgeo.org/postgis/wiki/DevWikiAffineParameters

	/**
	 * Creates a codec for a 2-dimensional affine transformation. The composed
	 * order of the transformation is: <i>R&bull;Sc&bull;Sh&bull;T</i>
	 *
	 * @param sx the scale factor range in x direction
	 * @param sy the scale factor range in y direction
	 * @param tx the translation range in x direction
	 * @param ty the translation range in y direction
	 * @param th the rotation range (in radians)
	 * @param kx the shear range in x direction
	 * @param ky the shear range in x direction
	 * @return the affine transformation codec
	 * @throws NullPointerException if one of the arguments is {@code null}
	 */
	public static Codec<AffineTransform, DoubleGene> ofAffineTransform(
		final DoubleRange sx, final DoubleRange sy,
		final DoubleRange tx, final DoubleRange ty,
		final DoubleRange th,
		final DoubleRange kx, final DoubleRange ky
	) {
		return Codec.of(
			Genotype.of(
				// Scale
				DoubleChromosome.of(sx), DoubleChromosome.of(sy),
				// Translation
				DoubleChromosome.of(tx), DoubleChromosome.of(ty),
				// Rotation
				DoubleChromosome.of(th),
				// Shear
				DoubleChromosome.of(kx), DoubleChromosome.of(ky)
			),
			gt -> {
				final AffineTransform at = new AffineTransform();

				at.translate(
					gt.getChromosome(2).getGene().doubleValue(),
					gt.getChromosome(3).getGene().doubleValue()
				);
				at.shear(
					gt.getChromosome(5).getGene().doubleValue(),
					gt.getChromosome(6).getGene().doubleValue()
				);
				at.scale(
					gt.getChromosome(0).getGene().doubleValue(),
					gt.getChromosome(1).getGene().doubleValue()
				);
				at.rotate(gt.getChromosome(4).getGene().doubleValue());

				return at;
			}
		);
	}

	/**
	 * Creates a codec for a 2-dimensional affine transformation. The composed
	 * order of the transformation is: <i>R&bull;Sc&bull;Sh&bull;T</i>
	 *
	 * @param s the scale factor range in x and y direction
	 * @param t the translation range in x and y direction
	 * @param th the rotation angle range
	 * @param k the shear range in x and y direction
	 * @return the affine transformation codec
	 * @throws NullPointerException if one of the arguments is {@code null}
	 */
	public static Codec<AffineTransform, DoubleGene> ofAffineTransform(
		final DoubleRange s,
		final DoubleRange t,
		final DoubleRange th,
		final DoubleRange k
	) {
		return ofAffineTransform(s, s, t, t, th, k, k);
	}

}
