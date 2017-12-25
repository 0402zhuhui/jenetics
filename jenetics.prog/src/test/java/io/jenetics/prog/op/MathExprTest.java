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

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.*;

import java.util.Random;
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
public class MathExprTest {
	/*
	{
		signum(abs(min((max((log10(sqr(y))*min(asin(y), cos(y))), asin(asin(min(1.0, x))))%((sin(rint(w))/(max(y, x)%sinh(1.0)))^(tanh(asin(y))%((z + u) - sin(y))))), sqr(sqr(abs(max(tan(x), cosh(v))))))));
	}*/

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
	public void parse() {
		//final MathExpr expr = MathExpr.parse("x*x + sin(z) - cos(x)*y*pow(z*x + y, pow(pow(z*x + y, pow(z*x + y, x)), x))");
		//System.out.println(expr);
		//System.out.println(expr.tree());
	}

	@Test(dataProvider = "functionData")
	public void eval(final String expression, final F3 f, final double[] x) {
		Assert.assertEquals(
			MathExpr.eval(expression, x),
			f.apply(x[0], x[1], x[2])
		);

		Assert.assertEquals(
			MathExpr.eval(MathExpr.parse(expression).toString(), x),
			f.apply(x[0], x[1], x[2])
		);
	}

	@DataProvider(name = "functionData")
	public Object[][] functionData() {
		return new Object[][] {
			{"x*x + sin(z) - cos(x)*y*pow(z*x + y, x)", (F3)MathExprTest::f1, new double[]{1, 2, 3}},
			{"x*x + sin(z) - cos(x)*y*pow(z*x + y, x)", (F3)MathExprTest::f1, new double[]{1.5, 65, 13}},
			{"x*x + sin(z) - cos(x)*y*pow(z*x + y, x)", (F3)MathExprTest::f1, new double[]{10, 0, 53}},
			{"x*x + sin(z) - cos(x)*y*pow(z*x + y, x)", (F3)MathExprTest::f1, new double[]{11, 23, 39}},

			{"x*x + sin(z) - cos(x)*y*pow(z*x + y, pow(z*x + y, x))", (F3)MathExprTest::f2, new double[]{1.5, 2.6, 3.9}},
			{"x*x + sin(z) - cos(x)*y*pow(z*x + y, pow(pow(z*x + y, pow(z*x + y, x)), x))", (F3)MathExprTest::f3, new double[]{1.5, 2.6, 3.9}}
		};
	}

	private static double f1(final double x, final double y, final double z) {
		return x*x + sin(z) - cos(x)*y*pow(z*x + y, x);
	}

	private static double f2(final double x, final double y, final double z) {
		return x*x + sin(z) - cos(x)*y*pow(z*x + y, pow(z*x + y, x));
	}

	private static double f3(final double x, final double y, final double z) {
		return x*x + sin(z) - cos(x)*y*pow(z*x + y, pow(pow(z*x + y, pow(z*x + y, x)), x));
	}

	static interface F3 {
		double apply(final double x, final double y, final double z);
	}

	@Test(dataProvider = "ast")
	public void toStringAndParse(final Tree<? extends Op<Double>, ?> tree) {
		final String expression = new MathExpr(tree).toString();
		final MathExpr expr = MathExpr.parse(expression);

		Assert.assertEquals(expr.tree(), tree);
		Assert.assertEquals(expr.toString(), expression);
	}

	@DataProvider(name = "ast")
	public Object[][] ast() {
		return Stream.generate(() -> Program.of(9, OPERATIONS, TERMINALS, new Random(125)))
			.limit(100)
			.map(p -> new Object[]{p})
			.toArray(Object[][]::new);
	}


	@Test
	public void simplify() {
		final MathExpr expr = MathExpr.parse("4+4+x*(5 + 13)");

		System.out.println(expr.tree());
		final TreeNode<Op<Double>> simple = MathExpr.simplify((TreeNode<Op<Double>>)expr.tree());
		System.out.println(simple);
	}

}
