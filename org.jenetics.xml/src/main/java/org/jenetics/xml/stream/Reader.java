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
package org.jenetics.xml.stream;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static javax.xml.stream.XMLStreamConstants.CDATA;
import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version !__version__!
 * @since !__version__!
 */
public abstract class Reader<T> {

	static enum Type {
		ELEM, ATTR, LIST, TEXT
	}

	private final String _name;
	private final Type _type;

	Reader(final String name, final Type type) {
		_name = requireNonNull(name);
		_type = requireNonNull(type);
	}

	/**
	 * Read the given type from the underlying XML stream {@code reader}.
	 *
	 * @param reader the underlying XML stream {@code reader}
	 * @return the read type, maybe {@code null}
	 * @throws XMLStreamException if an error occurs while reading the value
	 */
	public abstract T read(final XMLStreamReader reader)
		throws XMLStreamException;

	/**
	 * Create a new reader with the new type {@code B}.
	 *
	 * @param mapper the mapper function
	 * @param <B> the target type of the new reader
	 * @return a new reader
	 */
	public <B> Reader<B> map(final Function<? super T, ? extends B> mapper) {
		requireNonNull(mapper);

		return new Reader<B>(_name, _type) {
			@Override
			public B read(final XMLStreamReader reader)
				throws XMLStreamException
			{
				return mapper.apply(Reader.this.read(reader));
			}
		};
	}

	/**
	 * Return the name of the element processed by this reader.
	 *
	 * @return the element name the reader is processing
	 */
	public String name() {
		return _name;
	}

	Type type() {
		return _type;
	}

	@Override
	public String toString() {
		return format("Reader[%s, %s]", name(), type());
	}

	public static List<String> attrs(final String... attrs) {
		return Arrays.asList(attrs);
	}

	public static Reader<String> attr(final String name) {
		return new AttrReader(name);
	}

	public static <T> Reader<T> elem(
		final String name,
		final Function<Object[], T> mapper,
		final Reader<?>... children
	) {
		requireNonNull(name);
		requireNonNull(mapper);
		requireNonNull(children);

		return new ElemReader<>(name, mapper, asList(children), Type.ELEM);
	}

	@SuppressWarnings("unchecked")
	public static <T> Reader<T> elem(final String name, final Reader<?>... children) {
		return elem(name, v -> v.length > 0 ? (T)v[0] : null, children);
	}

	public static <T> Reader<T> text(final Function<String, ? extends T> mapper) {
		return text().map(mapper);
	}

	public static Reader<String> text() {
		return new TextReader();
	}

	public static <T> Reader<List<T>> elems(final Reader<? extends T> reader) {
		return new ListReader<T>(reader);
	}


	/**
	 * Create a new {@code XMLReader} with the given elements.
	 *
	 * @param creator creates the final object from the read arguments
	 * @param name the element name
	 * @param attrs the element attributes
	 * @param children the child element readers
	 * @param <T> the object type
	 * @return the reader for the given element
	 */
	public static <T> Reader<T> of(
		final Function<Object[], T> creator,
		final String name,
		final List<String> attrs,
		final Reader<?>... children
	) {
		return new ElemReader<T>(name, creator, asList(children), Type.ELEM);
	}

	/**
	 * Create a new {@code XMLReader} with the given elements.
	 * <pre>{@code
	 * XMLReader.of(
	 *     a -> Link.of((String)a[0], (String)a[1], (String)a[2]),
	 *     "link", attr("href"),
	 *     XMLReader.of("text"),
	 *     XMLReader.of("type")
	 * )
	 * }</pre>
	 *
	 * @param creator creates the final object from the read arguments
	 * @param name the element name
	 * @param children the child element readers
	 * @param <T> the object type
	 * @return the reader for the given element
	 */
	static <T> Reader<T> of(
		final Function<Object[], T> creator,
		final String name,
		final Reader<?>... children
	) {
		return of(creator, name, emptyList(), children);
	}

	@SuppressWarnings("unchecked")
	static <T> Reader<T> of(final String name, final Reader<T> reader) {
		//return r -> reader.read(r);
		return of(a -> (T)a[0], name, reader);
	}

	/**
	 * Create a reader for a leaf element with the given {@code name}.
	 *
	 * @param name the element
	 * @return the reader for the given element
	 */
	static Reader<String> of(final String name) {
		return null;
		//return new TextReader(name);
	}

	/**
	 * Return a reader which reads a list of elements.
	 *
	 * @param reader the basic element reader
	 * @param <T> the object type
	 * @return the reader for the given elements
	 */
	static <T> Reader<List<T>> ofList(final Reader<T> reader) {
		return null; //new ListReader<T>(reader);
	}

}

/**
 * Reader implementation for reading the attribute of the current node.
 */
final class AttrReader extends Reader<String> {

	AttrReader(final String name) {
		super(name, Type.ATTR);
	}

