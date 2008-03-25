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
 * @version $Id: NumberChromosome.java,v 1.1 2008-03-25 18:31:55 fwilhelm Exp $
 */
public abstract class NumberChromosome<N extends NumberGene<?>> 
	extends AbstractChromosome<N> 
{
	/**
	 * The minimum value of this <code>NumberChromosome</code>.
	 */
	protected org.jscience.mathematics.number.Number<?> _min;
	
	/**
	 * The maximum value of this <code>NumberChromosome</code>.
	 */
	protected org.jscience.mathematics.number.Number<?> _max;
	
	protected NumberChromosome() {
	}
	
	/**
	 * Return the byte value of this <code>NumberChromosome</code> at the given
	 * <code>index</code>.
	 * 
	 * @param index the index of the {@link NumberGene}.
	 * @return the byte value of the {@link Gene} with the given <code>index</code>.
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * 		(index < 0 || index >= length()).
	 */
	public byte byteValue(final int index) {
		return getGene(index).byteValue();
	}
	
	/**
	 * Return the byte value of this <code>NumberChromosome</code> at the  
	 * <code>index</code> 0.
	 * 
	 * @return the byte value of the {@link Gene} with <code>index</code> 0.
	 */
	public byte byteValue() {
		return byteValue(0);
	}
	
	/**
	 * Return the short value of this <code>NumberChromosome</code> at the given
	 * <code>index</code>.
	 * 
	 * @param index the index of the {@link NumberGene}.
	 * @return the short value of the {@link Gene} with the given <code>index</code>.
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * 		(index < 0 || index >= length()).
	 */
	public short shortValue(final int index) {
		return getGene(index).shortValue();
	}
	
	/**
	 * Return the short value of this <code>NumberChromosome</code> at the  
	 * <code>index</code> 0.
	 * 
	 * @return the short value of the {@link Gene} with <code>index</code> 0.
	 */
	public short shortValue() {
		return shortValue(0);
	}
	
	/**
	 * Return the int value of this <code>NumberChromosome</code> at the given
	 * <code>index</code>.
	 * 
	 * @param index the index of the {@link NumberGene}.
	 * @return the int value of the {@link Gene} with the given <code>index</code>.
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * 		(index < 0 || index >= length()).
	 */
	public int intValue(final int index) {
		return getGene(index).intValue();
	}
	
	/**
	 * Return the int value of this <code>NumberChromosome</code> at the  
	 * <code>index</code> 0.
	 * 
	 * @return the int value of the {@link Gene} with <code>index</code> 0.
	 */
	public int intValue() {
		return intValue(0);
	}
	
	/**
	 * Return the long value of this <code>NumberChromosome</code> at the given
	 * <code>index</code>.
	 * 
	 * @param index the index of the {@link NumberGene}.
	 * @return the long value of the {@link Gene} with the given <code>index</code>.
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * 		(index < 0 || index >= length()).
	 */
	public long longValue(final int index) {
		return getGene(index).longValue();
	}
	
	/**
	 * Return the long value of this <code>NumberChromosome</code> at the  
	 * <code>index</code> 0.
	 * 
	 * @return the long value of the {@link Gene} with <code>index</code> 0.
	 */
	public long longValue() {
		return longValue(0);
	}
	
	/**
	 * Return the float value of this <code>NumberChromosome</code> at the given
	 * <code>index</code>.
	 * 
	 * @param index the index of the {@link NumberGene}.
	 * @return the float value of the {@link Gene} with the given <code>index</code>.
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * 		(index < 0 || index >= length()).
	 */
	public float floatValue(final int index) {
		return getGene(index).floatValue();
	}
	
	/**
	 * Return the float value of this <code>NumberChromosome</code> at the  
	 * <code>index</code> 0.
	 * 
	 * @return the float value of the {@link Gene} with <code>index</code> 0.
	 */
	public float floatValue() {
		return floatValue(0);
	}
	
	/**
	 * Return the double value of this <code>NumberChromosome</code> at the given
	 * <code>index</code>.
	 * 
	 * @param index the index of the {@link NumberGene}.
	 * @return the double value of the {@link Gene} with the given <code>index</code>.
	 * @throws IndexOutOfBoundsException if the index is out of range 
	 * 		(index < 0 || index >= length()).
	 */
	public double doubleValue(final int index) {
		return getGene(index).doubleValue();
	}
	
	/**
	 * Return the double value of this <code>NumberChromosome</code> at the  
	 * <code>index</code> 0.
	 * 
	 * @return the double value of the {@link Gene} with <code>index</code> 0.
	 */
	public double doubleValue() {
		return doubleValue(0);
	}

}




