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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javolution.context.ConcurrentContext;
import jsr166y.ForkJoinPool;

import org.jenetics.util.ConcurrentEvaluator;
import org.jenetics.util.Evaluator;
import org.jenetics.util.EvaluatorRegistry;
import org.jenetics.util.Factory;
import org.jenetics.util.ForkJoinEvaluator;
import org.jenetics.util.RandomRegistry;
import org.jenetics.util.SerialEvaluator;
import org.jenetics.util.ThreadedEvaluator;
import org.jscience.mathematics.number.Float64;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class GeneticAlgorithmTest {

	private static class FF implements FitnessFunction<Float64Gene, Float64> {
		private static final long serialVersionUID = 618089611921083000L;

		@Override
		public Float64 evaluate(final Genotype<Float64Gene> genotype) {
			return genotype.getGene().getAllele();
		}
	}
	
	@Test
	public void setGetAlterer() {
		final GeneticAlgorithm<Float64Gene, Float64> ga = 
			new GeneticAlgorithm<Float64Gene, Float64>(
					Genotype.valueOf(new Float64Chromosome(0, 1)), 
					new FF()
				);
		
		final Alterer<Float64Gene> alterer = new Mutator<Float64Gene>();
		ga.setAlterer(alterer);
		Assert.assertSame(ga.getAlterer(), alterer);
		
		ga.addAlterer(new MeanAlterer<Float64Gene>());
		Assert.assertNotSame(ga.getAlterer(), alterer);
		Assert.assertTrue(ga.getAlterer() instanceof CompositeAlterer<?>);
		Assert.assertEquals(((CompositeAlterer<?>)ga.getAlterer()).getAlterers().length(), 2);
		
		ga.addAlterer(new SwapMutator<Float64Gene>());
		Assert.assertTrue(ga.getAlterer() instanceof CompositeAlterer<?>);
		Assert.assertEquals(((CompositeAlterer<?>)ga.getAlterer()).getAlterers().length(), 3);
	}
	
	@Test
	public void optimize() {
		RandomRegistry.setRandom(new Random(12345));
		EvaluatorRegistry.setEvaluator(new SerialEvaluator());
		
		final int concurrency = ConcurrentContext.getConcurrency();
		ConcurrentContext.setConcurrency(0);
		try {
			final Factory<Genotype<Float64Gene>> factory = Genotype.valueOf(new Float64Chromosome(0, 1));
			final FitnessFunction<Float64Gene, Float64> ff = new FF();
			
			final GeneticAlgorithm<Float64Gene, Float64> ga = GeneticAlgorithm.valueOf(factory, ff);
			ga.setPopulationSize(200);
			ga.setAlterer(new MeanAlterer<Float64Gene>());
			ga.setOffspringFraction(0.3);
			ga.setOffspringSelector(new RouletteWheelSelector<Float64Gene, Float64>());
			ga.setSurvivorSelector(new TournamentSelector<Float64Gene, Float64>());
			
			ga.setup();
			for (int i = 0; i < 100; ++i) {
				ga.evolve();
			}
			
			Statistics<?, ?> s = ga.getBestStatistics();
			Reporter.log(s.toString());
			Assert.assertEquals(s.getAgeMean(), 0.0);
			Assert.assertEquals(s.getAgeVariance(), 0.0);
			Assert.assertEquals(s.getSamples(), 200);
			Assert.assertEquals(((Float64)s.getBestFitness()).doubleValue(), 0.99577149482586247, 0.00000001);
			Assert.assertEquals(((Float64)s.getWorstFitness()).doubleValue(), 0.0014983949586988565, 0.00000001);
			
			s = ga.getStatistics();
			Reporter.log(s.toString());
			Assert.assertEquals(s.getAgeMean(), 21.35000000000, 0.000001);
			Assert.assertEquals(s.getAgeVariance(), 207.90703517588, 0.000001);
			Assert.assertEquals(s.getSamples(), 200);
			Assert.assertEquals(((Float64)s.getBestFitness()).doubleValue(), 0.99577149482586247, 0.00000001);
			Assert.assertEquals(((Float64)s.getWorstFitness()).doubleValue(), 0.13094479012932092, 0.00000001);
		} finally {
			ConcurrentContext.setConcurrency(concurrency);
		}
		
	}
	
	@Test(invocationCount = 10)
	public void evolveForkJoinPool() {
		Evaluator evaluator = EvaluatorRegistry.getEvaluator();
		final ForkJoinPool pool = new ForkJoinPool(10);
		EvaluatorRegistry.setEvaluator(new ForkJoinEvaluator(pool));
		
		try {
			final Factory<Genotype<Float64Gene>> factory = Genotype.valueOf(new Float64Chromosome(0, 1));
			final FitnessFunction<Float64Gene, Float64> ff = new FF();
			
			final GeneticAlgorithm<Float64Gene, Float64> ga = GeneticAlgorithm.valueOf(factory, ff);
			ga.setPopulationSize(1000);
			ga.setAlterer(new MeanAlterer<Float64Gene>());
			ga.setOffspringFraction(0.3);
			ga.setOffspringSelector(new RouletteWheelSelector<Float64Gene, Float64>());
			ga.setSurvivorSelector(new StochasticUniversalSelector<Float64Gene, Float64>());
			
			ga.setup();
			for (int i = 0; i < 10; ++i) {
				ga.evolve();
			}
		} finally {
			EvaluatorRegistry.setEvaluator(evaluator);
			pool.shutdown();
		}
	}
	
	@Test(invocationCount = 10)
	public void evolveThreadPool() {
		Evaluator evaluator = EvaluatorRegistry.getEvaluator();
		final ExecutorService pool = Executors.newFixedThreadPool(10);
		EvaluatorRegistry.setEvaluator(new ThreadedEvaluator(pool));
		
		try {
			final Factory<Genotype<Float64Gene>> factory = Genotype.valueOf(new Float64Chromosome(0, 1));
			final FitnessFunction<Float64Gene, Float64> ff = new FF();
			
			final GeneticAlgorithm<Float64Gene, Float64> ga = GeneticAlgorithm.valueOf(factory, ff);
			ga.setPopulationSize(1000);
			ga.setAlterer(new MeanAlterer<Float64Gene>());
			ga.setOffspringFraction(0.3);
			ga.setOffspringSelector(new BoltzmannSelector<Float64Gene, Float64>(0.001));
			ga.setSurvivorSelector(new ExponentialRankSelector<Float64Gene, Float64>(0.675));
			
			ga.setup();
			for (int i = 0; i < 10; ++i) {
				ga.evolve();
			}
		} finally {
			EvaluatorRegistry.setEvaluator(evaluator);
			pool.shutdown();
		}
	}
	
	@Test(invocationCount = 10)
	public void evolveConcurrent() {
		Evaluator evaluator = EvaluatorRegistry.getEvaluator();
		EvaluatorRegistry.setEvaluator(new ConcurrentEvaluator(10));
		
		try {
			final Factory<Genotype<Float64Gene>> factory = Genotype.valueOf(new Float64Chromosome(0, 1));
			final FitnessFunction<Float64Gene, Float64> ff = new FF();
			
			final GeneticAlgorithm<Float64Gene, Float64> ga = GeneticAlgorithm.valueOf(factory, ff);
			ga.setPopulationSize(1000);
			ga.setAlterer(new MeanAlterer<Float64Gene>());
			ga.setOffspringFraction(0.3);
			ga.setOffspringSelector(new RouletteWheelSelector<Float64Gene, Float64>());
			ga.setSurvivorSelector(new LinearRankSelector<Float64Gene, Float64>());
			
			ga.setup();
			for (int i = 0; i < 10; ++i) {
				ga.evolve();
			}
		} finally {
			EvaluatorRegistry.setEvaluator(evaluator);
		}
	}
	
}




