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

import javolution.context.ObjectFactory;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.jenetics.util.RandomRegistry;
import org.jscience.mathematics.number.Float64;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
public class Float64Gene extends NumberGene<Float64, Float64Gene> {
	private static final long serialVersionUID = 2531451920309748752L;	
	
	protected Float64Gene() {
	}

	/**
	 * Create a new, <em>random</em> gene.
	 */
	@Override
	public Float64Gene newInstance() {
		final Random random = RandomRegistry.getRandom();
		final double difference = _max.doubleValue() - _min.doubleValue();
		final double value = random.nextDouble()*difference + _min.doubleValue();
		
		return newInstance(value);
	}
	
	@Override
	public Float64Gene newInstance(final java.lang.Number number) {
		return valueOf(Float64.valueOf(number.doubleValue()), _min, _max);
	}
	
	/**
	 * Create a new gene from the given {@code number}.
	 * 
	 * @param number the value of the new gene.
	 * @return a new gene with the given value.
	 */
	public Float64Gene newInstance(final Float64 number) {
		return valueOf(number, _min, _max);
	}
	
	/**
	 * Create a new DoubleGene with the same limits and the given value.
	 * 
	 * @param number The value of the new NumberGene.
	 * @return The new NumberGene.
	 */
	public Float64Gene newInstance(final double number) {
		return newInstance(Float64.valueOf(number));
	}
	
	@Override
	public Float64Gene mean(final Float64Gene that) {
		return newInstance(
				(_value.doubleValue() + that._value.doubleValue())/2.0
			);
	}
	
	private static final ObjectFactory<Float64Gene> 
	FACTORY = new ObjectFactory<Float64Gene>() {
		@Override protected Float64Gene create() {
			return new Float64Gene();
		}
	};
	
	/**
	 * Create a new random DoubleGene with the given value and the given range. 
	 * If the {@code value} isn't within the closed interval [min, max], no 
	 * exception is thrown. In this case the method {@link Float64Gene#isValid()} 
	 * returns {@code false}.
	 * 
	 * @param value the value of the DoubleGene.
	 * @param min the minimal valid value of this DoubleGene.
	 * @param max the maximal valid value of this DoubleGene.
	 * @return the new created DoubleGene with the given {@code value}.
	 */
	public static Float64Gene valueOf(
		final Float64 value, final Float64 min, final Float64 max
	) {
		Float64Gene gene = FACTORY.object();
		gene.set(value, min, max);
		return gene;
	}
	
	/**
	 * Create a new random DoubleGene with the given value and the given range. 
	 * If the {@code value} isn't within the closed interval [min, max], no 
	 * exception is thrown. In this case the method {@link Float64Gene#isValid()} 
	 * returns {@code false}.
	 * 
	 * @param value the value of the DoubleGene.
	 * @param min the minimal valid value of this DoubleGene.
	 * @param max the maximal valid value of this DoubleGene.
	 * @return the new created DoubleGene with the given {@code value}.
	 */
	public static Float64Gene valueOf(
		final double value, final double min, final double max
	) {
		return valueOf(
			Float64.valueOf(value),
			Float64.valueOf(min), 
			Float64.valueOf(max)
		);
	}
	
	/**
	 * Create a new random DoubleGene. It is guaranteed that the value of the 
	 * DoubleGene lies in the closed interval [min, max].
	 * 
	 * @param min the minimal value of the DoubleGene to create.
	 * @param max the maximal value of the DoubleGene to create.
	 * @return the new created DoubleGene.
	 */
	public static Float64Gene valueOf(final Float64 min, final Float64 max) {
		final Random random = RandomRegistry.getRandom();
		final double value = min.doubleValue() + 
							random.nextDouble()*(max.doubleValue() - 
							min.doubleValue());
		
		return valueOf(Float64.valueOf(value), min, max);
	}
	
	/**
	 * Create a new random DoubleGene. It is guaranteed that the value of the 
	 * DoubleGene lies in the closed interval [min, max].
	 * 
	 * @param min the minimal value of the DoubleGene to create.
	 * @param max the maximal value of the DoubleGene to create.
	 * @return the new created DoubleGene.
	 */
	public static Float64Gene valueOf(final double min, final double max) {
		return valueOf(Float64.valueOf(min), Float64.valueOf(max));
	}
	
	static final XMLFormat<Float64Gene> 
	XML = new XMLFormat<Float64Gene>(Float64Gene.class) 
	{
		private static final String MIN = "min";
		private static final String MAX = "max";
		
		@Override
		public Float64Gene newInstance(
			final Class<Float64Gene> cls, final InputElement element
		)
			throws XMLStreamException
		{
			final double min = element.getAttribute(MIN, 0.0);
			final double max = element.getAttribute(MAX, 1.0);
			final double value = element.<Double>getNext();
			return Float64Gene.valueOf(value, min, max);
		}
		@Override
		public void write(final Float64Gene gene, final OutputElement element) 
			throws XMLStreamException 
		{
			element.setAttribute(MIN, gene.getMin().doubleValue());
			element.setAttribute(MAX, gene.getMax().doubleValue());
			element.add(gene.getAllele().doubleValue());
		}
		@Override
		public void read(InputElement element, Float64Gene gene) throws XMLStreamException {
		}
	};

}




