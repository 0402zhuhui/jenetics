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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Definition of different tree formatter strategies.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
abstract class TreeFormatter {

	/**
	 * This formatter creates tree strings, which formats the given tree
	 * <pre>
	 *  mul(div(cos(1.0), cos(π)), sin(mul(1.0, z)))
	 * </pre>
	 * to
	 * <pre>
	 *  mul
	 *  ├── div
	 *  │   ├── cos
	 *  │   │   └── 1.0
	 *  │   └── cos
	 *  │       └── π
	 *  └── sin
	 *      └── mul
	 *          ├── 1.0
	 *          └── z
	 *  </pre>
	 */
	static final TreeFormatter TREE_STRING = new TreeFormatter() {

		@Override
		<V, T extends Tree<V, T>> String format(
			final T tree,
			final Function<? super V, ? extends CharSequence> mapper
		) {
			return toStrings(tree, mapper).stream()
				.map(StringBuilder::toString)
				.collect(Collectors.joining("\n"));
		}

		private <V, T extends Tree<V, T>> List<StringBuilder> toStrings(
			final T tree,
			final Function<? super V, ? extends CharSequence> mapper
		) {
			final List<StringBuilder> result = new ArrayList<>();
			result.add(new StringBuilder().append(mapper.apply(tree.getValue())));

			final Iterator<T> it = tree.childIterator();
			while (it.hasNext()) {
				final List<StringBuilder> subtree = toStrings(it.next(), mapper);
				if (it.hasNext()) {
					subtree(result, subtree, mapper);
				} else {
					lastSubtree(result, subtree);
				}
			}
			return result;
		}

		private <V, T extends Tree<V, T>> void subtree(
			final List<StringBuilder> result,
			final List<StringBuilder> subtree,
			final Function<? super V, ? extends CharSequence> mapper
		) {
			final Iterator<StringBuilder> it = subtree.iterator();
			result.add(it.next().insert(0, "├── "));
			while (it.hasNext()) {
				result.add(it.next().insert(0, "│   "));
			}
		}

		private void lastSubtree(
			final List<StringBuilder> result,
			final List<StringBuilder> subtree
		) {
			final Iterator<StringBuilder> it = subtree.iterator();
			result.add(it.next().insert(0, "└── "));
			while (it.hasNext()) {
				result.add(it.next().insert(0, "    "));
			}
		}
	};

	/**
	 * Formats the given {@code tree} to its string representation. The given
	 * {@code mapper} is used for converting the node type {@code V} to a string
	 * value.
	 *
	 * @param tree the input tree to format
	 * @param mapper the tree node value mapper
	 * @param <V> the tree node type
	 * @param <T> the tree type
	 * @return the string representation of the given {@code tree}
	 * @throws NullPointerException if one of the arguments is {@code null}
	 */
	abstract  <V, T extends Tree<V, T>> String format(
		final T tree,
		final Function<? super V, ? extends CharSequence> mapper
	);

	/**
	 * Formats the given {@code tree} to its string representation.
	 *
	 * @param tree the input tree to format
	 * @param <V> the tree node type
	 * @param <T> the tree type
	 * @return the string representation of the given {@code tree}
	 * @throws NullPointerException if the {@code tree} is {@code null}
	 */
	 <V, T extends Tree<V, T>> String format(final T tree) {
	 	return format(tree, Objects::toString);
	 }

}
