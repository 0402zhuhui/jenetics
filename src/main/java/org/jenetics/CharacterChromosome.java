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

import static org.jenetics.util.Validator.nonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javolution.text.CharArray;
import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.jenetics.util.Array;
import org.jenetics.util.CharSet;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class CharacterChromosome extends AbstractChromosome<CharacterGene>
	implements CharSequence, 
				XMLSerializable
{	
	private static final long serialVersionUID = 1L;

	private transient CharSet _validCharacters;
	
	/**
	 * Create a new chromosome with the {@link CharacterGene#DEFAULT_CHARACTERS}
	 * char set as valid characters.
	 * 
	 * @param length the {@code length} of the new chromosome.
	 * @throws IllegalArgumentException if the {@code length} is smaller than 
	 * 		  one.
	 */
	public CharacterChromosome(final int length) {
		super(length);
		_validCharacters = CharacterGene.DEFAULT_CHARACTERS;
		_genes.fill(CharacterGene.valueOf(_validCharacters));
	}
	
	/**
	 * Create a new chromosome with the {@code validCharacters} char set as 
	 * valid characters.
	 * 
	 * @param validCharacters the valid characters for this chromosome.
	 * @param length the length of the new chromosome.
	 * @throws NullPointerException if the {@code validCharacters} is 
	 * 		  {@code null}.
	 * @throws IllegalArgumentException if the {@code length} is smaller than 
	 * 		  one.
	 */
	public CharacterChromosome(final CharSet validCharacters, final int length) {
		super(length);
		_validCharacters = nonNull(validCharacters, "Valid characters");
		_genes.fill(CharacterGene.valueOf(_validCharacters).asFactory()).seal();
	}
	
	/**
	 * Create a new chromosome from the given {@code genes} array. The genes 
	 * array is copied, so changes to the given genes array doesn't effect the 
	 * genes of this chromosome.
	 * 
	 * @param genes the genes that form the chromosome.
	 * @throws NullPointerException if the given gene array is {@code null}.
	 * @throws IllegalArgumentException if the length of the gene array is
	 * 		  smaller than one.
	 */
	public CharacterChromosome(final Array<CharacterGene> genes) {
		super(genes);
		_validCharacters = genes.get(0).getValidCharacters();
	}

	@Override
	public char charAt(final int index) {
		return getGene(index).getAllele();
	}

	@Override
	public CharacterChromosome subSequence(final int start, final int end) {
		return new CharacterChromosome(_genes.subArray(start, end).copy());
	}
	
	/**
	 * @throws NullPointerException if the given gene array is {@code null}.
	 */
	@Override
	public CharacterChromosome newInstance(final Array<CharacterGene> genes) {
		return new CharacterChromosome(genes);
	}
	
	/**
	 * Create a new, <em>random</em> chromosome.
	 */
	@Override
	public CharacterChromosome newInstance() {
		final CharacterChromosome chromosome = new CharacterChromosome(
				_validCharacters, 
				length()
			);
		chromosome._genes.fill(CharacterGene.valueOf(_validCharacters));
		return chromosome;
	}
	
	@Override
	public Text toText() {
		final TextBuilder out = TextBuilder.newInstance();
		out.append("[");
		for (CharacterGene gene : this) {
			out.append(gene.toText());
		}
		out.append("]");
		return out.toText();
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
		return obj instanceof CharacterChromosome && super.equals(obj);	
	}
	
	static final XMLFormat<CharacterChromosome> 
	XML = new XMLFormat<CharacterChromosome>(CharacterChromosome.class) 
	{
		private static final String LENGTH = "length";
		private static final String VALID_CHARS = "valid-characters";
		
		@Override
		public CharacterChromosome newInstance(
			final Class<CharacterChromosome> cls, final InputElement xml
		) 
			throws XMLStreamException 
		{
			final int length = xml.getAttribute(LENGTH, 0);
			final String validCharacters = xml.getAttribute(
					VALID_CHARS, CharacterGene.DEFAULT_CHARACTERS.toString()
				);
			
			final CharacterChromosome chromosome = new CharacterChromosome(
					new CharSet(validCharacters), length
				);
			final CharArray values = xml.getText();
			for (int i = 0; i < length; ++i) {
				chromosome._genes.set(i, CharacterGene.valueOf(values.charAt(i)));
			}
			return chromosome;
		}
		@Override
		public void write(final CharacterChromosome chromosome, final OutputElement xml) 
			throws XMLStreamException 
		{
			xml.setAttribute(LENGTH, chromosome.length());
			xml.setAttribute(VALID_CHARS, chromosome._validCharacters.toString());
			final StringBuilder out = new StringBuilder(chromosome.length());
			for (CharacterGene gene : chromosome) {
				out.append(gene.getAllele().charValue());
			}
			xml.addText(out.toString());
		}
		@Override
		public void read(final InputElement element, final CharacterChromosome chromosome) {
		}
		
	};

	private void writeObject(final ObjectOutputStream out)
		throws IOException 
	{
		out.defaultWriteObject();
	
		out.writeInt(length());
		out.writeObject(_validCharacters);
		
		for (CharacterGene gene : _genes) {
			out.writeChar(gene.getAllele().charValue());
		}
	}
	
	private void readObject(final ObjectInputStream in)
		throws IOException, ClassNotFoundException 
	{
		in.defaultReadObject();
	
		final int length = in.readInt();
		_validCharacters = (CharSet)in.readObject();
		
		_genes = new Array<CharacterGene>(length);
		for (int i = 0; i < length; ++i) {
			_genes.set(i, CharacterGene.valueOf(Character.valueOf(in.readChar()), _validCharacters));
		}
	}
	
}



