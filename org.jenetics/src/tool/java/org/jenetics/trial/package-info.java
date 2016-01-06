/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */

@XmlSchema(
	namespace = "http://jenetics.io/Jenetics/trail/1",
	elementFormDefault = XmlNsForm.QUALIFIED,
	xmlns = {
		@XmlNs(
			namespaceURI = "http://jenetics.io/Jenetics/trail/1",
			prefix = ""
		)
	}
)
package org.jenetics.trial;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
