/*
 * #%L
 * prolobjectlink-jpi-jekpro
 * %%
 * Copyright (C) 2019 Prolobjectlink Project
 * %%
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
 * #L%
 */
package io.github.prolobjectlink.prolog.jekpro;

import static io.github.prolobjectlink.prolog.PrologTermType.LIST_TYPE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import io.github.prolobjectlink.prolog.AbstractIterator;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.TermAtomic;
import jekpro.tools.term.TermCompound;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
class JekejekePrologList extends JekejekePrologTerm implements PrologList {

	static final TermAtomic EMPTY = new TermAtomic("[]");

	protected JekejekePrologList(PrologProvider provider) {
		super(LIST_TYPE, provider, EMPTY);
	}

	protected JekejekePrologList(PrologProvider provider, AbstractTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = EMPTY;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = new TermCompound(".", arguments[i], value);
		}
	}

	protected JekejekePrologList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = EMPTY;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = new TermCompound(".", arguments[i].getObject(), value);
		}
	}

	protected JekejekePrologList(PrologProvider provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		AbstractTerm h = ((JekejekePrologTerm) head).value;
		AbstractTerm t = ((JekejekePrologTerm) tail).value;
		value = new TermCompound(".", h, t);
	}

	protected JekejekePrologList(PrologProvider provider, PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = fromTerm(tail, AbstractTerm.class);
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = new TermCompound(".", fromTerm(arguments[i], AbstractTerm.class), value);
		}
	}

	public int size() {
		int size = 0;
		Iterator<?> i = iterator();
		while (i.hasNext()) {
			i.next();
			size++;
		}
		return size;
	}

	public void clear() {
		value = EMPTY;
	}

	public boolean isEmpty() {
		return value.equals(EMPTY);
	}

	public Iterator<PrologTerm> iterator() {
		return new JekejekePrologListIter(value);
	}

	public PrologTerm getHead() {
		TermCompound list = (TermCompound) value;
		return toTerm(list.getArg(0), PrologTerm.class);
	}

	public PrologTerm getTail() {
		TermCompound list = (TermCompound) value;
		return toTerm(list.getArg(1), PrologTerm.class);
	}

	public int getArity() {
		if (value instanceof TermCompound) {
			TermCompound list = (TermCompound) value;
			return list.getArity();
		}
		return 0;
	}

	public String getFunctor() {
		if (value instanceof TermCompound) {
			TermCompound list = (TermCompound) value;
			return list.getFunctor();
		}
		TermAtomic list = (TermAtomic) value;
		return "" + list + "";
	}

	public PrologTerm[] getArguments() {
		List<PrologTerm> l = new ArrayList<PrologTerm>();
		for (Iterator<PrologTerm> i = iterator(); i.hasNext();) {
			l.add(i.next());
		}
		return l.toArray(new PrologTerm[0]);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder("[");
		Iterator<PrologTerm> i = iterator();
		if (i.hasNext()) {
			string.append(i.next());
		}
		while (i.hasNext()) {
			string.append(", ");
			string.append(i.next());
		}
		return string.append("]").toString();
	}

	private class JekejekePrologListIter extends AbstractIterator<PrologTerm> implements Iterator<PrologTerm> {

		private AbstractTerm ptr;

		private JekejekePrologListIter(AbstractTerm l) {
			ptr = l;
		}

		public boolean hasNext() {
			return ptr instanceof TermCompound && !((TermCompound) ptr).getArg(0).equals(EMPTY);
		}

		public PrologTerm next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			PrologTerm next = toTerm(((TermCompound) ptr).getArg(0), PrologTerm.class);
			if (((TermCompound) ptr).getArg(1) instanceof TermCompound) {
				ptr = (TermCompound) ((TermCompound) ptr).getArg(1);
			} else {
				ptr = null;
			}
			return next;
		}

	}

}
