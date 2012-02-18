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

import org.jscience.mathematics.number.Float64;

import org.jenetics.stat.Distribution;
import org.jenetics.stat.LinearDistribution;
import org.jenetics.util.Factory;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class LinearRankSelectorTest	
	extends ProbabilitySelectorTester<LinearRankSelector<Float64Gene, Float64>>
{
	
	@Override
	protected boolean isSorted() {
		return true;
	}
	
	@Override
	protected Factory<LinearRankSelector<Float64Gene, Float64>> getFactory() {
		return SelectorFactories.LinearRankSelector;
	}
	
	@Override
	protected Distribution<Float64> getDistribution() {
		return new LinearDistribution<>(_domain, 0);
	}
	
	@Override
	protected LinearRankSelector<Float64Gene, Float64> getSelector() {
		return new LinearRankSelector<>(0.0);
	}
	
	
}




