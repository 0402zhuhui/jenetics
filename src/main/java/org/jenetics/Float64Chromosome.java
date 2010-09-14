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
package org.jenetics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.jenetics.util.Array;
import org.jscience.mathematics.number.Float64;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class Float64Chromosome extends NumberChromosome<Float64Gene> 
	implements XMLSerializable
{	
	private static final long serialVersionUID = 1L;
	
	
	protected Float64Chromosome(final Array<Float64Gene> genes) {
		super(genes);
	}
	
	/**
	 * Create a new chromosome.
	 * 
	 * @param genes the genes this chromosome consists.
	 * @throws IllegalArgumentException if the number of genes is smaller than 
	 * 		 one.
	 * @throws NullPointerException if the {@code genes} are {@code null}.
	 */
	public Float64Chromosome(final Float64Gene... genes) {
		super(new Array<Float64Gene>(genes));
	}
	
	/**
	 * Create a new random DoubleChromosome.
	 * 
	 * @param min the min value of the {@link Float64Gene}s.
	 * @param max the max value of the {@link Float64Gene}s.
	 * @param length the length of the chromosome.
	 * @throws IllegalArgumentException if min is not less max.
	 */
	public Float64Chromosome(
		final Float64 min, 
		final Float64 max, 
		final int length
	) {
		super(
				new Array<Float64Gene>(length)
				.fill(Float64Gene.valueOf(min, max).asFactory())
			);
	}
	
	/**
	 * Create a new random chromosome.
	 * 
	 * @param min the minimal value of this chromosome.
	 * @param max the maximal value of this chromosome.
	 * @throws IllegalArgumentException if min is not less max.
	 */
	public Float64Chromosome(final double min, final double max) {
		this(Float64.valueOf(min), Float64.valueOf(max));
	}
	
	/**
	 * Create a new random chromosome.
	 * 
	 * @param min the minimal value of this chromosome.
	 * @param max the maximal value of this chromosome.
	 * @throws NullPointerException if {@code min} or {@code max} is {@code null}.
	 * @throws IllegalArgumentException if min is not less max.
	 */
	public Float64Chromosome(final Float64 min, final Float64 max) {
		this(min, max, 1);
	}
	
	/**
	 * Create a new chromosome
	 * 
	 * @param min the minimal value of this chromosome.
	 * @param max the maximal value of this chromosome.
	 * @param length the {@code length} of the new chromosome.
	 * @throws IllegalArgumentException if the {@code length} is smaller than 
	 * 		  one.
	 */
	public Float64Chromosome(final double min, final double max, final int length) {
		this(Float64.valueOf(min), Float64.valueOf(max), length);
	}
	
	@Override
	public Float64Chromosome newInstance(final Array<Float64Gene> genes) {
		return new Float64Chromosome(genes);		
	}

	/**
	 * Create a new, <em>random</em> chromosome.
	 */
	@Override
	public Float64Chromosome newInstance() {
		return new Float64Chromosome(_min.doubleValue(), _max.doubleValue(), length());
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		hash += super.hashCode()*37;
		return hash;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		return obj instanceof Float64Chromosome && 
					super.equals(obj);
	}
	
	
	static final XMLFormat<Float64Chromosome> 
	XML = new XMLFormat<Float64Chromosome>(Float64Chromosome.class) 
	{
		private static final String LENGTH = "length";
		private static final String MIN = "min";
		private static final String MAX = "max";
		
		@Override
		public Float64Chromosome newInstance(
			final Class<Float64Chromosome> cls, final InputElement xml
		) throws XMLStreamException {
			final int length = xml.getAttribute(LENGTH, 0);
			final double min = xml.getAttribute(MIN, 0.0);
			final double max = xml.getAttribute(MAX, 1.0);
			
			final Array<Float64Gene> genes = new Array<Float64Gene>(length);
			for (int i = 0; i < length; ++i) {
				final Float64 value = xml.getNext();
				genes.set(i, Float64Gene.valueOf(value.doubleValue(), min, max));
			}
			
			final Float64Chromosome chromosome = new Float64Chromosome(genes);
			chromosome._min = Float64.valueOf(min);
			chromosome._max = Float64.valueOf(max);
			
			return chromosome;
		}
		@Override
		public void write(final Float64Chromosome chromosome, final OutputElement xml) 
			throws XMLStreamException 
		{
			xml.setAttribute(LENGTH, chromosome.length());
			xml.setAttribute(MIN, chromosome._min.doubleValue());
			xml.setAttribute(MAX, chromosome._max.doubleValue());
			for (Float64Gene gene : chromosome) {
				xml.add(gene.getAllele());
			}
		}
		@Override
		public void read(final InputElement element, final Float64Chromosome chromosome) 
			throws XMLStreamException 
		{
		}
	};

	
	private void writeObject(final ObjectOutputStream out)
		throws IOException 
	{
		out.defaultWriteObject();
	
		out.writeInt(length());
		out.writeDouble(_min.doubleValue());
		out.writeDouble(_max.doubleValue());
		
		for (Float64Gene gene : _genes) {
			out.writeDouble(gene.doubleValue());
		}
	}
	
	private void readObject(final ObjectInputStream in)
		throws IOException, ClassNotFoundException 
	{
		in.defaultReadObject();
	
		final int length = in.readInt();
		Float64 min = Float64.valueOf(in.readDouble());
		Float64 max = Float64.valueOf(in.readDouble());
		
		_min = min;
		_max = max;
		_genes = new Array<Float64Gene>(length);
		for (int i = 0; i < length; ++i) {
			_genes.set(i, Float64Gene.valueOf(Float64.valueOf(in.readDouble()), min, max));
		}
	}

}





