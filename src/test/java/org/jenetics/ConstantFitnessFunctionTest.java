/*
 * Java Genetic Algorithm Library (@!identifier!@).
 * Copyright (c) @!year!@ Franz Wilhelmstötter
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *
 */
package org.jenetics;

import java.util.Random;

import org.jscience.mathematics.number.Float64;

import org.jenetics.util.Factory;
import org.jenetics.util.ObjectTester;
import org.jenetics.util.RandomRegistry;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class ConstantFitnessFunctionTest
	extends ObjectTester<ConstantFitnessFunction<Float64Gene, Float64>>
{

	final Factory<ConstantFitnessFunction<Float64Gene, Float64>>
	_factory = new Factory<ConstantFitnessFunction<Float64Gene,Float64>>() {
		@Override
		public ConstantFitnessFunction<Float64Gene, Float64> newInstance() {
			final Random random = RandomRegistry.getRandom();
			return new ConstantFitnessFunction<>(
					Float64.valueOf(random.nextDouble()*Double.MAX_VALUE)
				);
		}
	};
	@Override
	protected Factory<ConstantFitnessFunction<Float64Gene, Float64>> getFactory() {
		return _factory;
	}
	
}
