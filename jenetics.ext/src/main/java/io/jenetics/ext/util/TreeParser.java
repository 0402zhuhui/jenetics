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

import static java.lang.String.format;
import static io.jenetics.ext.util.ParenthesesTrees.ESCAPE_CHAR;
import static io.jenetics.ext.util.ParenthesesTrees.unescape;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Parses an parentheses string into a {@code TreeNode<String>} object.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class TreeParser {
	private TreeParser() {}

	/**
	 * Represents a parentheses tree string token.
	 */
	final static class Token {
		private final String _seq;
		private final int _pos;

		Token(final String seq, final int pos) {
			_seq = seq;
			_pos = pos;
		}

		String seq() {
			return _seq;
		}

		int pos() {
			return _pos;
		}
	}

	/**
	 * Tokenize the given parentheses string.
	 *
	 * @param value the parentheses string
	 * @return the parentheses string tokens
	 * @throws NullPointerException if the given {@code value} is {@code null}
	 */
	static Deque<Token> tokenize(final String value) {
		final Deque<Token> tokens = new ArrayDeque<>();

		char pc = '\0';
		int pos = 0;
		final StringBuilder token = new StringBuilder();
		for (int i = 0; i < value.length(); ++i) {
			final char c = value.charAt(i);

			if (isTokenSeparator(c) && pc != ESCAPE_CHAR) {
				tokens.add(new Token(unescape(token.toString()), pos));
				tokens.add(new Token(Character.toString(c), i));
				token.setLength(0);
				pos = i;
			} else {
				token.append(c);
			}

			pc = c;
		}

		if (token.length() > 0) {
			tokens.add(new Token(unescape(token.toString()), pos));
		}

		return tokens;
	}

	private static boolean isTokenSeparator(final char c) {
		return c == '(' || c == ')' || c == ',';
	}

	/**
	 * Parses the given parentheses tree string
	 *
	 * @param value the parentheses tree string
	 * @return the parsed tree object
	 * @throws NullPointerException if the given {@code value} is {@code null}
	 * @throws IllegalArgumentException if the given parentheses tree string
	 *         doesn't represent a valid tree
	 */
	static TreeNode<String> parse(final String value) {
		final Deque<Token> tokens = tokenize(value);

		final TreeNode<String> root = TreeNode.of();
		final Deque<TreeNode<String>> parents = new ArrayDeque<>();

		TreeNode<String> current = root;
		for (Token token : tokens) {
			switch (token.seq()) {
				case "(":
					final TreeNode<String> tn1 = TreeNode.of();
					current.attach(tn1);
					parents.push(current);
					current = tn1;
					break;
				case ",":
					if (parents.isEmpty()) {
						throw new IllegalArgumentException(format(
							"Expect '(' at position %d.",
							token.pos()
						));
					}

					final TreeNode<String> tn2 = TreeNode.of();
					parents.peek().attach(tn2);
					current = tn2;
					break;
				case ")":
					if (parents.isEmpty()) {
						throw new IllegalArgumentException(format(
							"Unbalanced parentheses at position %d.",
							token.pos()
						));
					}
					current = parents.pop();
					if (parents.isEmpty()) {
						current = null;
					}
					break;
				default:
					if (current == null) {
						throw new IllegalArgumentException(format(
							"More than one root element at pos %d.",
							token.pos()
						));
					}
					if (current.getValue() == null) {
						current.setValue(token.seq());
					}
					break;
			}
		}

		if (!parents.isEmpty()) {
			throw new IllegalArgumentException("Unbalanced parentheses.");
		}

		return root;
	}

}
