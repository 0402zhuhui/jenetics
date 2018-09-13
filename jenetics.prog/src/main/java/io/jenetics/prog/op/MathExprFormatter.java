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
package io.jenetics.prog.op;

import java.util.EnumMap;
import java.util.Map;

import io.jenetics.ext.util.Tree;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class MathExprFormatter {

	private static final Map<MathOp, String> INFIX_OPS = new EnumMap<>(MathOp.class);
	static {
		INFIX_OPS.put(MathOp.ADD, " + ");
		INFIX_OPS.put(MathOp.SUB, " - ");
		INFIX_OPS.put(MathOp.MUL, "*");
		INFIX_OPS.put(MathOp.DIV, "/");
		INFIX_OPS.put(MathOp.MOD, "%");
		INFIX_OPS.put(MathOp.POW, "^");
	}

	private static final Map<MathOp, Integer> PRECEDENCE = new EnumMap<>(MathOp.class);
	static {
		PRECEDENCE.put(MathOp.ADD, 6);
		PRECEDENCE.put(MathOp.SUB, 6);
		PRECEDENCE.put(MathOp.MUL, 5);
		PRECEDENCE.put(MathOp.DIV, 5);
		PRECEDENCE.put(MathOp.MOD, 5);
		PRECEDENCE.put(MathOp.POW, 4);
	}

	static String format(final Tree<? extends Op<Double>, ?> tree) {
		final StringBuilder out = new StringBuilder();
		format(tree, out);
		return out.toString();
	}

	private static void format(
		final Tree<? extends Op<Double>, ?> tree,
		final StringBuilder out
	) {
		final Op<Double> op = tree.getValue();
		if (INFIX_OPS.containsKey(op)) {
			infix(tree, out);
		} else {
			out.append(op);
			if (!tree.isLeaf()) {
				out.append("(");
				format(tree.getChild(0), out);
				for (int i = 1; i < tree.childCount(); ++i) {
					out.append(", ");
					format(tree.getChild(i), out);
				}
				out.append(")");
			}
		}
	}

	private static void infix(
		final Tree<? extends Op<Double>, ?> tree,
		final StringBuilder out
	) {
		assert tree.childCount() == 2;

		final Op<Double> op = tree.getValue();
		final Op<Double> pop = tree.getParent()
			.map(Tree::getValue)
			.orElse(null);

		final int precedence = PRECEDENCE.getOrDefault(tree.getValue(), 100);
		final int parentPrecedence = tree.getParent()
			.map(p -> PRECEDENCE.getOrDefault(p.getValue(), 100))
			.orElse(100);

		//final boolean brackets = !tree.isRoot() &&
		//	precedence > parentPrecedence /*&&
		//	tree.getParent().filter(p -> p.getValue() != MathOp.ADD).isPresent()*/;

		final boolean brackets = brackets(tree);

		if (brackets) out.append("(");
		format(tree.getChild(0), out);
		out.append(INFIX_OPS.get(op));
		format(tree.getChild(1), out);
		if (brackets) out.append(")");
	}

	private static boolean brackets(final Tree<? extends Op<Double>, ?> tree) {
		if (tree.isRoot()) {
			return false;
		}

		final Op<Double> op = tree.getValue();
		final Op<Double> pop = tree.getParent()
			.map(Tree::getValue)
			.orElse(null);

		final int precedence = PRECEDENCE.getOrDefault(tree.getValue(), 100);
		final int parentPrecedence = tree.getParent()
			.map(p -> PRECEDENCE.getOrDefault(p.getValue(), 100))
			.orElse(100);

		if (precedence == parentPrecedence) {
			if (pop == MathOp.SUB && op == MathOp.ADD) {
				return true;
			}
			if (op == MathOp.SUB && pop == MathOp.ADD) {
				return false;
			}
			if (pop == MathOp.DIV && op == MathOp.MUL) {
				return true;
			}
			if (op == MathOp.DIV && pop == MathOp.MUL) {
				return false;
			}
			return false;
		}
		if (precedence > parentPrecedence) {
			return true;
		}

		return false;
	}

}
