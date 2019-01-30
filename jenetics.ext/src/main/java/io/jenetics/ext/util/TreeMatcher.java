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

import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * Implementation of a pattern based tree matcher.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class TreeMatcher<V> {

	private final TreePattern _pattern;
	private final Tree<V, ?> _tree;
	private final BiPredicate<V, String> _equals;

	private TreeMatcher(
		final TreePattern pattern,
		final Tree<V, ?> tree,
		final BiPredicate<V, String> equals
	) {
		_pattern = requireNonNull(pattern);
		_tree = requireNonNull(tree);
		_equals = requireNonNull(equals);
	}

	TreePattern pattern() {
		return _pattern;
	}

	boolean matches() {
		return results().findFirst().isPresent();
	}

	Stream<Tree<V, ?>> results() {
		@SuppressWarnings("unchecked")
		final Stream<Tree<V, ?>> ts = (Stream<Tree<V, ?>>)_tree.stream();
		return  ts.filter(_pattern::matches);
	}

	static <V> TreeMatcher<V> of(
		final TreePattern pattern,
		final Tree<V, ?> tree,
		final BiPredicate<V, String> equals
	) {
		return new TreeMatcher<>(pattern, tree, equals);
	}

}
