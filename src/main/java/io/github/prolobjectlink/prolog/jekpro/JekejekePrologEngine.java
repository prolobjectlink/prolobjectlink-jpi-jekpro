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

import static io.github.prolobjectlink.prolog.PrologLogger.DONT_WORRY;
import static io.github.prolobjectlink.prolog.PrologLogger.IO;
import static io.github.prolobjectlink.prolog.PrologLogger.RUNTIME_ERROR;
import static io.github.prolobjectlink.prolog.jekpro.JekejekePrologList.EMPTY;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.github.prolobjectlink.prolog.AbstractEngine;
import io.github.prolobjectlink.prolog.ArrayIterator;
import io.github.prolobjectlink.prolog.Licenses;
import io.github.prolobjectlink.prolog.PrologClause;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologIndicator;
import io.github.prolobjectlink.prolog.PrologOperator;
import io.github.prolobjectlink.prolog.PrologProgram;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;
import jekpro.model.a.b;
import jekpro.model.a.n;
import jekpro.model.a.r;
import jekpro.model.b.m;
import jekpro.model.c.c;
import jekpro.model.c.e;
import jekpro.model.c.p;
import jekpro.model.d.k;
import jekpro.reference.bootload.ForeignEngine;
import jekpro.tools.call.CallIn;
import jekpro.tools.call.Interpreter;
import jekpro.tools.call.InterpreterException;
import jekpro.tools.call.InterpreterMessage;
import jekpro.tools.term.AbstractSkel;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.SkelAtom;
import jekpro.tools.term.SkelCompound;
import matula.util.data.MapEntry;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
public class JekejekePrologEngine extends AbstractEngine implements PrologEngine {

	final Interpreter prolog;
	private CallIn accessor;

	public JekejekePrologEngine(PrologProvider provider, Interpreter prolog) {
		super(provider);
		this.prolog = prolog;
	}

