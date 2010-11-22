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
package org.jenetics.stat;

import org.jenetics.stat.Distribution.Domain;
import org.jscience.mathematics.function.Function;
import org.jscience.mathematics.number.Float64;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class NormalDistributionTest {

	@Test
	public void pdf() {
		final Domain<Double> domain = new Domain<Double>(0.0, 100.0);
		final Distribution<Double> dist = new NormalDistribution<Double>(domain, 50.0, 34.0);
		final Function<Double, Float64> pdf = dist.pdf();
		
		Assert.assertEquals(pdf.toString(), "p(x) = N[µ=50.000000, σ²=34.000000](x)");
	}
	
	@Test
	public void cdf() {
		final Domain<Double> domain = new Domain<Double>(0.0, 100.0);
		final Distribution<Double> dist = new NormalDistribution<Double>(domain, 50.0, 34.0);
		final Function<Double, Float64> cdf = dist.cdf();
		
		Assert.assertEquals(cdf.toString(), "P(x) = 1/2(1 + erf((x - 50.000000)/(sqrt(2·34.000000))))");
	}
	
}
