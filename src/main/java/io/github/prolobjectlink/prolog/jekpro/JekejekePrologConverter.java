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

import static io.github.prolobjectlink.prolog.PrologTermType.ATOM_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.CUT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.DOUBLE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FAIL_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FALSE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.INTEGER_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.LIST_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.LONG_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.NIL_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.TRUE_TYPE;
import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import java.util.ArrayList;
import java.util.List;

import io.github.prolobjectlink.prolog.AbstractConverter;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologLogger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;
import io.github.prolobjectlink.prolog.UnknownTermError;
import jekpro.model.c.c;
import jekpro.model.c.p;
import jekpro.model.d.k;
import jekpro.platform.headless.ToolkitLibrary;
import jekpro.tools.call.Interpreter;
import jekpro.tools.call.InterpreterException;
import jekpro.tools.call.InterpreterMessage;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.Knowledgebase;
import jekpro.tools.term.TermAtomic;
import jekpro.tools.term.TermCompound;
import jekpro.tools.term.TermVar;
import matula.util.data.ListArray;
import matula.util.data.MapEntry;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologConverter extends AbstractConverter<Object> implements PrologConverter<Object> {

	private static ListArray<String> listArray = new ListArray<String>();
	private static Knowledgebase know = new Knowledgebase(ToolkitLibrary.DEFAULT);
	private static Interpreter prolog = know.iterable();

	public JekejekePrologConverter() {
		try {
			Knowledgebase.initKnowledgebase(prolog, true);
			ToolkitLibrary.initPaths(prolog, listArray);
			ToolkitLibrary.initCapas(prolog, listArray);
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), PrologLogger.RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			getLogger().error(getClass(), PrologLogger.RUNTIME_ERROR, e);
		}
	}

	@Override
	public PrologTerm toTerm(Object prologTerm) {
		if (prologTerm.equals("nil")) {
			return new JekejekePrologNil(provider);
		} else if (prologTerm.equals("!")) {
			return new JekejekePrologCut(createProvider());
		} else if (prologTerm.equals("fail")) {
			return new JekejekePrologFail(provider);
		} else if (prologTerm.equals("true")) {
			return new JekejekePrologTrue(provider);
		} else if (prologTerm.equals("false")) {
			return new JekejekePrologFalse(provider);
		} else if (prologTerm.equals("[]")) {
			return new JekejekePrologEmpty(provider);
		} else if (prologTerm.equals(JekejekePrologList.EMPTY)) {
			return new JekejekePrologEmpty(provider);
		} else if (prologTerm instanceof Double) {
			return new JekejekePrologDouble(provider, (Number) prologTerm);
		} else if (prologTerm instanceof Float) {
			return new JekejekePrologFloat(provider, (Number) prologTerm);
		} else if (prologTerm instanceof Integer) {
			return new JekejekePrologInteger(provider, (Number) prologTerm);
		} else if (prologTerm instanceof Long) {
			return new JekejekePrologLong(provider, (Number) prologTerm);
		} else if (prologTerm instanceof String) {
			return new JekejekePrologAtom(provider, (String) prologTerm);
		} else if (prologTerm instanceof TermAtomic) {
			TermAtomic t = (TermAtomic) prologTerm;
			Object value = t.getValue();
			if (value.equals("nil")) {
				return new JekejekePrologNil(provider);
			} else if (value.equals("!")) {
				return new JekejekePrologCut(createProvider());
			} else if (value.equals("fail")) {
				return new JekejekePrologFail(provider);
			} else if (value.equals("true")) {
				return new JekejekePrologTrue(provider);
			} else if (value.equals("false")) {
				return new JekejekePrologFalse(provider);
			} else if (value.equals("[]")) {
				return new JekejekePrologEmpty(provider);
			} else if (prologTerm.equals(JekejekePrologList.EMPTY)) {
				return new JekejekePrologEmpty(provider);
			} else if (value instanceof Double) {
				return new JekejekePrologDouble(provider, (Number) value);
			} else if (value instanceof Float) {
				return new JekejekePrologFloat(provider, (Number) value);
			} else if (value instanceof Integer) {
				return new JekejekePrologInteger(provider, (Number) value);
			} else if (value instanceof Long) {
				return new JekejekePrologLong(provider, (Number) value);
			} else if (value instanceof String) {
				return new JekejekePrologAtom(provider, (String) value);
			}
		} else if (prologTerm instanceof TermVar) {
			// String name = ((TermVar) prologTerm).name()
			// String name = ((TermVariable) prologTerm).getName()
			String name = ((TermVar) prologTerm).toString();
			PrologVariable variable = sharedVariables.get(name);
			if (variable == null) {
				variable = new JekejekePrologVariable(provider, name);
				sharedVariables.put(variable.getName(), variable);
			}
			return variable;
		} else if (prologTerm instanceof TermCompound) {
			TermCompound compound = (TermCompound) prologTerm;
			String functor = compound.getFunctor();
			int arity = compound.getArity();
			if (arity == 2) {

				// list
				if (functor.equals(".")) {
					Object term = compound;
					List<PrologTerm> list = new ArrayList<PrologTerm>();
					while (term instanceof TermCompound) {
						TermCompound struct = (TermCompound) term;
						if (struct.getFunctor().equals(".") && struct.getArity() == 2) {
							list.add(toTerm(struct.getArg(0), PrologTerm.class));
							term = struct.getArg(1);
						} else {
							list.add(toTerm(term, PrologTerm.class));
						}
					}
					PrologTerm[] arguments = list.toArray(new PrologTerm[0]);
					return new JekejekePrologList(provider, arguments);
				}

				// expressions
				p store = (p) prolog.getKnowledgebase().getStore();
				MapEntry<String, c>[] source = store.aj();
				for (MapEntry<String, c> mapEntry : source) {
					k[] opsinv = mapEntry.value.snapshotOpersInv();
					for (k op : opsinv) {
						if (!functor.equals(".") && op.getKey().equals(functor)) {
							Object left = compound.getArg(0);
							Object right = compound.getArg(1);
							return new JekejekePrologStructure(provider, left, functor, right);
						}
					}
				}
			}

			// structure
			Object[] arguments = new Object[arity];
			for (int i = 0; i < arity; i++) {
				arguments[i] = compound.getArg(i);
			}
			return new JekejekePrologStructure(provider, functor, arguments);

		}

		throw new UnknownTermError(prologTerm);
	}

	@Override
	public AbstractTerm fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case NIL_TYPE:
			return new TermAtomic("nil");
		case CUT_TYPE:
			return new TermAtomic("!");
		case FAIL_TYPE:
			return new TermAtomic("fail");
		case TRUE_TYPE:
			return new TermAtomic("true");
		case FALSE_TYPE:
			return new TermAtomic("false");
		case ATOM_TYPE:
			return new TermAtomic(removeQuoted(((PrologAtom) term).getStringValue()));
		case FLOAT_TYPE:
			return new TermAtomic(((PrologFloat) term).getFloatValue());
		case INTEGER_TYPE:
			return new TermAtomic(((PrologInteger) term).getIntegerValue());
		case DOUBLE_TYPE:
			return new TermAtomic(((PrologDouble) term).getDoubleValue());
		case LONG_TYPE:
			return new TermAtomic(((PrologLong) term).getLongValue());
		case VARIABLE_TYPE:
			String name = ((PrologVariable) term).getName();
			// int position = ((PrologVariable) term).getPosition()
			Object variable = sharedPrologVariables.get(name);
			if (variable == null) {
				// variable = new TermVariable(name, position)
				variable = new TermVariable(name);
				// variable = new TermVar()
				sharedPrologVariables.put(name, variable);
			}
			return (AbstractTerm) variable;
		case LIST_TYPE:
			PrologTerm[] array = term.getArguments();
			AbstractTerm list = JekejekePrologList.EMPTY;
			for (int i = array.length - 1; i >= 0; --i) {
				list = new TermCompound(".", fromTerm(array[i]), list);
			}
			return list;
		case STRUCTURE_TYPE:
			String functor = term.getFunctor();
			PrologTerm[] terms = term.getArguments();
			Object[] arguments = fromTermArray(terms);
			return new TermCompound(functor, arguments);
		default:
			throw new UnknownTermError(term);
		}
	}

	public Object[] fromTermArray(PrologTerm[] terms) {
		Object[] prologTerms = new Object[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

	public AbstractTerm fromTerm(PrologTerm head, PrologTerm[] body) {
		AbstractTerm clauseHead = fromTerm(head);
		if (body != null && body.length > 0) {
			AbstractTerm clauseBody = fromTerm(body[body.length - 1]);
			for (int i = body.length - 2; i >= 0; --i) {
				clauseBody = new TermCompound(",", fromTerm(body[i]), clauseBody);
			}
			return new TermCompound(":-", clauseHead, clauseBody);
		}
		return clauseHead;
	}

	public PrologProvider createProvider() {
		return new JekejekeProlog(this);
	}

	@Override
	public String toString() {
		return "JekejekePrologConverter";
	}

}
