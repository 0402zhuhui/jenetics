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

import java.util.stream.Stream;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.util.ISeq;

import io.jenetics.ext.util.Tree;
import io.jenetics.ext.util.TreeNode;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class MathOpExprTest {

	static final ISeq<Op<Double>> OPERATIONS = ISeq.of(MathOp.values());

	static final ISeq<Op<Double>> TERMINALS = ISeq.of(
		Var.of("u", 0),
		Var.of("v", 1),
		Var.of("w", 2),
		Var.of("x", 3),
		Var.of("y", 4),
		Var.of("z", 5),
		Const.of(1.0)
	);

	@Test
	public void eval() {
		final String expr = "5.0 + 6.0*x + sin(x)^34.0 + (1.0 + sin(x*5.0)/4.0) + 6.5";
		final MathOpExpr tree = MathOpExpr.parse(expr);
		assert tree.toString().equals(expr);

		final MathOpExpr e = MathOpExpr.parse("2*z + 3*x - y");
		final double result = MathOpExpr.eval("2*z + 3*x - y", 3, 2, 1);
		assert result == 9.0;
		System.out.println(result);

		final double e2 = MathOpExpr.parse("5 + 6*x + sin(x)^34 + (1 + sin(x*5)/4)/6").eval(4.32);
		assert e2 == 31.170600453465315;
		System.out.println(e2);

		final TreeNode<Op<Double>> tree1 = Program.parse("5 + 6*x + sin(x)^34 + (1 + sin(x*5)/4)/6");
		System.out.println(tree1);
	}

	@Test(dataProvider = "ast")
	public void toStringAndParse(final Tree<? extends Op<Double>, ?> tree) {
		final String expression = new MathOpExpr(tree).toString();
		final MathOpExpr expr = MathOpExpr.parse(expression);
		Assert.assertEquals(expr.toString(), expression);
		//Assert.assertEquals(expr.tree(), tree);
	}

	@DataProvider(name = "ast")
	public Object[][] ast() {
		return Stream.generate(() -> Program.of(4, OPERATIONS, TERMINALS))
			.limit(100)
			.map(p -> new Object[]{p})
			.toArray(Object[][]::new);
	}

}
