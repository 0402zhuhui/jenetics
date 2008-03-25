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

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

/**
 * An alternative to the "weak" <code>LinearRankSelector</code> is to assign
 * survivial probabilities to the sorted individuals using an exponential 
 * function:
 * <p/>
 * <pre>
 *          N-i
 *         c
 *  p_i = -------,
 *           N
 * </pre>
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: ExponentialRankSelector.java,v 1.1 2008-03-25 18:31:55 fwilhelm Exp $
 */
public class ExponentialRankSelector<T extends Gene<?>> 
	extends ProbabilitySelector<T> implements XMLSerializable
{
	private static final long serialVersionUID = -5633748296591142197L;
	
	private final double _c;
	
	public ExponentialRankSelector(final double c) {
		this._c = c;
	}

	@Override
	protected Probability[] probabilities(final Population<T> population, final int count) {
		assert(population != null) : "Population can not be null. ";
		assert(count >= 0) : "Population to select must be greater than zero. ";
		
		final double N = population.size();
		Probability[] props = new Probability[population.size()];
		
		double p = 0;
		for (int i = 0, n = population.size(); i < N; ++i) {
			p = ((_c - 1)*Math.pow(_c, N - i - 1))/(Math.pow(_c, N) - 1);
			props[n - i - 1] = Probability.valueOf(p); 
		}
	
		return props;
	}
	
	@SuppressWarnings("unchecked")
	static final XMLFormat<ExponentialRankSelector> 
	XML = new XMLFormat<ExponentialRankSelector>(ExponentialRankSelector.class) {
		@Override
		public ExponentialRankSelector newInstance(
			final Class<ExponentialRankSelector> cls, final InputElement xml
		)  throws XMLStreamException {
			final double beta = xml.getAttribute("c", 1.0);
			return new ExponentialRankSelector(beta);
		}
		@Override
		public void write(final ExponentialRankSelector s, final OutputElement xml) 
			throws XMLStreamException 
		{
			xml.setAttribute("c", s._c);
		}
		@Override
		public void read(final InputElement xml, final ExponentialRankSelector s) 
			throws XMLStreamException 
		{
		}
	};

}



