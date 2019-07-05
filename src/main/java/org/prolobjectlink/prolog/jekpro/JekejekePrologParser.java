/*
 * #%L
 * prolobjectlink-jpi-ip
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

import static org.prolobjectlink.prolog.AbstractConverter.SIMPLE_ATOM_REGEX;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.prolobjectlink.prolog.PrologLogger;
import org.prolobjectlink.prolog.UnknownTermError;

import com.igormaznitsa.prologparser.DefaultParserContext;
import com.igormaznitsa.prologparser.GenericPrologParser;
import com.igormaznitsa.prologparser.ParserContext;
import com.igormaznitsa.prologparser.PrologParser;
import com.igormaznitsa.prologparser.terms.PrologAtom;
import com.igormaznitsa.prologparser.terms.PrologCompound;
import com.igormaznitsa.prologparser.terms.PrologFloat;
import com.igormaznitsa.prologparser.terms.PrologInt;
import com.igormaznitsa.prologparser.terms.PrologStruct;
import com.igormaznitsa.prologparser.terms.PrologTerm;
import com.igormaznitsa.prologparser.terms.PrologVar;
import com.igormaznitsa.prologparser.tokenizer.Op;

import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.TermAtomic;
import jekpro.tools.term.TermCompound;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologParser {

	private int varIndex;
	private static final PrologAtom op = new PrologAtom("op");
	private final HashMap<String, TermVariable> sharedPrologVariables;

	protected JekejekePrologParser() {
		sharedPrologVariables = new HashMap<String, TermVariable>();
	}

	AbstractTerm parseTerm(final String term) {
		AbstractTerm result = null;
		try {
			String temp = term;
			if (temp.lastIndexOf('.') != temp.length() - 1) {
				temp += ".";
			}
			Reader reader = new StringReader(temp);
			PrologParser parser = new GenericPrologParser(reader,
					new DefaultParserContext(ParserContext.FLAG_CURLY_BRACKETS, Op.SWI));
			if (parser.hasNext()) {
				PrologTerm t = parser.next();
				result = fromTerm(t);
			}
			parser.close();
		} catch (IOException e) {
			JekejekeProlog.logger.error(getClass(), PrologLogger.IO, e);
		}
		varIndex = 0;
		return result;
	}

	AbstractTerm[] parseTerms(AbstractTerm term) {
		return parseTerms("" + term + "");
	}

	AbstractTerm[] parseTerms(final String stringTerms) {
		AbstractTerm[] result = new AbstractTerm[0];
		LinkedList<AbstractTerm> list = new LinkedList<AbstractTerm>();
		try {
			String temp = stringTerms;
			if (temp.lastIndexOf('.') != temp.length() - 1) {
				temp += ".";
			}
			Reader reader = new StringReader(temp);
			PrologParser parser = new GenericPrologParser(reader,
					new DefaultParserContext(ParserContext.FLAG_CURLY_BRACKETS, Op.SWI));
			for (PrologTerm ptr : parser) {
				while (ptr instanceof PrologStruct) {
					PrologStruct struct = (PrologStruct) ptr;
					if (struct.getText().equals(",") && struct.getArity() == 2) {
						list.add(fromTerm(struct.getTermAt(0)));
						ptr = struct.getTermAt(1);
					} else {
						list.add(fromTerm(ptr));
						break;
					}
				}
			}
			parser.close();
		} catch (IOException e) {
			JekejekeProlog.logger.error(getClass(), PrologLogger.IO, e);
		}
		varIndex = 0;
		return list.toArray(result);
	}

	private AbstractTerm fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case ATOM:
			if (term instanceof PrologInt) {
				PrologInt i = (PrologInt) term;
				return new TermAtomic(i.getNumber());
			} else if (term instanceof PrologFloat) {
				PrologFloat f = (PrologFloat) term;
				return new TermAtomic(f.getNumber());
			} else {
				String functor = term.getText();
				if (functor.equals("nil")) {
					return new TermAtomic("nil");
				} else if (functor.equals("!")) {
					return new TermAtomic("!");
				} else if (functor.equals("fail")) {
					return new TermAtomic("fail");
				} else if (functor.equals("true")) {
					return new TermAtomic("true");
				} else if (functor.equals("false")) {
					return new TermAtomic("false");
				} else if (!functor.matches(SIMPLE_ATOM_REGEX)) {
					return new TermAtomic("'" + ((PrologAtom) term).getText() + "'");
				} else {
					return new TermAtomic(((PrologAtom) term).getText());
				}
			}
		case VAR:
			String name = ((PrologVar) term).getText();
//			int position = ((PrologVar) term).getPos();// FIXME CATCH THE VAR POSITION IN STRUCTURE ???
			TermVariable variable = sharedPrologVariables.get(name);
			if (variable == null) {
//				variable = new TermVariable(name, varIndex++);
				variable = new TermVariable(name);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case LIST:
			PrologCompound l = (PrologCompound) term;
			if (l.getArity() < 1) {
				return new TermAtomic("[]");
			}
			ArrayList<AbstractTerm> arrayList = new ArrayList<AbstractTerm>();
			while (l.getArity() > 0) {
				arrayList.add(fromTerm(l.getTermAt(0)));
				l = (PrologCompound) l.getTermAt(1);
			}
			return new TermCompound(".", arrayList.toArray());
		case STRUCT:
			PrologCompound compound = (PrologCompound) term;
			PrologTerm[] args = new PrologTerm[compound.getArity()];
			String functor = term.getFunctor().toString();
			for (int i = 0; i < args.length; i++) {
				args[i] = compound.getTermAt(i);
			}
			AbstractTerm[] arguments = fromTermArray(args);
			if (compound.getFunctor().equals(op) && compound.getArity() == 3) {
				String operator = compound.getTermAt(2).toString();
				return new TermCompound(operator, arguments);
			} else if (compound.getFunctor() instanceof Op) {
				String operator = compound.getFunctor().getText();
				return new TermCompound(operator, arguments);
			}
			return new TermCompound(functor, arguments);
		default:
			throw new UnknownTermError(term);
		}
	}

	private AbstractTerm[] fromTermArray(PrologTerm[] terms) {
		AbstractTerm[] prologTerms = new AbstractTerm[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

}