	@Override
	public void consult(String path) {
		AbstractTerm consult;
		try {
			consult = prolog.parseTerm("consult('" + path + "')", EMPTY);
			accessor = prolog.iterator(consult).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void consult(Reader reader) {
		AbstractTerm consult;
		try {
			consult = prolog.parseTerm(reader, EMPTY);
			accessor = prolog.iterator(consult).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void include(String path) {
		AbstractTerm include;
		try {
			include = prolog.parseTerm("include('" + path + "')", EMPTY);
			accessor = prolog.iterator(include).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void include(Reader reader) {
		AbstractTerm include;
		try {
			include = prolog.parseTerm(reader, EMPTY);
			accessor = prolog.iterator(include).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void persist(String path) {
		PrintWriter writer = null;
		List<AbstractSkel> l = listClauses();
		try {
			writer = new PrintWriter(path);
			for (AbstractSkel abstractSkel : l) {
				writer.println(abstractSkel);
			}
		} catch (IOException e) {
			getLogger().warn(getClass(), IO + path, e);
			getLogger().info(getClass(), DONT_WORRY + path);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	@Override
	public void abolish(String functor, int arity) {
		AbstractTerm abolish;
		try {
			String key = functor + "/" + arity;
			abolish = prolog.parseTerm("abolish(" + key + ")", EMPTY);
			accessor = prolog.iterator(abolish).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void asserta(String stringClause) {
		AbstractTerm asserta;
		try {
			asserta = prolog.parseTerm("asserta(" + stringClause + ")", EMPTY);
			accessor = prolog.iterator(asserta).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void asserta(PrologTerm term) {
		AbstractTerm asserta = fromTerm(term, AbstractTerm.class);
		try {
			asserta = prolog.parseTerm("asserta(" + asserta + ")", EMPTY);
			accessor = prolog.iterator(asserta).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void asserta(PrologTerm head, PrologTerm... body) {
		AbstractTerm asserta = fromTerm(head, body, AbstractTerm.class);
		try {
			asserta = prolog.parseTerm("asserta(" + asserta + ")", EMPTY);
			accessor = prolog.iterator(asserta).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void assertz(String stringClause) {
		AbstractTerm assertz;
		try {
			assertz = prolog.parseTerm("assertz(" + stringClause + ")", EMPTY);
			accessor = prolog.iterator(assertz).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void assertz(PrologTerm term) {
		AbstractTerm assertz = fromTerm(term, AbstractTerm.class);
		try {
			assertz = prolog.parseTerm("assertz(" + assertz + ")", EMPTY);
			accessor = prolog.iterator(assertz).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void assertz(PrologTerm head, PrologTerm... body) {
		AbstractTerm assertz = fromTerm(head, body, AbstractTerm.class);
		try {
			assertz = prolog.parseTerm("assertz(" + assertz + ")", EMPTY);
			accessor = prolog.iterator(assertz).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public boolean clause(String stringClause) {
		AbstractTerm clause;
		try {
			clause = prolog.parseTerm("clause(" + stringClause + ")", EMPTY);
			prolog.iterator(clause).next().close();
			CallIn callin = prolog.iterator(clause);
			if (callin.hasNext()) {
				return true;
			}
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
		return false;
	}

	@Override
	public boolean clause(PrologTerm term) {
		AbstractTerm clause = fromTerm(term, AbstractTerm.class);
		try {
			clause = prolog.parseTerm("clause(" + clause + ")", EMPTY);
			prolog.iterator(clause).next().close();
			CallIn callin = prolog.iterator(clause);
			if (callin.hasNext()) {
				return true;
			}
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
		return false;
	}

	@Override
	public boolean clause(PrologTerm head, PrologTerm... body) {
		AbstractTerm clause = fromTerm(head, body, AbstractTerm.class);
		try {
			clause = prolog.parseTerm("clause(" + clause + ")", EMPTY);
			prolog.iterator(clause).next().close();
			CallIn callin = prolog.iterator(clause);
			if (callin.hasNext()) {
				return true;
			}
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
		return false;
	}

	@Override
	public void retract(String stringClause) {
		AbstractTerm retract;
		try {
			retract = prolog.parseTerm("retract(" + stringClause + ")", EMPTY);
			accessor = prolog.iterator(retract).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void retract(PrologTerm term) {
		AbstractTerm retract = fromTerm(term, AbstractTerm.class);
		try {
			retract = prolog.parseTerm("retract(" + retract + ")", EMPTY);
			accessor = prolog.iterator(retract).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public void retract(PrologTerm head, PrologTerm... body) {
		AbstractTerm retract = fromTerm(head, body, AbstractTerm.class);
		try {
			retract = prolog.parseTerm("retract(" + retract + ")", EMPTY);
			accessor = prolog.iterator(retract).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public PrologQuery query(String query) {
		return new JekejekePrologQuery(this, query);
	}

	@Override
	public PrologQuery query(PrologTerm goal) {
		return query("" + goal + ".");
	}

	public final PrologQuery query(PrologTerm[] terms) {
		Iterator<PrologTerm> i = new ArrayIterator<PrologTerm>(terms);
		StringBuilder buffer = new StringBuilder();
		while (i.hasNext()) {
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(',');
			}
		}
		buffer.append(".");
		return query("" + buffer + "");
	}

	public final PrologQuery query(PrologTerm term, PrologTerm... terms) {
		Iterator<PrologTerm> i = new ArrayIterator<PrologTerm>(terms);
		StringBuilder buffer = new StringBuilder();
		buffer.append("" + term + "");
		while (i.hasNext()) {
			buffer.append(',');
			buffer.append(i.next());
		}
		buffer.append(".");
		return query("" + buffer + "");
	}

	@Override
	public void operator(int priority, String specifier, String operator) {
		AbstractTerm op;
		try {
			String definition = priority + "," + specifier + "," + operator;
			op = prolog.parseTerm("op(" + definition + ")", EMPTY);
			accessor = prolog.iterator(op).next();
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
	}

	@Override
	public boolean currentPredicate(String functor, int arity) {
		AbstractTerm predicate;
		try {
			String key = functor + "/" + arity;
			predicate = prolog.parseTerm("current_predicate(" + key + ")", EMPTY);
			prolog.iterator(predicate).next().close();
			CallIn callin = prolog.iterator(predicate);
			if (callin.hasNext()) {
				return true;
			}
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
		return false;
	}

	@Override
	public boolean currentOperator(int priority, String specifier, String operator) {
		AbstractTerm op;
		try {
			String definition = priority + "," + specifier + "," + operator;
			op = prolog.parseTerm("current_op(" + definition + ")", EMPTY);
			prolog.iterator(op).next().close();
			CallIn callin = prolog.iterator(op);
			if (callin.hasNext()) {
				return true;
			}
		} catch (InterpreterMessage e1) {
			getLogger().error(getClass(), IO, e1);
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), IO, e1);
		}
		return false;
	}

	@Override
	public Set<PrologOperator> currentOperators() {
		Set<PrologOperator> operators = new HashSet<PrologOperator>();
		p store = (p) prolog.getKnowledgebase().getStore();
		MapEntry<String, c>[] source = store.aj();
		for (MapEntry<String, c> mapEntry : source) {
			k[] opsinv = mapEntry.value.snapshotOpersInv();
			for (k op : opsinv) {
				String x = null;
				int level = op.getLevel();
				String bits = "" + op.getBits() + "";
				String key = op.getKey();
				if (bits.equals("512") || bits.equals("514")) {
					x = "fx";
				} else if (bits.equals("513") || bits.equals("529") || bits.equals("561")) {
					x = "xfy";
				} else if (bits.equals("515")) {
					x = "xfx";
				}
				operators.add(new JekejekePrologOperator(level, x, key));
			}
		}
		return operators;
	}

	@Override
	public int getProgramSize() {
		int size = 0;
		n engine = (n) accessor.getInter().getEngine();
		SkelCompound dx = (SkelCompound) engine.dx;
		SkelAtom vt = dx.vT;
		e e = (e) vt.ou;
		MapEntry<r, Integer>[] cachepreds = e.cachepredsinv;
		for (MapEntry<r, Integer> mapEntry : cachepreds) {
			r r = mapEntry.key;
			b q = (b) r.eq;
			size += q.i(engine);
		}
		return size;
	}

	public PrologProgram getProgram() {
		return new JekejekePrologProgram(this);
	}

	@Override
	public Set<PrologIndicator> getPredicates() {
		List<AbstractSkel> l = listClauses();
		Set<PrologIndicator> s = new HashSet<PrologIndicator>(l.size());
		for (AbstractSkel abstractSkel : l) {
			if (abstractSkel instanceof SkelCompound) {
				SkelCompound cmp = (SkelCompound) abstractSkel;
				String functor = "" + cmp.vT + "";
				int arity = cmp.args.length;
				s.add(new JekejekePrologIndicator(functor, arity));
			}
		}
		return s;
	}

	@Override
	public Set<PrologIndicator> getBuiltIns() {
		Set<PrologIndicator> builtins = new HashSet<PrologIndicator>();
		p store = (p) prolog.getKnowledgebase().getStore();
		MapEntry<String, c>[] source = store.aj();
		for (MapEntry<String, c> mapEntry : source) {
			MapEntry<r, Integer>[] predsinv = mapEntry.value.snapshotPredsInv();
			for (MapEntry<r, Integer> predicate : predsinv) {
				String functor = predicate.key.getFun();
				int arity = predicate.key.getArity();
				builtins.add(new JekejekePrologIndicator(functor, arity));
			}
		}
		return builtins;
	}

	@Override
	public String getLicense() {
		return Licenses.NO_SPECIFIED;
	}

	@Override
	public String getVersion() {
		return ForeignEngine.sysPrologVersion(prolog);
	}

	public final String getVendor() {
		return ForeignEngine.sysPrologVendor(prolog);
	}

	@Override
	public String getName() {
		return "Jekejeke Prolog";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((prolog == null) ? 0 : prolog.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JekejekePrologEngine other = (JekejekePrologEngine) obj;
		if (prolog == null) {
			if (other.prolog != null)
				return false;
		} else if (!prolog.equals(other.prolog)) {
			return false;
		}
		return true;
	}

	@Override
	public Iterator<PrologClause> iterator() {
		Collection<PrologClause> cls = new LinkedList<PrologClause>();
		for (AbstractSkel clause : listClauses()) {
			if (clause instanceof SkelCompound) {
				SkelCompound compound = (SkelCompound) clause;
				if (compound.args.length == 2 && compound.vT.toString().equals(":-")) {
					PrologTerm head = toTerm(compound.args[0], PrologTerm.class);
					PrologTerm body = toTerm(compound.args[1], PrologTerm.class);
					if (body.getType() != PrologTermType.TRUE_TYPE) {
						cls.add(new JekejekePrologClause(provider, head, body, false, false, false));
					} else {
						cls.add(new JekejekePrologClause(provider, head, false, false, false));
					}
				}
			} else {
				cls.add(new JekejekePrologClause(provider, toTerm(clause, PrologTerm.class), false, false, false));
			}
		}
		return new PrologProgramIterator(cls);
	}

	@Override
	public void dispose() {
		try {
			accessor.close();
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
	}

	@Override
	public List<String> verify() {
		return Arrays.asList("OK");
	}

	private List<AbstractSkel> listClauses() {
		ArrayList<AbstractSkel> l = new ArrayList<AbstractSkel>();
		try {
			n engine = (n) accessor.getInter().getEngine();
			SkelCompound dx = (SkelCompound) engine.dx;
			SkelAtom vt = dx.vT;
			e e = (e) vt.ou;
			MapEntry<r, Integer>[] cachepreds = e.cachepredsinv;
			for (MapEntry<r, Integer> mapEntry : cachepreds) {
				r r = mapEntry.key;
				b definition = (b) r.eq;
				jekpro.model.d.b[] list = definition.h(engine);
				for (jekpro.model.d.b clause : list) {
					AbstractSkel predicate = (AbstractSkel) clause.nl;
					l.add(predicate);
				}
			}
		} catch (m e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return l;
	}

}
