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

import io.github.prolobjectlink.prolog.AbstractProgram;
import io.github.prolobjectlink.prolog.PrologClauses;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologProgram;

class JekejekePrologProgram extends AbstractProgram implements PrologProgram {

	JekejekePrologProgram(PrologEngine engine) {
		super(engine);
	}

	public PrologClauses newClauses(String functor, int arity) {
		return new JekejekePrologClauses(functor, arity);
	}

}
