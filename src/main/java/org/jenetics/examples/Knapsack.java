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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 * 	 Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 * 	 
 */
package org.jenetics.examples;

import java.io.Serializable;

import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;
import org.jenetics.CompositeAlterer;
import org.jenetics.FitnessFunction;
import org.jenetics.GeneticAlgorithm;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.NumberStatistics;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.SinglePointCrossover;
import org.jenetics.util.ConcurrentEvaluator;
import org.jenetics.util.EvaluatorRegistry;
import org.jenetics.util.Factory;
import org.jscience.mathematics.number.Float64;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	public double size;
	 public double value;
}

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
class KnappsackFunction implements FitnessFunction<BitGene, Float64> {
	private static final long serialVersionUID = -924756568100918419L;
	
	private final Item[] _items;
	 private final double _knapsackSize;
	 
	 public KnappsackFunction(final Item[] items, double knapsackSize) {
		  this._items = items;
		  this._knapsackSize = knapsackSize;
	 }
	 
	 public Item[] getItems() {
		return _items;
	 }
	 
	 @Override
	 public Float64 evaluate(final Genotype<BitGene> genotype) {
		  final Chromosome<BitGene> ch = genotype.getChromosome();
		  
		  double size = 0;
		  double value = 0;
		  for (int i = 0, n = ch.length(); i < n; ++i) {
				if (ch.getGene(i).getBit()) {
					 size += _items[i].size;
					 value += _items[i].value;
				}
		  }
		  
		  if (size > _knapsackSize) {
				return Float64.ZERO;
		  } else {
				return Float64.valueOf(value);
		  }
	 }
}

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class Knapsack {
	
	private static KnappsackFunction newFitnessFuntion(int n, double knapsackSize) {
		Item[] items = new Item[n];
		for (int i = 0; i < items.length; ++i) {
				items[i] = new Item();
				items[i].size = (Math.random() + 1)*10;
				items[i].value = (Math.random() + 1)*15;
		}
		  
		return new KnappsackFunction(items, knapsackSize);
	}
	
	public static void main(String[] argv) throws Exception {
		EvaluatorRegistry.setEvaluator(new ConcurrentEvaluator());
		
		//Defining the fitness function and the genotype.
		final KnappsackFunction ff = newFitnessFuntion(15, 100);
		final Factory<Genotype<BitGene>> genotype = Genotype.valueOf(
				BitChromosome.valueOf(15, 0.5)
			);
		 
		final GeneticAlgorithm<BitGene, Float64> ga = GeneticAlgorithm.valueOf(genotype, ff);
		ga.setMaximalPhenotypeAge(30);
		ga.setPopulationSize(1000);
		ga.setStatisticsCalculator(new NumberStatistics.Calculator<BitGene, Float64>());
		ga.setSelectors(new RouletteWheelSelector<BitGene, Float64>());
		ga.setAlterer(new CompositeAlterer<BitGene>(
				 new Mutator<BitGene>(0.115),
				 new SinglePointCrossover<BitGene>(0.16)
		 	));

		ga.setup();
		ga.evolve(999);
//		System.out.println(ga);
		System.out.println(ga.getTimeStatistics());
		System.out.println(ga.getBestStatistics());
	 }
}



