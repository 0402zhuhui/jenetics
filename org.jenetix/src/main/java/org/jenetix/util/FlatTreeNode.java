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

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jenetics.util.MSeq;

/**
 * Tree implementation, where the nodes of the whole tree are stored in an array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class FlatTreeNode<T> implements Tree<T, FlatTreeNode<T>> {

	private final int _index;
	private final MSeq<? extends T> _nodes;
	private final int[] _childOffsets;
	private final int[] _childCounts;

	private FlatTreeNode(
		final int index,
		final MSeq<? extends T> nodes,
		final int[] childOffsets,
		final int[] childCounts
	) {
		_index = index;
		_nodes = requireNonNull(nodes);
		_childOffsets = childOffsets;
		_childCounts = childCounts;
	}

	/**
	 * Returns the root of the tree that contains this node. The root is the
	 * ancestor with no parent. This implementation have a runtime complexity
	 * of O(1).
	 *
	 * @return the root of the tree that contains this node
	 */
	@Override
	public FlatTreeNode<T> getRoot() {
		return node(0);
	}

	@Override
	public boolean isRoot() {
		return _index == 0;
	}

	private FlatTreeNode<T> node(final int index) {
		return new FlatTreeNode<T>(
			index,
			_nodes,
			_childOffsets,
			_childCounts
		);
	}

	@Override
	public T getValue() {
		return _nodes.get(_index);
	}

	@Override
	public Optional<FlatTreeNode<T>> getParent() {
		return breathFirstStream()
			.filter(node -> node.childStream()
				.anyMatch(n -> n._nodes == _nodes && n._index == _index))
			.findFirst();
	}

	@Override
	public FlatTreeNode<T> getChild(final int index) {
		if (index < 0 || index >= childCount()) {
			throw new IndexOutOfBoundsException("" + index);
		}

		return new FlatTreeNode<T>(
			childOffset() + index,
			_nodes,
			_childOffsets,
			_childCounts
		);
	}

	@Override
	public int childCount() {
		return _childCounts[_index];
	}

	/**
	 * Return the index of the first child node in the underlying node array.
	 * {@code -1} is returned if {@code this} node is a leaf.
	 *
	 * @return Return the index of the first child node in the underlying node
	 *         array, or {@code -1} if {@code this} node is a leaf
	 */
	public int childOffset() {
		return _childOffsets[_index];
	}

	/**
	 * Return a stream of all nodes of the whole underlying tree. This method
	 * call is equivalent to
	 * <pre>{@code
	 * final Stream<FlatTreeNode<T>> nodes = getRoot().breathFirstStream();
	 * }</pre>
	 *
	 * @return
	 */
	public Stream<FlatTreeNode<T>> rootStream() {
		return IntStream.range(0, _nodes.size()).mapToObj(this::node);
	}

	@Override
	public String toString() {
		return Objects.toString(getValue());
	}

	/**
	 * Create a new {@code FlatTreeNode} from the given {@code tree}.
	 *
	 * @param tree the source tree
	 * @param <V> the tree value types
	 * @param <T> the tree type
	 * @return a new {@code FlatTreeNode} from the given {@code tree}
	 * @throws NullPointerException if the given {@code tree} is {@code null}
	 */
	public static <V, T extends Tree<? extends V, T>>
	FlatTreeNode<V> of(final T tree) {
		requireNonNull(tree);

		final int size = tree.size();
		final MSeq<V> elements = MSeq.ofLength(size);
		final int[] childOffsets = new int[size];
		final int[] childCounts = new int[size];

		assert size >= 1;
		final FlatTreeNode<V> root = new FlatTreeNode<>(
			0,
			elements,
			childOffsets,
			childCounts
		);

		int childOffset = 1;
		int index = 0;
		final Iterator<T> it = tree.breadthFirstIterator();
		while (it.hasNext()) {
			final T node = it.next();

			elements.set(index, node.getValue());
			childCounts[index] = node.childCount();
			childOffsets[index] = node.isLeaf() ? -1 : childOffset;

			childOffset += node.childCount();
			++index;
		}

		return root;
	}

}
