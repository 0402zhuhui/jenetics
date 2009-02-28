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

import org.jenetics.GeneticAlgorithm;
import org.jenetics.Statistics;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: GAUtils.java,v 1.11 2009-02-28 14:53:10 fwilhelm Exp $
 */
public class GAUtils {

	private GAUtils() {
	}
	
	public static void execute(final GeneticAlgorithm<?, ?> ga, int generations) {
		ga.setup();	
		System.out.println(ga);
		for (int i = 1; i < generations; ++i) {
			ga.evolve();
			System.out.println(ga);
		}
		
		final Statistics.Time times = ga.getTimeStatistics();
		
		System.out.println();
		System.out.println("               Select time: " + times.getSelectionTime());
		System.out.println("                Alter time: " + times.getAlterTime());
		System.out.println("  Fitness calculation time: " + times.getEvaluationTime());
		System.out.println("Statistic calculation time: " + times.getStatisticsTime());
		System.out.println("----------------------------");
		System.out.println("    Overall execution time: " + times.getExecutionTime());
		System.out.println();
		System.out.println(ga.getBestStatistic());
	}
	
}
