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


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: NumberStatistic.java,v 1.7 2009-02-23 20:58:08 fwilhelm Exp $
 */
public class NumberStatistic<G extends Gene<?, G>, R extends Number & Comparable<R>>
	extends Statistic<G, R> 
{
	private static final long serialVersionUID = -7468324436514041360L;
	
	protected final double _fitnessMean;
	protected final double _fitnessVariance;
	
	protected NumberStatistic(
		final Phenotype<G, R> best, final Phenotype<G, R> worst, 
		final double fitnessMean, final double fitnessVariance,
		final int samples, final double ageMean, final double ageVariance
	) {
		super(best, worst, samples, ageMean, ageVariance);
		
		_fitnessMean = fitnessMean;
		_fitnessVariance = fitnessVariance;
	}
	
	protected NumberStatistic(
		final Statistic<G, R> other, 
		final double fitnessMean, final double fitnessVariance
	) {
		super(other);
		_fitnessMean = fitnessMean;
		_fitnessVariance = fitnessVariance;
	}

	public double getFitnessMean() {
		return _fitnessMean;
	}
	
	public double getFitnessVariance() {
		return _fitnessVariance;
	}

	@Override
	public String toString() {
		final StringBuilder out = new StringBuilder();

		out.append(super.toString() + "\n");
		out.append("Mean:            " + _fitnessMean + "\n");
		out.append("Variance:        " + _fitnessVariance);
		
		return out.toString();
	}
}



















