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
package org.jenetics;

import java.util.Iterator;

import org.jenetics.util.ISeq;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public class TreeChromosome<G extends Gene<?, G>> implements Chromosome<G> {


	final class Node<A> implements Gene<A, Node<A>> {
		@Override
		public A getAllele() {
			return null;
		}

		@Override
		public Node<A> newInstance() {
			return null;
		}

		@Override
		public Node<A> newInstance(A value) {
			return null;
		}

		@Override
		public boolean isValid() {
			return false;
		}
	}


	@Override
	public Chromosome<G> newInstance(ISeq<G> genes) {
		return null;
	}

	@Override
	public G getGene(int index) {
		return null;
	}

	@Override
	public int length() {
		return 0;
	}

	@Override
	public ISeq<G> toSeq() {
		return null;
	}

	@Override
	public Chromosome<G> newInstance() {
		return null;
	}

	@Override
	public Iterator<G> iterator() {
		return null;
	}

	@Override
	public boolean isValid() {
		return false;
	}
}
