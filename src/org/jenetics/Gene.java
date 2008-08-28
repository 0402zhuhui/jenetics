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

import java.io.Serializable;

import javolution.lang.ValueType;

/**
 * The <code>Gene</code> is the base of this genetic algorithm implementation.
 * A common interface for Genes.
 * 
 * @param <A> the <a href="http://en.wikipedia.org/wiki/Allele">Allele</a> type
 *            of this gene.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: Gene.java,v 1.2 2008-08-28 21:21:13 fwilhelm Exp $
 */
public interface Gene<A> extends Serializable, ValueType, Verifiable {
	
	/**
	 * Return the allel of this gene.
	 * 
	 * @return the allel of this gene.
	 */
	public A getAllele();
	
}
