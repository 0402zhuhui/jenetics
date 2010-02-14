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

import static org.jenetics.util.Validator.nonNull;

import java.util.Random;

import javolution.context.ObjectFactory;
import javolution.lang.Realtime;
import javolution.text.Text;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.jenetics.util.CharSet;
import org.jenetics.util.RandomRegistry;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 */
public class CharacterGene 
	implements Gene<Character, CharacterGene>, Comparable<CharacterGene>, 
				Realtime, XMLSerializable 
{
	private static final long serialVersionUID = 5091130159700639888L;
	
	public static final CharSet DEFAULT_CHARACTERS = new CharSet(
				CharSet.expand("0-9a-zA-Z") +  " !\"$%&/()=?`{[]}\\+~*#';.:,-_<>|@^'"
			);
	
	private CharSet _validCharacters;
	private Character _character;
	
	protected CharacterGene() {
	}
		
	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public Character getAllele() {
		return _character;
	}

	@Override
	public CharacterGene copy() {
		return valueOf(_character, _validCharacters);
	}
	
	/**
	 * @see java.lang.Character#compareTo(java.lang.Character)
	 * @param that The other gene to compare.
	 * @return the value 0 if the argument Character is equal to this Character; 
	 *         a value less than 0 if this Character is numerically less than 
	 *         the Character argument; and a value greater than 0 if this 
	 *         Character is numerically greater than the Character argument 
	 *         (unsigned comparison). Note that this is strictly a numerical 
	 *         comparison; it is not local-dependent. 
	 */
	@Override
	public int compareTo(final CharacterGene that) {
		return getAllele().compareTo(that.getAllele());
	}
	
	@Override
	public CharacterGene newInstance() {
		final Random random = RandomRegistry.getRandom();
		final int index = random.nextInt(_validCharacters.length());
		
		return valueOf(_validCharacters.charAt(index), _validCharacters);
	}
	
	@Override
	public int hashCode() {
		return _character.hashCode();
	}
	
	@Override 
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof CharacterGene)) {
			return false;
		}
		CharacterGene gene = (CharacterGene)obj;
		return getAllele().equals(gene.getAllele());
	}
	
	@Override
	public String toString() {
		return _character.toString();
	}
	
	@Override
	public Text toText() {
		return Text.valueOf(_character);
	}
	
	
	/**
	 * Test, if the given character is valid.
	 * 
	 * @param c The character to test.
	 * @return true if the character is valid, false otherwise.
	 */
	public boolean isValidCharacter(final Character c) {
		return _validCharacters.contains(c);
	}
	
	/**
	 * Retunr a (unmodifiable) set of valid characters.
	 * 
	 * @return the {@link CharSet} of valid characters.
	 */
	public CharSet getValidCharacters() {
		return _validCharacters;
	}
	
	private static final ObjectFactory<CharacterGene> 
	FACTORY = new ObjectFactory<CharacterGene>() {
		@Override
		protected CharacterGene create() {
			return new CharacterGene();
		}
	};
	
	/**
	 * Create a new CharacterGene with a randomly chosen character from the
	 * set of valid characters.
	 */
	public static CharacterGene valueOf(final CharSet validCharacters) {
		final Random random = RandomRegistry.getRandom();
		int pos = random.nextInt(validCharacters.length());
		return valueOf(validCharacters.charAt(pos), validCharacters);
	}
	
	/**
	 * Create a new CharacterGene from the give character.
	 * 
	 * @param character The allele.
	 * @throws NullPointerException if the <code>character</code> is null.
	 * @throws IllegalArgumentException if the <code>character</code> is not
	 * 		a valid character. 
	 * 		See {@link CharacterGene#isValidCharacter(Character)}
	 * 		and {@link CharacterGene#getCharacters()}.
	 */
	public static CharacterGene valueOf(
		final Character character, 
		final CharSet validCharacters
	) {
		nonNull(character, "Character");
		nonNull(validCharacters, "Valid characters");
		
		CharacterGene g = FACTORY.object();
		if (!validCharacters.contains(character)) {
			throw new IllegalArgumentException(
				"Character '" + character + "' is not valid. "
			);
		}
		g._character = character;
		g._validCharacters = validCharacters;
		
		return g;
	}
	
	public static CharacterGene valueOf(final Character character) {
		return valueOf(character, DEFAULT_CHARACTERS);
	}
	
	public static CharacterGene valueOf() {
		final Random random = RandomRegistry.getRandom();
		final int index = random.nextInt(DEFAULT_CHARACTERS.length());
		return valueOf(DEFAULT_CHARACTERS.charAt(index));
	}
	
	static final XMLFormat<CharacterGene> 
	XML = new XMLFormat<CharacterGene>(CharacterGene.class) 
	{
		private static final String VALID_CHARS = "valid-characters";
		
		@Override
		public CharacterGene newInstance(
			final Class<CharacterGene> cls, final InputElement xml
		) 
			throws XMLStreamException 
		{
			final String validCharacters = xml.getAttribute(
					VALID_CHARS, 
					DEFAULT_CHARACTERS.toString()
				);
			final String character = xml.getText().toString();
			
			return CharacterGene.valueOf(character.charAt(0), new CharSet(validCharacters));
		}
		@Override
		public void write(final CharacterGene gene, final OutputElement xml) 
			throws XMLStreamException 
		{
			xml.setAttribute(VALID_CHARS, gene.getValidCharacters().toString());
			xml.addText(gene._character.toString());
		}
		@Override
		public void read(final InputElement element, final CharacterGene gene) {
		}
	};

	
}






