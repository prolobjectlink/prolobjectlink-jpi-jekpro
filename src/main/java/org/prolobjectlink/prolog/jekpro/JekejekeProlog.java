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

import static org.prolobjectlink.prolog.PrologLogger.RUNTIME_ERROR;
import static org.prolobjectlink.prolog.PrologLogger.SYNTAX_ERROR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.prolobjectlink.prolog.AbstractProvider;
import org.prolobjectlink.prolog.PrologAtom;
import org.prolobjectlink.prolog.PrologConverter;
import org.prolobjectlink.prolog.PrologDouble;
import org.prolobjectlink.prolog.PrologEngine;
import org.prolobjectlink.prolog.PrologFloat;
import org.prolobjectlink.prolog.PrologInteger;
import org.prolobjectlink.prolog.PrologJavaConverter;
import org.prolobjectlink.prolog.PrologList;
import org.prolobjectlink.prolog.PrologLogger;
import org.prolobjectlink.prolog.PrologLong;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologStructure;
import org.prolobjectlink.prolog.PrologTerm;
import org.prolobjectlink.prolog.PrologVariable;
import org.prolobjectlink.prolog.SyntaxError;

import jekpro.platform.headless.ToolkitLibrary;
import jekpro.tools.call.Interpreter;
import jekpro.tools.call.InterpreterException;
import jekpro.tools.call.InterpreterMessage;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.Knowledgebase;
import jekpro.tools.term.TermAtomic;
import jekpro.tools.term.TermCompound;
import matula.util.data.ListArray;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public class JekejekeProlog extends AbstractProvider implements PrologProvider {

	protected static final Map<String, String> varCache = new HashMap<String, String>();
	static final PrologLogger logger = new JekejekePrologLogger();
	private static final ListArray<String> listArray = new ListArray<String>();
	private static final Knowledgebase know = new Knowledgebase(ToolkitLibrary.DEFAULT);
	private static final Interpreter prolog = know.iterable();

	public JekejekeProlog() {
		super(new JekejekePrologConverter());
	}

	public JekejekeProlog(PrologConverter<Object> converter) {
		super(converter);
	}

	public boolean isCompliant() {
		return false;
	}

	public PrologTerm prologNil() {
		return new JekejekePrologNil(this);
	}

	public PrologTerm prologCut() {
		return new JekejekePrologCut(this);
	}

	public PrologTerm prologFail() {
		return new JekejekePrologFail(this);
	}

	public PrologTerm prologTrue() {
		return new JekejekePrologTrue(this);
	}

	public PrologTerm prologFalse() {
		return new JekejekePrologFalse(this);
	}

	public PrologTerm prologEmpty() {
		return new JekejekePrologEmpty(this);
	}

	public PrologTerm prologInclude(String file) {
		return newStructure("ensured_loaded", newAtom(file));
	}

	// engine

	public PrologEngine newEngine() {
		Interpreter jekpro = new Knowledgebase(ToolkitLibrary.DEFAULT).iterable();
		try {
			Knowledgebase.initKnowledgebase(jekpro, true);
			ToolkitLibrary.initPaths(jekpro, listArray);
			ToolkitLibrary.initCapas(jekpro, listArray);
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return new JekejekePrologEngine(this, jekpro);
	}

	public PrologEngine newEngine(String path) {
		PrologEngine engine = newEngine();
		engine.consult(path);
		return engine;
	}

	// parser helpers

	public PrologTerm parseTerm(String term) {
		AbstractTerm e = new TermAtomic("[]");
		try {
			Knowledgebase.initKnowledgebase(prolog, true);
			ToolkitLibrary.initPaths(prolog, listArray);
			ToolkitLibrary.initCapas(prolog, listArray);
			AbstractTerm t = prolog.parseTerm(term, e);
			return toTerm(t, PrologTerm.class);
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), SYNTAX_ERROR, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), SYNTAX_ERROR, e1);
		}
		throw new SyntaxError(term);
		// return toTerm(JekejekePrologUtil.parseTerm(term), PrologTerm.class)
	}

	public PrologTerm[] parseTerms(String stringTerms) {
		List<PrologTerm> list = new ArrayList<PrologTerm>();
		AbstractTerm e = new TermAtomic("[]");
		try {
			Knowledgebase.initKnowledgebase(prolog, true);
			ToolkitLibrary.initPaths(prolog, listArray);
			ToolkitLibrary.initCapas(prolog, listArray);
			AbstractTerm term = prolog.parseTerm(stringTerms, e);
			while (term instanceof TermCompound) {
				TermCompound struct = (TermCompound) term;
				if (struct.getFunctor().equals(",") && struct.getArity() == 2) {
					list.add(toTerm(struct.getArg(0), PrologTerm.class));
					term = (AbstractTerm) struct.getArg(1);
				} else {
					list.add(toTerm(term, PrologTerm.class));
				}
			}
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), SYNTAX_ERROR, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), SYNTAX_ERROR, e1);
		}
		return list.toArray(new PrologTerm[0]);
		// return toTermArray(JekejekePrologUtil.parseTerms(stringTerms),
		// PrologTerm[].class)
	}

	// terms

	public PrologAtom newAtom(String functor) {
		return new JekejekePrologAtom(this, functor);
	}

	public PrologFloat newFloat(Number value) {
		return new JekejekePrologFloat(this, value);
	}

	public PrologDouble newDouble(Number value) {
		return new JekejekePrologDouble(this, value);
	}

	public PrologInteger newInteger(Number value) {
		return new JekejekePrologInteger(this, value);
	}

	public PrologLong newLong(Number value) {
		return new JekejekePrologLong(this, value);
	}

	public PrologVariable newVariable(int position) {
		return new JekejekePrologVariable(this);
	}

	public PrologVariable newVariable(String name, int position) {
		return new JekejekePrologVariable(this, name);
	}

	public PrologList newList() {
		return new JekejekePrologEmpty(this);
	}

	public PrologList newList(PrologTerm[] arguments) {
		if (arguments != null && arguments.length > 0) {
			return new JekejekePrologList(this, arguments);
		}
		return new JekejekePrologEmpty(this);
	}

	public PrologList newList(PrologTerm head, PrologTerm tail) {
		return new JekejekePrologList(this, head, tail);
	}

	public PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
		return new JekejekePrologList(this, arguments, tail);
	}

	public PrologStructure newStructure(String functor, PrologTerm... arguments) {
		return new JekejekePrologStructure(this, functor, arguments);
	}

	public PrologTerm newStructure(PrologTerm left, String operator, PrologTerm right) {
		return new JekejekePrologStructure(this, left, operator, right);
	}

	public PrologTerm newReference(Object reference) {
		throw new UnsupportedOperationException("newReference(Object reference)");
	}

	public PrologJavaConverter getJavaConverter() {
		return new JekejekePrologJavaConverter(this);
	}

	public PrologLogger getLogger() {
		return logger;
	}

	@Override
	public String toString() {
		return "JekejekeProlog [converter=" + converter + "]";
	}

}
