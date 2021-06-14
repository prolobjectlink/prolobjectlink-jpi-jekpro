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

import static io.github.prolobjectlink.prolog.PrologTermType.NIL_TYPE;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import jekpro.tools.term.TermAtomic;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologNil extends JekejekePrologTerm implements PrologTerm {

	JekejekePrologNil(PrologProvider provider) {
		super(NIL_TYPE, provider, new TermAtomic("nil"));
	}

	public PrologTerm[] getArguments() {
		return new PrologTerm[0];
	}

	public int getArity() {
		return 0;
	}

	public String getFunctor() {
		return "" + value + "";
	}

}
