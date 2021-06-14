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

import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologVariable extends JekejekePrologTerm implements PrologVariable {

	JekejekePrologVariable(PrologProvider provider) {
		super(VARIABLE_TYPE, provider, new TermVariable());
	}

	JekejekePrologVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, new TermVariable(name));
	}

	public boolean isAnonymous() {
		return getName().equals("_");
	}

	public final String getName() {
		return ((TermVariable) value).getName();
	}

	public final void setName(String name) {
		TermVariable old = (TermVariable) value;
		value = new TermVariable(name, old.getVar());
	}

	public PrologTerm[] getArguments() {
		return new JekejekePrologVariable[0];
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
	}

	public int getPosition() {
		throw new UnsupportedOperationException("getPosition()");
	}

}
