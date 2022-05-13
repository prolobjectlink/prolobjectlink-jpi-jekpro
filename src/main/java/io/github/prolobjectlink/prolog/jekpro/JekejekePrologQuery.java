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
import static io.github.prolobjectlink.prolog.jekpro.JekejekePrologList.EMPTY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import io.github.prolobjectlink.prolog.AbstractEngine;
import io.github.prolobjectlink.prolog.AbstractQuery;
import io.github.prolobjectlink.prolog.PrologError;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologTerm;
import jekpro.tools.call.CallIn;
import jekpro.tools.call.Interpreter;
import jekpro.tools.call.InterpreterException;
import jekpro.tools.call.InterpreterMessage;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.SkelCompound;
import jekpro.tools.term.SkelVar;
import jekpro.tools.term.TermCompound;
import jekpro.tools.term.TermVar;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologQuery extends AbstractQuery implements PrologQuery {

	private Interpreter prolog;
	private CallIn callIn;
	private String query;

	private final List<Object> variables = new ArrayList<Object>();
	private final Map<String, String> directNames = new LinkedHashMap<String, String>();
	private final Map<String, String> inverseNames = new LinkedHashMap<String, String>();

	private void enumerateVariables(List<Object> vector, Object term) {
		if (!(term instanceof TermVar) && (term instanceof TermCompound)) {
			TermCompound compound = (TermCompound) term;
			for (int i = 0; i < compound.getArity(); i++) {
				Object t = compound.getArg(i);
				enumerateVariables(vector, t);
			}
		} else if (!vector.contains(term)) {
			vector.add(term);
		}
	}

	public JekejekePrologQuery(AbstractEngine engine, String query) {
		super(engine);
		this.query = query;
		try {

			String delim = ",;() +-*/%=><";
			List<String> names = new ArrayList<String>();
			prolog = ((JekejekePrologEngine) engine).prolog;
			StringTokenizer tokenizer = new StringTokenizer(query, delim);
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (token.matches("[A-Z][A-Za-z0-9_]*") && !names.contains(token)) {
					names.add(token);
				}
			}

			AbstractTerm goal = prolog.parseTerm(query, EMPTY);
			SkelCompound compound = (SkelCompound) goal.getSkel();
			if (compound.vU instanceof SkelVar[]) {
				SkelVar[] vars = (SkelVar[]) compound.vU;
				for (int i = 0; i < vars.length; i++) {
					directNames.put(names.get(i), vars[i].toString());
					inverseNames.put(vars[i].toString(), names.get(i));
				}
			} else if (compound.vU instanceof SkelVar) {
				SkelVar var = (SkelVar) compound.vU;
				directNames.put(names.get(0), var.toString());
				inverseNames.put(var.toString(), names.get(0));
			}

			enumerateVariables(variables, goal);
			callIn = prolog.iterator(goal);
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			// getLogger().error(getClass(), RUNTIME_ERROR, e)
			// Error: syntax_error(cannot_start_term)
			// Error: syntax_error(end_of_clause_expected)
			// Error: syntax_error(ref_unreadable)
		}
	}

	@Override
	public boolean hasSolution() {
		try {
			if (callIn.hasNext()) {
				return true;
			}
		} catch (InterpreterException e1) {
			getLogger().error(getClass(), RUNTIME_ERROR, e1);
		}
		return false;
	}

	@Override
	public boolean hasMoreSolutions() {
		return hasSolution();
	}

	@Override
	public PrologTerm[] oneSolution() {
		try {
			if (hasSolution()) {
				int l = variables.size();
				PrologTerm[] one = new PrologTerm[l];
				for (int i = 0; i < variables.size(); i++) {
					Object object = variables.get(i);
					AbstractTerm variable = (AbstractTerm) object;
					String term = prolog.unparseTerm(variable, EMPTY);
					one[i] = getProvider().parseTerm(term);
				}
				return one;
			}
		} catch (NullPointerException e) {
			// do nothing
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return new PrologTerm[0];
	}

	@Override
	public Map<String, PrologTerm> oneVariablesSolution() {
		Map<String, PrologTerm> map = new HashMap<String, PrologTerm>();
		try {
			if (hasSolution()) {
				for (int i = 0; i < variables.size(); i++) {
					Object object = variables.get(i);
					AbstractTerm variable = (AbstractTerm) object;
					String term = prolog.unparseTerm(variable, EMPTY);
					String name = inverseNames.get(variable.toString());
					map.put(name, getProvider().parseTerm(term));
				}
			}
		} catch (NullPointerException e) {
			// do nothing
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return map;
	}

	@Override
	public PrologTerm[] nextSolution() {
		try {
			if (hasSolution()) {
				int l = variables.size();
				PrologTerm[] one = new PrologTerm[l];
				for (int i = 0; i < variables.size(); i++) {
					Object object = variables.get(i);
					AbstractTerm variable = (AbstractTerm) object;
					String term = prolog.unparseTerm(variable, EMPTY);
					one[i] = getProvider().parseTerm(term);
				}
				callIn.next();
				return one;
			}
		} catch (NullPointerException e) {
			// do nothing
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return new PrologTerm[0];
	}

	@Override
	public Map<String, PrologTerm> nextVariablesSolution() {
		Map<String, PrologTerm> map = new HashMap<String, PrologTerm>();
		try {
			if (hasSolution()) {
				for (int i = 0; i < variables.size(); i++) {
					Object object = variables.get(i);
					AbstractTerm variable = (AbstractTerm) object;
					String term = prolog.unparseTerm(variable, EMPTY);
					String name = inverseNames.get(variable.toString());
					map.put(name, getProvider().parseTerm(term));
				}
				callIn.next();
			}
		} catch (NullPointerException e) {
			// do nothing
		} catch (InterpreterMessage e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
		return map;
	}

	@Override
	public PrologTerm[][] nSolutions(int n) {
		if (n > 0) {
			// m:solutionSize
			int m = 0;
			int index = 0;
			ArrayList<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
			while (hasNext() && index < n) {
				PrologTerm[] solution = nextSolution();
				m = solution.length > m ? solution.length : m;
				index++;
				all.add(solution);
			}

			PrologTerm[][] allSolutions = new PrologTerm[n][m];
			for (int i = 0; i < n; i++) {
				PrologTerm[] solution = all.get(i);
				for (int j = 0; j < m && j < solution.length; j++) {
					allSolutions[i][j] = solution[j];
				}
			}
			return allSolutions;
		}
		throw new PrologError("Impossible find " + n + " solutions");
	}

	@Override
	public Map<String, PrologTerm>[] nVariablesSolutions(int n) {
		if (n > 0) {
			int index = 0;
			@SuppressWarnings("unchecked")
			Map<String, PrologTerm>[] solutionMaps = new Map[n];
			while (hasMoreSolutions() && index < n) {
				Map<String, PrologTerm> solutionMap = nextVariablesSolution();
				solutionMaps[index++] = solutionMap;
			}
			return solutionMaps;
		}
		throw new PrologError("Impossible find " + n + " solutions");
	}

	@Override
	public PrologTerm[][] allSolutions() {
		// n:solutionCount, m:solutionSize
		int n = 0;
		int m = 0;
		ArrayList<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
		while (hasMoreSolutions()) {
			PrologTerm[] solution = nextSolution();
			m = solution.length > m ? solution.length : m;
			n++;
			all.add(solution);
		}

		PrologTerm[][] allSolutions = new PrologTerm[n][m];
		for (int i = 0; i < n; i++) {
			PrologTerm[] solution = all.get(i);
			for (int j = 0; j < m; j++) {
				allSolutions[i][j] = solution[j];
			}
		}
		return allSolutions;
	}

	@Override
	public Map<String, PrologTerm>[] allVariablesSolutions() {
		return all().toArray(new Map[0]);
	}

	@Override
	public List<Map<String, PrologTerm>> all() {
		List<Map<String, PrologTerm>> all = new ArrayList<Map<String, PrologTerm>>();
		while (hasMoreSolutions()) {
			Map<String, PrologTerm> solution = nextVariablesSolution();
			all.add(solution);
		}
		return all;
	}

	@Override
	public String toString() {
		return query;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((query == null) ? 0 : query.hashCode());
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
		JekejekePrologQuery other = (JekejekePrologQuery) obj;
		if (query == null) {
			if (other.query != null)
				return false;
		} else if (!query.equals(other.query)) {
			return false;
		}
		return true;
	}

	@Override
	public void dispose() {
		variables.clear();
		directNames.clear();
		inverseNames.clear();
		try {
			callIn.close();
		} catch (InterpreterException e) {
			getLogger().error(getClass(), RUNTIME_ERROR, e);
		}
	}

}
