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

import static io.jenetics.ext.util.TreeMatcher.children;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.jenetics.ext.util.TreeRewriter.Matcher;

/**
 * Check if a given tree matches specific values.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class TreeValueMatcher<V> implements Matcher<V> {

	final Map<ChildPath, Object> _values;

	private TreeValueMatcher(final Map<ChildPath, Object> values) {
		_values = values;
	}

	@Override
	public boolean matches(final Tree<V, ?> tree) {
		final List<ChildPath> paths = new ArrayList<>(_values.keySet());
		final List<Tree<?, ?>> nodes = children(tree, paths);

		boolean matches = nodes.size() == paths.size();
		final Iterator<Tree<?, ?>> tit = nodes.iterator();
		final Iterator<ChildPath> pit = paths.iterator();

		while (matches && tit.hasNext()) {
			final Tree<?, ?> tn = tit.next();
			final ChildPath path = pit.next();

			matches = Objects.equals(_values.get(path), tn.getValue());
		}

		return matches;
	}

	static <V> TreeValueMatcher<V> of(final Tree<V, ?> pattern) {
		return null;
	}

}
