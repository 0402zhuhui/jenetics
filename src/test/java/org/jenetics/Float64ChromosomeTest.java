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

import static org.jenetics.stat.StatisticsAssert.assertDistribution;
import static org.jenetics.util.Accumulators.accumulate;
import static org.testng.Assert.assertEquals;

import java.util.Random;

import javolution.context.LocalContext;

import org.jscience.mathematics.number.Float64;
import org.testng.Assert;
import org.testng.annotations.Test;

import org.jenetics.stat.Histogram;
import org.jenetics.stat.UniformDistribution;
import org.jenetics.stat.Variance;
import org.jenetics.util.Accumulators.MinMax;
import org.jenetics.util.Factory;
import org.jenetics.util.RandomRegistry;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class Float64ChromosomeTest extends ObjectTester<Chromosome<Float64Gene>> { 
    
	private final Factory<Chromosome<Float64Gene>> 
	_factory = new Float64Chromosome(0, Double.MAX_VALUE, 500);
	@Override protected Factory<Chromosome<Float64Gene>> getFactory() {
		return _factory;
	}

	@Test(invocationCount = 20, successPercentage = 95)
    public void newInstance() {
		LocalContext.enter();
		try {
			RandomRegistry.setRandom(new Random());
			
			final Float64 min = Float64.ZERO;
			final Float64 max = Float64.valueOf(100);
			
			
			final MinMax<Float64> mm = new MinMax<Float64>();
			final Histogram<Float64> histogram = Histogram.valueOf(min, max, 10);
			final Variance<Float64> variance = new Variance<Float64>();
			
			for (int i = 0; i < 1000; ++i) {
				final Float64Chromosome chromosome = new Float64Chromosome(min, max, 500);
				
				accumulate(
						chromosome, 
						mm.adapt(Float64Gene.Value),
						histogram.adapt(Float64Gene.Value),
						variance.adapt(Float64Gene.Value)
					);
			}
			
			Assert.assertTrue(mm.getMin().compareTo(0) >= 0);
			Assert.assertTrue(mm.getMax().compareTo(100) <= 100);
			
			// Chi-Square teset for gene distribution.
			// http://de.wikibooks.org/wiki/Mathematik:_Statistik:_Tabelle_der_Chi-Quadrat-Verteilung
			assertDistribution(histogram, new UniformDistribution<Float64>(min, max));
		} finally {
			LocalContext.exit();
		}
    }

    @Test
    public void createChromosome() {
        final Float64Chromosome c1 = new Float64Chromosome(-12.0, 230.123, 100);
        final Float64Chromosome c2 = c1.newInstance();
        
        for (NumberGene<Float64, ?> g : c2) {
            assertEquals(-12.0, g.getMin().doubleValue());
            assertEquals(230.123, g.getMax().doubleValue());
        }
    }

}
