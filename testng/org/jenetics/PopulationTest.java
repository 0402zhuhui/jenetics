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

import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.jscience.mathematics.number.Float64;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: PopulationTest.java,v 1.2 2009-02-22 10:32:21 fwilhelm Exp $
 */
public class PopulationTest {
	
	final class Function implements FitnessFunction<DoubleGene, Float64> {
		private static final long serialVersionUID = 2793605351118238308L;
		
		public Float64 evaluate(final Genotype<DoubleGene> genotype) {
			final DoubleGene gene = genotype.getChromosome().getGene(0);
			return Float64.valueOf(sin(toRadians(gene.doubleValue())));
		}
	}
	
	@Test
	public void serialization() throws XMLStreamException, IOException {
		final int size = 10;
		final GenotypeFactory<DoubleGene> gtf = Genotype.valueOf(new DoubleChromosome(0, 360));
		final Function ff = new Function();
		final IdentityScaler<Float64> scaler = IdentityScaler.valueOf();
		final Population<DoubleGene, Float64> population = new Population<DoubleGene, Float64>();
		
		for (int i = 0; i < size; ++i) {
			final Phenotype<DoubleGene, Float64> pt = Phenotype.valueOf(
				gtf.newGenotype(), ff, scaler, 0
			);
			population.add(pt);
		}
		
		
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final XMLObjectWriter writer = XMLObjectWriter.newInstance(out);
		writer.setIndentation("\t");
		writer.write(population);
		writer.close();
		out.flush();
		
		byte[] data = out.toByteArray();
		System.out.println(new String(data));
		
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		final XMLObjectReader reader = XMLObjectReader.newInstance(in);
		final Population<?, ?> p = reader.read();
		
		Assert.assertEquals(
				p, 
				population);
	}
	
}





