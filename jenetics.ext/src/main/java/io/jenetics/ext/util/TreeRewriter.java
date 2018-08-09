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

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class TreeRewriter {

	@FunctionalInterface
	public static interface Matcher<V> {
		public boolean matches(final Tree<V, ?> node);
	}

	public static interface Rule<V> {

		public boolean matches(final TreeNode<V> node);

		public void rewrite(final TreeNode<V> node);

	}

	public static <V>
	int rewrite(final TreeNode<V> node, final List<Rule<V>> rules) {
		if (node.isLeaf()) {
			return 0;
		} else {
			final Optional<Rule<V>> simplifier= rules.stream()
				.filter(s -> s.matches(node))
				.findFirst();

			simplifier.ifPresent(r -> r.rewrite(node));
			return node.childStream()
				.mapToInt(child -> rewrite(child, rules))
				.sum();
		}
	}

	@SafeVarargs
	public static <V>
	int rewrite(final TreeNode<V> node, final Rule<V>... rules) {
		return rewrite(node, Arrays.asList(rules));
	}

}
