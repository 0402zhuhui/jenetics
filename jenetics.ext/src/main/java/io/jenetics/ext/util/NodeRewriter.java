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

import java.util.Map;
import java.util.stream.Collectors;

import io.jenetics.ext.util.Tree.Path;

/**
 * <pre>{@code
 * <x:expr> + 0 -> <x>
 * <x:expr> * 1 -> <x>
 * <x:expr> * 0 -> 0
 *
 * add(<x>,0) -> <x>
 * mul(<x>,1) -> <x>
 * add(<x>,0,<y>) -> add(<x>,<y>)
 *
 *
 * }</pre>
 *
 * add(X,0) -> X
 * mul(X,0) -> 0
 * sin(neg(X)) -> neg(sin(X))
 *
 * This class is responsible for rewriting a single node with the given set
 * of paths to be replaced.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class NodeRewriter<V> {

	private final Map<Character, Path> _replace;

	private NodeRewriter(final Map<Character, Path> replace) {
		_replace = requireNonNull(replace);
	}

	public void rewrite(final TreeNode<V> root) {
		// The tree paths replaced by the actual nodes for the given root.
		final Map<Character, TreeNode<V>> nodes = _replace.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> root.childAtPath(e.getValue())
					.orElseThrow(AssertionError::new)));
	}

}
