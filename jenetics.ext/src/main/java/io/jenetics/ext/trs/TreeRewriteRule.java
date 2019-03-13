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
package io.jenetics.ext.trs;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static io.jenetics.internal.util.Hashes.hash;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.jenetics.ext.trs.TreePattern.Var;

/**
 * Represents a tree rewrite rule. A rewrite rule consists of a match pattern,
 * which must be matched, and a substitution pattern, which is expanded and
 * replaces the variables in the pattern. Some simple <em>arithmetic</em>
 * rewrite rules.
 * <pre>
 *     add($x,0) -> $x
 *     mul($x,1) -> $x
 * </pre>
 * The <em>substitution</em> pattern may only use variables, already defined in
 * the <em>match</em> pattern. So, the creation of the following rewrite rule s
 * would lead to an {@link IllegalArgumentException}:
 * <pre>
 *     add($x,0) -> $y
 *     mul(0,1) -> mul($x,1)
 * </pre>
 *
 * @see RuleTreeRewriter
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public final class TreeRewriteRule<V> {

	private final TreePattern<V> _match;
	private final TreePattern<V> _replacement;

	/**
	 * Create a new rewrite rule from the given {@code match} and
	 * {@code replacement} pattern.
	 *
	 * @param match the matching pattern of the rule
	 * @param replacement the substitution pattern
	 * @throws NullPointerException if one of the arguments is {@code null}
	 * @throws IllegalArgumentException if the <em>template</em> pattern uses
	 *         variables not defined in the <em>matcher</em> pattern
	 */
	private TreeRewriteRule(
		final TreePattern<V> match,
		final TreePattern<V> replacement
	) {
		_match = requireNonNull(match);
		_replacement = requireNonNull(replacement);

		final Set<Var<V>> undefined = new HashSet<>(_replacement.vars());
		undefined.removeAll(_match.vars());
		if (!undefined.isEmpty()) {
			throw new IllegalArgumentException(format(
				"Some template variables are not defined in the matcher '%s': %s",
				this,
				undefined.stream()
					.map(v -> format("<%s>", v))
					.collect(Collectors.joining(", "))
			));
		}
	}

	/**
	 * Return the rule matching pattern.
	 *
	 * @return the rule matching pattern
	 */
	public TreePattern<V> match() {
		return _match;
	}

	/**
	 * Return the replacement pattern of the rule.
	 *
	 * @return the replacement pattern of the rule
	 */
	public TreePattern<V> replacement() {
		return _replacement;
	}

	@Override
	public int hashCode() {
		return hash(_match, hash(_replacement));
	}

	@Override
	public boolean equals(final Object obj) {
		return obj == this ||
			obj instanceof TreeRewriteRule &&
			_match.equals(((TreeRewriteRule)obj)._match) &&
			_replacement.equals(((TreeRewriteRule)obj)._replacement);
	}

	@Override
	public String toString() {
		return format("%s -> %s", _match, _replacement);
	}

	/**
	 * Create a new rewrite rule with the given values.
	 *
	 * @param match the matching pattern of the rule
	 * @param replacement the substitution pattern
	 * @return a new rewrite rule
	 * @throws NullPointerException if one of the arguments is {@code null}
	 * @throws IllegalArgumentException if the <em>match</em> pattern uses
	 *         variables not defined in the <em>replacement</em> pattern
	 */
	public static <V> TreeRewriteRule<V> of(
		final TreePattern<V> match,
		final TreePattern<V> replacement
	) {
		return new TreeRewriteRule<>(match, replacement);
	}

	/**
	 * Compiles the string representation of a rewrite rule:
	 * <pre>
	 *     add($x,0) -> $x
	 *     mul($x,1) -> $x
	 * </pre>
	 *
	 * @param rule the rewrite rule
	 * @return a new rewrite rule, compiled from the given rule string
	 * @throws IllegalArgumentException if the rewrite rule is invalid
	 * @throws NullPointerException if the given {@code rule} string is
	 *         {@code null}
	 */
	public static TreeRewriteRule compile(final String rule) {
		final String[] parts = rule.split("->");
		if (parts.length != 2) {
			throw new IllegalArgumentException(format(
				"Invalid rewrite rule: %s", rule
			));
		}

		return of(
			TreePattern.compile(parts[0]),
			TreePattern.compile(parts[1])
		);
	}

}
