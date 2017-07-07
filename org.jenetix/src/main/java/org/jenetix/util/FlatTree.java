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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jenetics.util.ISeq;
import org.jenetics.util.MSeq;

/**
 * Tree implementation, where the nodes of the whole tree are stored in an array.
 * The tree
 * <pre>
 * 0
 * ├── 1
 * │   ├── 4
 * │   └── 5
 * ├── 2
 * │   └── 6
 * └── 3
 *     ├── 7
 *     │   ├── 10
 *     │   └── 11
 *     ├── 8
 *     └── 9
 * </pre>
 * will be stored as
 * <pre>
 * ┌─┬───┐       ┌──────┬──┐
 * 0 1 2 3 4 5 6 7 8 9 10 11
 *   └─│─│─┴─┘ │ │   │
 *     └─│─────┘ │   │
 *       └───────┴───┘
 * </pre>
 * The child nodes are always stored on the right side of the parent nodes. So
 * you have to read the tree from left to right. All children of a parent node
 * are stored continuously after the {@code childOffset} and are defined by the
 * sub-array {@code [childOffset, childOffset + childCount)}.
 * <p>
 * This class is mainly used in the {@code ProgramChromosome}, where it is
 * required to map a tree structure (the AST of the program) onto a
 * array-structured chromosome.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version 3.9
 * @since 3.9
 */
public final class FlatTree<T> implements Tree<T, FlatTree<T>> {

	private final int _index;
	private final MSeq<? extends T> _nodes;
	private final int[] _childOffsets;
	private final int[] _childCounts;

	private FlatTree(
		final int index,
		final MSeq<? extends T> nodes,
		final int[] childOffsets,
		final int[] childCounts
	) {
		_index = index;
		_nodes = requireNonNull(nodes);
		_childOffsets = requireNonNull(childOffsets);
		_childCounts = requireNonNull(childCounts);
	}

	/**
	 * Returns the root of the tree that contains this node. The root is the
	 * ancestor with no parent. This implementation have a runtime complexity
	 * of O(1).
	 *
	 * @return the root of the tree that contains this node
	 */
	@Override
	public FlatTree<T> getRoot() {
		return node(0);
	}

	@Override
	public boolean isRoot() {
		return _index == 0;
	}

	private FlatTree<T> node(final int index) {
		return new FlatTree<T>(
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
	public Optional<FlatTree<T>> getParent() {
		return stream()
			.filter(node -> node.childStream().anyMatch(this::identical))
			.findFirst();
	}

	@Override
	public FlatTree<T> getChild(final int index) {
		if (index < 0 || index >= childCount()) {
			throw new IndexOutOfBoundsException("" + index);
		}

		return new FlatTree<T>(
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
	 * Return the whole flattened tree values in breadth-first order. This is
	 * equivalent to
	 * <pre>{@code
	 * final ISeq<? extends T> seq = getRoot().breadthFirstStream()
	 *     .map(Tree::getValue)
	 *     .collect(ISeq.toISeq());
	 * }</pre>
	 *
	 * @return the flattened tree values in breadth-first order
	 */
	public ISeq<? extends T> nodes() {
		return _nodes.toISeq();
	}

	/**
	 * Return a stream of all nodes of the whole underlying tree. This method
	 * call is equivalent to
	 * <pre>{@code
	 * final Stream<FlatTreeNode<T>> nodes = getRoot().breadthFirstStream();
	 * }</pre>
	 *
	 * @return a stream of all nodes of the whole underlying tree
	 */
	public Stream<FlatTree<T>> stream() {
		return IntStream.range(0, _nodes.size()).mapToObj(this::node);
	}

	/**
	 * Return a sequence of all <em>mapped</em> nodes of the whole underlying
	 * tree. This is a convenient method for
	 * <pre>{@code
	 * final ISeq<B> seq = stream()
	 *     .map(mapper)
	 *     .collect(ISeq.toISeq())
	 * }</pre>
	 *
	 * @param mapper the mapper function
	 * @param <B> the mapped type
	 * @return a sequence of all <em>mapped</em> nodes
	 */
	public <B> ISeq<B> map(final Function<FlatTree<T>, ? extends B> mapper) {
		return stream()
			.map(mapper)
			.collect(ISeq.toISeq());
	}

	@Override
	public boolean identical(final Tree<?, ?> other) {
		return other instanceof FlatTree<?> &&
			((FlatTree)other)._index == _index &&
			((FlatTree)other)._nodes == _nodes;
	}

	@Override
	public int hashCode(){
		int hash = 17;
		hash += 31*_index + 37;
		hash += 31*_nodes.hashCode() + 37;
		hash += 31*Arrays.hashCode(_childCounts) + 37;
		hash += 31*Arrays.hashCode(_childOffsets) + 37;
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		return obj instanceof FlatTree<?> &&
			((FlatTree)obj)._index == _index &&
			Objects.equals(((FlatTree)obj)._nodes, _nodes) &&
			Arrays.equals(((FlatTree)obj)._childCounts, _childCounts) &&
			Arrays.equals(((FlatTree)obj)._childOffsets, _childOffsets);
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
	 * @return a new {@code FlatTreeNode} from the given {@code tree}
	 * @throws NullPointerException if the given {@code tree} is {@code null}
	 */
	public static <V> FlatTree<V> of(final Tree<? extends V, ?> tree) {
		requireNonNull(tree);

		final int size = tree.size();
		final MSeq<V> elements = MSeq.ofLength(size);
		final int[] childOffsets = new int[size];
		final int[] childCounts = new int[size];

		assert size >= 1;
		final FlatTree<V> root = new FlatTree<>(
			0,
			elements,
			childOffsets,
			childCounts
		);

		int childOffset = 1;
		int index = 0;
		final Iterator<? extends Tree<? extends V, ?>> it =
			tree.breadthFirstIterator();

		while (it.hasNext()) {
			final Tree<? extends V, ?> node = it.next();

			elements.set(index, node.getValue());
			childCounts[index] = node.childCount();
			childOffsets[index] = node.isLeaf() ? -1 : childOffset;

			childOffset += node.childCount();
			++index;
		}

		return root;
	}

}
