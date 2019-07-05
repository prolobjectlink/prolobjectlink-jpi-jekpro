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
package org.prolobjectlink.prolog.jekpro;

import static org.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;

import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologStructure;
import org.prolobjectlink.prolog.PrologTerm;

import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.TermCompound;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologStructure extends JekeJekePrologTerm implements PrologStructure {

	JekejekePrologStructure(PrologProvider provider, String functor, PrologTerm... arguments) {
		super(STRUCTURE_TYPE, provider);
		Object[] terms = new Object[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			terms[i] = ((JekeJekePrologTerm) arguments[i]).value;
		}
		value = new TermCompound(removeQuoted(functor), terms);
	}

	JekejekePrologStructure(PrologProvider provider, String functor, Object... arguments) {
		super(STRUCTURE_TYPE, provider);
		value = new TermCompound(removeQuoted(functor), arguments);
	}

	JekejekePrologStructure(PrologProvider provider, PrologTerm left, String operator, PrologTerm right) {
		super(STRUCTURE_TYPE, provider);
		AbstractTerm leftOperand = ((JekeJekePrologTerm) left).value;
		AbstractTerm rightOperand = ((JekeJekePrologTerm) right).value;
		value = new TermCompound(operator, leftOperand, rightOperand);
	}

	JekejekePrologStructure(PrologProvider provider, Object left, String functor, Object right) {
		super(STRUCTURE_TYPE, provider, new TermCompound(functor, left, right));
	}

	public PrologTerm getArgument(int index) {
		checkIndex(index, getArity());
		return getArguments()[index];
	}

	public PrologTerm[] getArguments() {
		TermCompound structure = (TermCompound) value;
		int arity = structure.getArity();
		PrologTerm[] arguments = new PrologTerm[arity];
		for (int i = 0; i < arity; i++) {
			arguments[i] = toTerm(structure.getArg(i), PrologTerm.class);
		}
		return arguments;
	}

	public int getArity() {
		TermCompound structure = (TermCompound) value;
		return structure.getArity();
	}

	public String getFunctor() {
		TermCompound structure = (TermCompound) value;
		return structure.getFunctor();
	}

	public String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	public boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

	public final PrologTerm getRight() {
		return getArgument(1);
	}

	public final PrologTerm getLeft() {
		return getArgument(0);
	}

	public final String getOperator() {
		return getFunctor();
	}

}
