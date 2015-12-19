/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics.trial;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.jenetics.internal.util.require;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
final class jaxb {
	private jaxb() {require.noInstance();}

	private static final class JAXBContextHolder {
		private static final JAXBContext CONTEXT; static {
			try {
				CONTEXT = JAXBContext.newInstance(
					"org.jenetics:" +
					"org.jenetics.internal.util:" +
					"org.jenetics.engine:" +
					"org.jenetics.trial"
				);
			} catch (JAXBException e) {
				throw new DataBindingException(
					"Something went wrong while creating JAXBContext.", e
				);
			}
		}
	}

	public static JAXBContext context() {
		return JAXBContextHolder.CONTEXT;
	}

}
