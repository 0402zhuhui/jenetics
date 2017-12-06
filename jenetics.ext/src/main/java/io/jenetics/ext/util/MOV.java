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
package io.jenetics.ext.util;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.Comparator;

/**
 * Defines the needed methods for a multi-objective fitness value.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public interface MOV<T> extends ComponentComparable<T>, Comparable<MOV<T>> {

	/**
	 * Return the underlying data structure.
	 *
	 * @return the underlying data structure
	 */
	public T value();

	/**
	 * Return the comparator for comparing the elements of this MO vector.
	 *
	 * @return the comparator for comparing the elements of this MO vector
	 */
	public ComponentComparator<T> comparator();

	/**
	 * Return the comparator which defines the (Pareto) dominance measure.
	 *
	 * @return the comparator which defines the (Pareto) dominance measure
	 */
	public Comparator<T> dominance();


	/* *************************************************************************
	 * Default methods derived from the methods above.
	 * ************************************************************************/

	/**
	 * Compares the {@code this} vector with the {@code other} at the given
	 * component {@code index}.
	 *
	 * @param other the other vector
	 * @param index the component index
	 * @return a negative integer, zero, or a positive integer as
	 *        {@code this[index]} is less than, equal to, or greater than
	 *        {@code other[index]}
	 * @throws NullPointerException if the {@code other} object is {@code null}
	 */
	@Override
	public default int compareTo(final T other, final int index) {
		return comparator().compare(value(), other, index);
	}

	/**
	 * Calculates the <a href="https://en.wikipedia.org/wiki/Pareto_efficiency">
	 * <b>Pareto Dominance</b></a> of vector {@code value()} and {@code other}.
	 *
	 * @param other the other vector
	 * @return {@code 1} if <b>value()</b> ≻ <b>other</b>, {@code -1} if
	 *         <b>other</b> ≻ <b>value()</b> and {@code 0} otherwise
	 * @throws NullPointerException if the {@code other} vector is {@code null}
	 */
	public default int dominance(final T other) {
		return dominance().compare(value(), other);
	}

	/**
	 * The default implementation uses the {@link #dominance(Object)} function
	 * for defining a <b>partial</b> order of two vectors.
	 *
	 * @param other the other vector
	 * @return {@code 1} if <b>value()</b> ≻ <b>other</b>, {@code -1} if
	 *         <b>other</b> ≻ <b>value()</b> and {@code 0} otherwise
	 * @throws NullPointerException if the {@code other} vector is {@code null}
	 */
	@Override
	public default int compareTo(final MOV<T> other) {
		return dominance(other.value());
	}

	/* *************************************************************************
	 * Static factory functions for wrapping ordinary arrays.
	 * ************************************************************************/

	/**
	 * Wraps the given array into a {@code MOV} object.
	 *
	 * @param array the wrapped array
	 * @param <C> the array element type
	 * @return the given array wrapped into a {@code MOV} object.
	 * @throws NullPointerException if one of the arguments is {@code null}
	 */
	public static <C extends Comparable<? super C>> MOV<C[]> of(final C[] array) {
		return of(array, Comparator.naturalOrder());
	}

	/**
	 * Wraps the given array into a {@code MOV} object.
	 *
	 * @param array the wrapped array
	 * @param comparator the (natural order) comparator of the array elements
	 * @param <T> the array element type
	 * @return the given array wrapped into a {@code MOV} object.
	 * @throws NullPointerException if one of the arguments is {@code null}
	 */
	public static <T> MOV<T[]> of(
		final T[] array,
		final Comparator<? super T> comparator
	) {
		requireNonNull(array);
		requireNonNull(comparator);

		return new MOV<T[]>() {
			@Override
			public T[] value() {
				return array;
			}

			@Override
			public ComponentComparator<T[]> comparator() {
				return (u, v, i) -> comparator.compare(u[i], v[i]);
			}

			@Override
			public Comparator<T[]> dominance() {
				return (u, v) -> Pareto.dominance(u, v, comparator);
			}

			@Override
			public int size() {
				return array.length;
			}
		};
	}

	/**
	 * Wraps the given array into a {@code MOV} object.
	 *
	 * @param array the wrapped array
	 * @return the given array wrapped into a {@code MOV} object.
	 * @throws NullPointerException if the given {@code array} is {@code null}
	 */
	public static MOV<int[]> of(final int[] array) {
		requireNonNull(array);

		return new MOV<int[]>() {
			@Override
			public int[] value() {
				return array;
			}

			@Override
			public ComponentComparator<int[]> comparator() {
				return (u, v, i) -> {
					if (i < 0 || i > 1) {
						throw new IndexOutOfBoundsException(format(
							"Index out of bounds [0, %d): %d", size(), i
						));
					}

					return Integer.compare(u[i], v[i]);
				};
			}

			@Override
			public Comparator<int[]> dominance() {
				return Pareto::dominance;
			}

			@Override
			public int size() {
				return array.length;
			}
		};
	}

	/**
	 * Wraps the given array into a {@code MOV} object.
	 *
	 * @param array the wrapped array
	 * @return the given array wrapped into a {@code MOV} object.
	 * @throws NullPointerException if the given {@code array} is {@code null}
	 */
	public static MOV<long[]> of(final long[] array) {
		requireNonNull(array);

		return new MOV<long[]>() {
			@Override
			public long[] value() {
				return array;
			}

			@Override
			public ComponentComparator<long[]> comparator() {
				return (u, v, i) -> {
					if (i < 0 || i > 1) {
						throw new IndexOutOfBoundsException(format(
							"Index out of bounds [0, %d): %d", size(), i
						));
					}

					return Long.compare(u[i], v[i]);
				};
			}

			@Override
			public Comparator<long[]> dominance() {
				return Pareto::dominance;
			}

			@Override
			public int size() {
				return array.length;
			}
		};
	}

	/**
	 * Wraps the given array into a {@code MOV} object.
	 *
	 * @param array the wrapped array
	 * @return the given array wrapped into a {@code MOV} object.
	 * @throws NullPointerException if the given {@code array} is {@code null}
	 */
	public static MOV<double[]> of(final double[] array) {
		requireNonNull(array);

		return new MOV<double[]>() {
			@Override
			public double[] value() {
				return array;
			}

			@Override
			public ComponentComparator<double[]> comparator() {
				return (u, v, i) -> {
					if (i < 0 || i > 1) {
						throw new IndexOutOfBoundsException(format(
							"Index out of bounds [0, %d): %d", size(), i
						));
					}

					return Double.compare(u[i], v[i]);
				};
			}

			@Override
			public Comparator<double[]> dominance() {
				return Pareto::dominance;
			}

			@Override
			public int size() {
				return array.length;
			}
		};
	}

}
