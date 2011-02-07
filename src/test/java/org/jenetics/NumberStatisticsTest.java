package org.jenetics;

import java.io.IOException;

import javolution.xml.stream.XMLStreamException;

import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id$
 */
public class NumberStatisticsTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void xmlSerialize() throws XMLStreamException {
		final NumberStatistics numberStatistics = new NumberStatistics(
				Optimize.MAXIMUM, 234234, null, null, 
				0, 0, 0, 3.234, 42.234, 23, 23, 23
			);
		
		SerializeUtils.testXMLSerialization(numberStatistics);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void objectSerialize() throws IOException {
		final NumberStatistics numberStatistics = new NumberStatistics(
				Optimize.MAXIMUM, 234234, null, null, 
				0, 0, 0, 3.234, 42.234, 23, 23, 23
			);
		
		SerializeUtils.testSerialization(numberStatistics);
	}
	
}
