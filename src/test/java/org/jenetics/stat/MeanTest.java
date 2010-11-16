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
package org.jenetics.stat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class MeanTest {
	private Double[][] _values = null;
	
	@BeforeTest
	public void setup() throws Exception {
		final InputStream in = getClass().getResourceAsStream(
						"/org/jenetics/util/statistic-moments.txt"
					);		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		final List<String> lines = new java.util.ArrayList<String>(1000);
		String line = null;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		
		_values = new Double[lines.size()][10];
		
		for (int i = 0; i < lines.size(); ++i) {
			final String[] parts = lines.get(i).split("\\s");
			
			for (int j = 0; j < parts.length; ++j) {
				_values[i][j] = Double.valueOf(parts[j]);
			}
		}
	}
	
	@Test
	public void mean() {
		final Mean<Double> moment = new Mean<Double>();
		
		for (int i = 0; i < _values.length; ++i) {
			moment.accumulate(_values[i][0]);
			Assert.assertEquals(moment.getMean(), _values[i][1]);
		}
	}
}
