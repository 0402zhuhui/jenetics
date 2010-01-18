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
package org.jenetics.examples;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sin;
import jsr166y.ForkJoinPool;

import org.jenetics.Chromosome;
import org.jenetics.FitnessFunction;
import org.jenetics.GeneticAlgorithm;
import org.jenetics.Genotype;
import org.jenetics.IntegerGene;
import org.jenetics.PartiallyMatchedCrossover;
import org.jenetics.PermutationChromosome;
import org.jenetics.SwapMutation;
import org.jenetics.util.EvaluatorRegistry;
import org.jenetics.util.Factory;
import org.jenetics.util.ForkJoinEvaluator;
import org.jenetics.util.Probability;
import org.jscience.mathematics.number.Float64;

/**
 * The classical <a href="http://en.wikipedia.org/wiki/Travelling_salesman_problem">TSP</a>.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: TravelingSalesman.java,v 1.26 2010-01-18 16:21:58 fwilhelm Exp $
 */
public class TravelingSalesman {
	
	private static class Function implements FitnessFunction<IntegerGene, Double> {
		private static final long serialVersionUID = 8402072476064049463L;
		
		private final double[][] _adjacence;
		private final double _perimeter;
		
		public Function(final double[][] adjacence) {
			_adjacence = adjacence;
			_perimeter = _adjacence[0][1]*_adjacence.length;
		}
		
		@Override
		public Double evaluate(final Genotype<IntegerGene> genotype) {
			final Chromosome<IntegerGene> path = genotype.getChromosome();
			
			double length = 0.0;
			for (int i = 0, n = path.length(); i < n; ++i) {
				final int from = path.getGene(i).intValue();
				final int to = path.getGene((i + 1)%n).intValue();
				length += _adjacence[from][to];
			}
			return _perimeter/length;
		}
	}
	
	public static void main(String[] args) {
		final int stops = 20;
		
		final FitnessFunction<IntegerGene, Double> ff = new Function(adjacencyMatrix(stops));
		final Factory<Genotype<IntegerGene>> gtf = Genotype.valueOf(
			new PermutationChromosome(stops)
		);
		final GeneticAlgorithm<IntegerGene, Double> ga = GeneticAlgorithm.valueOf(gtf, ff);
		ga.setPopulationSize(200);
        ga.setAlterer(
            new SwapMutation<IntegerGene>(Probability.valueOf(0.2), 
            new PartiallyMatchedCrossover<IntegerGene>(Probability.valueOf(0.3)))
        );
        //ga.setSelectors(new org.jenetics.MonteCarloSelector<IntegerGene, Double>());
        
//        ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        EvaluatorRegistry.setEvaluator(new ThreadedEvaluator(pool));
        
        ForkJoinPool pool = new ForkJoinPool();
        EvaluatorRegistry.setEvaluator(new ForkJoinEvaluator(pool));
        try {
        	GAUtils.execute(ga, 1000);
        } finally {
        	pool.shutdown();
        }
        
//		try {
//			XMLSerializer.write(ga.getPopulation(), new FileOutputStream("/home/franzw/population.xml"));
//			ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream("/home/franzw/population.obj"));
//			oout.writeObject(ga.getPopulation());
//			oout.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
	}
	
	/**
	 * All points in the created adjacency matrix lie on a circle. So it is easy 
	 * to check the quality of the solution found by the GA.
	 */
	private static double[][] adjacencyMatrix(int stops) {
		double[][] matrix = new double[stops][stops];
		for (int i = 0; i < stops; ++i) {
			for (int j = 0; j < stops; ++j) {
				matrix[i][j] = chord(stops, abs(i - j), RADIUS);
			}
		}
		return matrix;
	}
	private static double chord(int stops, int i, double r) {
		return 2.0*r*abs(sin((PI*i)/stops));
	}
	private static double RADIUS = 10.0;
}





