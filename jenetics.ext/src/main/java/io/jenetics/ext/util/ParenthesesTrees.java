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
import static java.util.Objects.requireNonNull;
import static io.jenetics.internal.util.Hashes.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class ParenthesesTrees {
	private ParenthesesTrees() {}


	private static final char ESCAPE_CHAR = '\\';

	private static final char[] PROTECTED_CHARS = new char[] {
		'(', ')', ',', ESCAPE_CHAR
	};

	static String escape(final String value) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < value.length(); ++i) {
			final char c = value.charAt(i);
			if (isProtectedChar(c)) {
				result.append(ESCAPE_CHAR);
			}
			result.append(c);
		}

		return result.toString();
	}

	private static boolean isProtectedChar(final char c) {
		for (int i = 0; i < PROTECTED_CHARS.length; ++i) {
			if (c == PROTECTED_CHARS[i]) {
				return true;
			}
		}

		return false;
	}


	static String unescape(final String value) {
		final StringBuilder result = new StringBuilder();

		boolean escaping = false;
		for (int i = 0; i < value.length(); ++i) {
			final char c = value.charAt(i);

			if (c == ESCAPE_CHAR && !escaping) {
				escaping = true;
				continue;
			}

			if (escaping) {
				escaping = false;
			}
			result.append(c);
		}

		return result.toString();
	}

	/* *************************************************************************
	 * To string methods.
	 **************************************************************************/

	/**
	 * Return a compact string representation of the given tree.
	 * <pre>
	 *  mul(div(cos(1.0), cos(π)), sin(mul(1.0, z)))
	 * </pre>
	 *
	 * @param tree the input tree
	 * @return the string representation of the given tree
	 */
	static <V, T extends Tree<V, T>> String toString(
		final T tree,
		final Function<? super V, String> mapper
	) {
		requireNonNull(mapper);

		if (tree != null) {
			final StringBuilder out = new StringBuilder();
			toString(out, tree, mapper);
			return out.toString();
		} else {
			return "null";
		}
	}

	private static  <V, T extends Tree<V, T>> void toString(
		final StringBuilder out,
		final T tree,
		final Function<? super V, String> mapper
	) {
		out.append(escape(mapper.apply(tree.getValue())));
		if (!tree.isLeaf()) {
			out.append("(");
			toString(out, tree.getChild(0), mapper);
			for (int i = 1; i < tree.childCount(); ++i) {
				out.append(",");
				toString(out, tree.getChild(i), mapper);
			}
			out.append(")");
		}
	}


	/* *************************************************************************
	 * String parse methods.
	 **************************************************************************/

	private static final int ROOT_VALUE = 0;
	private static final int NEW_VALUE = 1;
	private static final int START_NODE = 2;
	private static final int END_NODE = 3;

	// mul(div(cos(1.0), cos(π)), sin(mul(1.0, z)))
	static TreeNode<String> parseParenthesesString(final String value) {
		final String[] tokens = tokenize(value);

		final TreeNode<String> root = TreeNode.of();
		TreeNode<String> node = root;



		int state = ROOT_VALUE;
		for (String token : tokens) {
			switch (token) {
				case "(":
					state = START_NODE;
					break;
				case ")":
					state = END_NODE;
					final TreeNode<String> fnode = node;
					if (!node.isRoot()) {
						node = node.getParent().orElseThrow(() -> {
							return new IllegalArgumentException("Unbalanced parentheses.");
						});
					} else {
						//throw new IllegalArgumentException("Unbalanced parentheses.");
					}
					break;
				case ",":
					state = NEW_VALUE;
					break;
				default:
					switch (state) {
						case START_NODE:
							node = node.lastChild().orElse(root);
						case NEW_VALUE:
							node.attach(token);
							break;
						case END_NODE:
							break;
						case ROOT_VALUE:
							node.setValue(token);
						default:
							break;
					}
					break;
			}
		}

		if (state != END_NODE) {
			throw new IllegalArgumentException("Unbalanced parentheses.");
		}

		return root;
	}

	static String[] tokenize(final String value) {
		final List<String> tokens = new ArrayList<>();

		final StringBuilder token = new StringBuilder();
		char pc = '\0';
		for (int i = 0; i < value.length(); ++i) {
			final char c = value.charAt(i);

			if (isTokenSeparator(c) && pc != ESCAPE_CHAR) {
				tokens.add(unescape(token.toString()));
				tokens.add(Character.toString(c));
				token.setLength(0);
			} else {
				token.append(c);
			}

			pc = c;
		}

		if (token.length() > 0) {
			tokens.add(unescape(token.toString()));
		}

		return tokens.toArray(new String[0]);
	}

	private static boolean isTokenSeparator(final char c) {
		return c == '(' || c == ')' || c == ',';
	}

}

final class Token {
	final String value;
	final int index;

	private Token(final String value, final int index) {
		this.value = value;
		this.index = index;
	}

	@Override
	public int hashCode() {
		return hash(value, hash(index));
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this ||
			obj instanceof Token &&
			Objects.equals(value, ((Token)obj).value) &&
			index == ((Token)obj).index;
	}

	@Override
	public String toString() {
		return format("%s[%d]", value, index);
	}

	static Token of(final String value, final int index) {
		return new Token(value, index);
	}

}

