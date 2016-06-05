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
package org.jenetix.util;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Preorder iterator of the tree.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class TreeNodePreorderIterator<T> implements Iterator<TreeNode<T>> {
	private final Deque<Iterator<TreeNode<T>>> _deque = new LinkedList<>();

	TreeNodePreorderIterator(final TreeNode<T> root) {
		requireNonNull(root);
		_deque.push(singletonList(root).iterator());
	}

	@Override
	public boolean hasNext() {
		return !_deque.isEmpty() && _deque.peek().hasNext();
	}

	@Override
	public TreeNode<T> next() {
		final Iterator<TreeNode<T>> it = _deque.peek();
		final TreeNode<T> node = it.next();
		if (!it.hasNext()) {
			_deque.pop();
		}

		final Iterator<TreeNode<T>> children = node.childIterator();
		if (children.hasNext()) {
			_deque.push(children);
		}

		return node;
	}
}
