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

import static io.github.prolobjectlink.prolog.PrologLogger.RUNTIME_ERROR;
import static io.github.prolobjectlink.prolog.PrologTermType.ATOM_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.CUT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.DOUBLE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FAIL_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FALSE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.INTEGER_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.LONG_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.NIL_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.TRUE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;
import static jekpro.tools.term.AbstractTerm.unifyTerm;

import io.github.prolobjectlink.prolog.AbstractTerm;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import jekpro.model.c.c;
import jekpro.model.c.p;
import jekpro.model.d.k;
import jekpro.platform.headless.ToolkitLibrary;
import jekpro.tools.call.Interpreter;
import jekpro.tools.call.InterpreterException;
import jekpro.tools.term.AbstractSkel;
import jekpro.tools.term.Knowledgebase;
import jekpro.tools.term.TermAtomic;
import jekpro.tools.term.TermCompound;
import jekpro.tools.term.TermVar;
import matula.util.data.MapEntry;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
abstract class JekejekePrologTerm extends AbstractTerm implements PrologTerm {

	protected jekpro.tools.term.AbstractTerm value;
	private static Knowledgebase know = new Knowledgebase(ToolkitLibrary.DEFAULT);
	private static Interpreter prolog = know.iterable();

	public JekejekePrologTerm(int type, PrologProvider provider) {
		super(type, provider);
	}

	public JekejekePrologTerm(int type, PrologProvider provider, jekpro.tools.term.AbstractTerm value) {
		super(type, provider);
		this.value = value;
	}

	public final boolean isAtom() {
		return type == ATOM_TYPE || type == FAIL_TYPE || type == FALSE_TYPE || type == TRUE_TYPE || isEmptyList()
				|| type == CUT_TYPE || type == NIL_TYPE;
	}

	public final boolean isNumber() {
		return isDouble() || isFloat() || isInteger() || isLong();
	}

	public final boolean isFloat() {
		return type == FLOAT_TYPE;
	}

	public final boolean isDouble() {
		return type == DOUBLE_TYPE;
	}

	public final boolean isInteger() {
		return type == INTEGER_TYPE;
	}

	public final boolean isLong() {
		return type == LONG_TYPE;
	}

	public final boolean isVariable() {
		return type == VARIABLE_TYPE;
	}

	public final boolean isList() {
		return (this instanceof JekejekePrologList)

				||

				(this instanceof JekejekePrologEmpty);
	}

	public final boolean isStructure() {
		return this instanceof JekejekePrologStructure;
	}

	public final boolean isNil() {
		return this instanceof JekejekePrologNil;
	}

	public final boolean isEmptyList() {
		return this instanceof JekejekePrologEmpty;
	}

	public final boolean isEvaluable() {
		if (value instanceof TermCompound) {
			p store = (p) prolog.getKnowledgebase().getStore();
			MapEntry<String, c>[] source = store.aj();
			for (MapEntry<String, c> mapEntry : source) {
				k[] opsinv = mapEntry.value.snapshotOpersInv();
				for (k op : opsinv) {
					if (!getFunctor().equals(".") && op.getKey().equals(getFunctor())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public final boolean isAtomic() {
		return value instanceof TermAtomic;
	}

	public final boolean isCompound() {
		return value instanceof TermCompound;
	}

	public final boolean isTrueType() {
		return getObject().equals(true);
	}

	public final boolean isFalseType() {
		return getObject().equals(false);
	}

	public final boolean isNullType() {
		return isObjectType() && getObject() == null;
	}

	public final boolean isVoidType() {
		return getObject() == void.class;
	}

	public final boolean isObjectType() {
		return getType() == OBJECT_TYPE;
	}

	public final boolean isReference() {
		return isObjectType();
	}

	public final PrologTerm getTerm() {
		if (value instanceof TermVar) {
			TermVar var = (TermVar) value;
			AbstractTerm i = (AbstractTerm) var.deref();
			return toTerm(i, PrologTerm.class);
		}
		return this;
	}

	public final boolean unify(PrologTerm term) {
		return unify(fromTerm(term, jekpro.tools.term.AbstractTerm.class));
	}

	private boolean unify(jekpro.tools.term.AbstractTerm t2) {
		try {

			//
			if (value instanceof TermVar) {
				TermVar var = (TermVar) value;
				jekpro.tools.term.AbstractTerm instanc = var.derefWrapped();
				if (instanc == var) {
					/* b b= */TermVar.markBind(prolog);
					return true;
				} else if (unifyTerm(prolog, instanc, t2)) {
					return true;
				}
			} else if (value instanceof TermVariable) {
				TermVariable x = (TermVariable) value;
				TermVar var = x.getVar();
				jekpro.tools.term.AbstractTerm instanc = var.derefWrapped();
				if (instanc == var) {
					/* b b= */TermVar.markBind(prolog);
					return true;
				} else if (unifyTerm(prolog, instanc, t2)) {
					return true;
				}
			}

			//
			if (t2 instanceof TermVar) {
				TermVar var = (TermVar) t2;
				jekpro.tools.term.AbstractTerm instanc = var.derefWrapped();
				if (instanc == var) {
					/* b b= */TermVar.markBind(prolog);
					return true;
				} else if (unifyTerm(prolog, instanc, value)) {
					return true;
				}
			} else if (t2 instanceof TermVariable) {
				TermVariable x = (TermVariable) t2;
				TermVar var = x.getVar();
				jekpro.tools.term.AbstractTerm instanc = var.derefWrapped();
				if (instanc == var) {
					/* b b= */TermVar.markBind(prolog);
					return true;
				} else if (unifyTerm(prolog, instanc, value)) {
					return true;
				}
			}

			// FIXME occurss check

			//
			else if (unifyTerm(prolog, value, t2)) {
				return true;
			}
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return false;
	}

	public final int compareTo(PrologTerm o) {
		JekejekePrologTerm ot = (JekejekePrologTerm) o;
		Object alpha = value.getSkel();
		Object beta = ot.value.getSkel();

		if (alpha instanceof Number && beta instanceof Number) {

			Number n1 = (Number) alpha;
			Number n2 = (Number) beta;

			if (n1.doubleValue() < n2.doubleValue()) {
				return -1;
			} else if (n1.doubleValue() > n2.doubleValue()) {
				return 1;
			}
			return 0;

		}

		int result = AbstractSkel.f(alpha, beta);
		if (result < 0) {
			return -1;
		} else if (result > 1) {
			return 1;
		}
		return 0;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		// Term not implement hashCode()
		result = prime * result + ((value == null) ? 0 : value.toString().hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof JekejekePrologTerm))
			return false;
		JekejekePrologTerm other = (JekejekePrologTerm) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (value.toString().equals(other.value.toString())) {
			return true;
		} else if (!unify(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + value + "";
	}

}
