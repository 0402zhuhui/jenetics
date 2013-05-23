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
package org.jenetix;

import org.jscience.mathematics.number.LargeInteger;

import org.jenetics.Chromosome;
import org.jenetics.NumberChromosomeTester;
import org.jenetics.util.Factory;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date: 2013-05-23 $</em>
 */
public class LargeIntegerChromosomeTest
	extends NumberChromosomeTester<LargeInteger, LargeIntegerGene>
{

	private final LargeIntegerChromosome _factory = new LargeIntegerChromosome(
		LargeInteger.ZERO,
		LargeInteger.valueOf("99999999999999999999999999999999999999999999999"),
		500
	);

	@Override
	protected Factory<Chromosome<LargeIntegerGene>> getFactory() {
		return _factory;
	}

}
