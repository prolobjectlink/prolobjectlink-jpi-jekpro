/*-
 * #%L
 * prolobjectlink-jpi-tuprolog
 * %%
 * Copyright (C) 2020 - 2021 Prolobjectlink Project
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package io.github.prolobjectlink.prolog.jekpro;

import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologReference;
import io.github.prolobjectlink.prolog.PrologTerm;
import jekpro.tools.term.AbstractTerm;

public class JekejekePrologReference extends JekejekePrologTerm implements PrologReference {

	JekejekePrologReference(PrologProvider provider, AbstractTerm reference) {
		super(OBJECT_TYPE, provider, reference);
	}

	JekejekePrologReference(PrologProvider provider, Object reference) {
		super(OBJECT_TYPE, provider, null);
	}

	public int getArity() {
		return 0;
	}

	public String getFunctor() {
		return "@";
	}

	public PrologTerm[] getArguments() {
		return new PrologTerm[0];
	}

	public boolean isTrueType() {
		return getObject().equals(true);
	}

	public boolean isFalseType() {
		return getObject().equals(false);
	}

	public boolean isNullType() {
		return getObject() == null;
	}

	public boolean isVoidType() {
		return getObject() == void.class;
	}

	public boolean isObjectType() {
		return getType() == OBJECT_TYPE;
	}

	public boolean isReference() {
		return isObjectType() || isNullType();
	}

	public Object getObject() {
		return null;
	}

}
