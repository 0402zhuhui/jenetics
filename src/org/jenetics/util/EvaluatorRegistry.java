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
package org.jenetics.util;

import java.util.List;

import javolution.context.LocalContext;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: EvaluatorRegistry.java,v 1.1 2008-11-13 20:37:41 fwilhelm Exp $
 */
public class EvaluatorRegistry {
	private static final LocalContext.Reference<Evaluator> EVALUATOR = 
		new LocalContext.Reference<Evaluator>(new SerialEvaluator());

	
	private EvaluatorRegistry() {
	}
	
	/**
	 * Return the currently registered evaluator.
	 * 
	 * @return teh currently registered evaluator.
	 */
	public static Evaluator getEvaluator() {
		return EVALUATOR.get();
	}
	
	/**
	 * Set the evaluator to use.
	 * 
	 * @param evaluator set the evaluator to use.
	 */
	public static void setEvaluator(final Evaluator evaluator) {
		Validator.notNull(evaluator, "Evaluator");
		EVALUATOR.set(evaluator);
	}
	
	/**
	 * Evaluates the given task list with the currently registered evaluator.
	 * 
	 * @param tasks the tasks to evaluate.
	 */
	public static void evaluate(final List<? extends Runnable> tasks) {
		Validator.notNull(tasks, "Task list");
		EVALUATOR.get().evaluate(tasks);
	}
	
}
