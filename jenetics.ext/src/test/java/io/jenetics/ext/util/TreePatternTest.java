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

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class TreePatternTest {

	@Test
	public void foo() {
		final Tree<?, ?> tree = TreeNode.parse("add(3,mul(sub(1, 1),6))");
		final Tree_Pattern pattern = Tree_Pattern.compile("mul(<x>,6)");

		System.out.println(pattern.matcher(tree).matches());
		pattern.matcher(tree).results().forEach(System.out::println);

		System.out.println(Tree_Matcher.matches(tree, pattern.tree()));
	}

	@Test
	public void expand() {
		final TreePattern pattern = TreePattern.compile("mul(<x>,6)");
		final Map<String, Tree<String, ?>> variables = new HashMap<>();
		variables.put("x", TreeNode.parse("sin(4,5)"));

		final Tree<String, ?> expanded = pattern.expand(variables);
		System.out.println(expanded.toParenthesesString());
	}

}
