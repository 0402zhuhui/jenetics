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

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Iterator;

/**
 * Postorder iterator of the tree.
 */
final class Postorder<T> implements Iterator<TreeNode<T>> {
	private TreeNode<T> _root;
	private final Iterator<TreeNode<T>> _children;
	private Iterator<TreeNode<T>> _subtree;

	Postorder(final TreeNode<T> root) {
		_root = requireNonNull(root);
		_children = _root.childIterator();
		_subtree = Collections.emptyIterator();
	}

	@Override
	public boolean hasNext() {
		return _root != null;
	}

	@Override
	public TreeNode<T> next() {
		TreeNode<T> result;

		if (_subtree.hasNext()) {
			result = _subtree.next();
		} else if (_children.hasNext()) {
			_subtree = new Postorder<>(_children.next());
			result = _subtree.next();
		} else {
			result = _root;
			_root = null;
		}

		return result;
	}

}