	@Override
	public String read(final XMLStreamReader xml) throws XMLStreamException {
		xml.require(XMLStreamReader.START_ELEMENT, null, null);
		return xml.getAttributeValue(null, name());
	}

}

/**
 * Reader implementation for reading the text of the current node.
 */
final class TextReader extends Reader<String> {

	TextReader() {
		super("", Type.TEXT);
	}

	@Override
	public String read(final XMLStreamReader xml) throws XMLStreamException {
		return xml.getText();
	}
}

final class ListReader<T> extends Reader<List<T>> {

	private final Reader<? extends T> _adoptee;

	ListReader(final Reader<? extends T> adoptee) {
		super(adoptee.name(), Type.LIST);
		_adoptee = adoptee;
	}

	@Override
	public List<T> read(final XMLStreamReader xml) throws XMLStreamException {
		xml.require(XMLStreamReader.START_ELEMENT, null, name());
		return Collections.singletonList(_adoptee.read(xml));
	}
}

/**
 * The main XML reader implementation.
 *
 * @param <T> the object type
 */
final class ElemReader<T> extends Reader<T> {

	private final Function<Object[], T> _creator;
	private final List<Reader<?>> _children;

	ElemReader(
		final String name,
		final Function<Object[], T> creator,
		final List<Reader<?>> children,
		final Type type
	) {
		super(name, type);
		_creator = requireNonNull(creator);
		_children = requireNonNull(children);
	}

	@Override
	public T read(final XMLStreamReader xml)
		throws XMLStreamException
	{
		xml.require(XMLStreamReader.START_ELEMENT, null, name());

		final Map<String, ReaderResult> results = ReaderResult.of(_children);

		for (ReaderResult result: results.values()) {
			if (result.reader().type() == Type.ATTR) {
				result.put(result.reader().read(xml));
			}
		}

		while (xml.hasNext()) {
			switch (xml.next()) {
				case XMLStreamReader.START_ELEMENT:
					final ReaderResult result = results.get(xml.getLocalName());
					if (result == null) break;

					final Object obj = result.reader().read(xml);
					result.put(obj);
					break;
				case CHARACTERS:
				case CDATA:
					for (ReaderResult r: results.values()) {
						if (r.reader().type() == Type.TEXT) {
							r.put(r.reader().read(xml));
						}
					}
					break;
				case END_ELEMENT:
					if (name().equals(xml.getLocalName())) {
						final Object[] array = new Object[results.size()];
						for (ReaderResult r : results.values()) {
							array[r.index()] = r.value();
						}
						return _creator.apply(array);
					}
			}
		}

		throw new XMLStreamException(format(
			"Premature end of file while reading '%s'.", name()
		));
	}

}

interface ReaderResult {
	void put(final Object value);
	Reader<?> reader();
	int index();
	Object value();

	static Map<String, ReaderResult> of(final List<Reader<?>> readers) {
		final Map<String, ReaderResult> results = new HashMap<>();


		for (int i = 0; i < readers.size(); ++i) {
			final Reader<?> reader = readers.get(i);
			final ReaderResult result; switch (reader.type()) {
				case TEXT: result = new ValueResult(reader, i); break;
				case LIST: result = new ListResult(reader, i); break;
				default: result = new ValueResult(reader, i);
			}

			results.put(reader.name(), result);
		}

		return results;
	}
}

final class ValueResult implements ReaderResult {
	private final Reader<?> _reader;
	private final int _index;
	private Object _value;

	ValueResult(final Reader<?> reader, final int index) {
		_reader = reader;
		_index = index;
	}

	@Override
	public void put(final Object value) {
		_value = value;
	}

	@Override
	public Reader<?> reader() {
		return _reader;
	}

	@Override
	public int index() {
		return _index;
	}

	@Override
	public Object value() {
		return _value;
	}
}

final class TextResult implements ReaderResult {
	private final Reader<?> _reader;
	private final int _index;
	private final StringBuilder _value = new StringBuilder();

	TextResult(final Reader<?> reader, final int index) {
		_reader = reader;
		_index = index;
	}

	@Override
	public void put(final Object value) {
		_value.append(value);
	}

	@Override
	public Reader<?> reader() {
		return _reader;
	}

	@Override
	public int index() {
		return _index;
	}

	@Override
	public String value() {
		return _value.toString();
	}
}

final class ListResult implements ReaderResult {
	private final Reader<?> _reader;
	private final int _index;
	private final List<Object> _value = new ArrayList<>();

	ListResult(final Reader<?> reader, final int index) {
		_reader = reader;
		_index = index;
	}

	@Override
	public void put(final Object value) {
		if (value instanceof List<?>) {
			_value.addAll((List<?>)value);
		} else {
			_value.add(value);
		}
	}

	@Override
	public Reader<?> reader() {
		return _reader;
	}

	@Override
	public int index() {
		return _index;
	}

	@Override
	public List<Object> value() {
		return _value;
	}
}
