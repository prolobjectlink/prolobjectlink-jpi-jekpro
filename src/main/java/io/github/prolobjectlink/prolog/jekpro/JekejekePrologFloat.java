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

import static io.github.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import jekpro.tools.term.TermAtomic;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JekejekePrologFloat extends JekejekePrologTerm implements PrologFloat {

	JekejekePrologFloat(PrologProvider provider, Number value) {
		super(FLOAT_TYPE, provider, new TermAtomic(value.floatValue()));
	}

	public PrologInteger getPrologInteger() {
		return new JekejekePrologInteger(provider, getIntegerValue());
	}

	public PrologFloat getPrologFloat() {
		return new JekejekePrologFloat(provider, getFloatValue());
	}

	public PrologDouble getPrologDouble() {
		return new JekejekePrologDouble(provider, getDoubleValue());
	}

	public PrologLong getPrologLong() {
		return new JekejekePrologLong(provider, getLongValue());
	}

	public long getLongValue() {
		return ((Number) ((TermAtomic) value).getValue()).longValue();
	}

	public double getDoubleValue() {
		return ((Number) ((TermAtomic) value).getValue()).doubleValue();
	}

	public int getIntegerValue() {
		return ((Number) ((TermAtomic) value).getValue()).intValue();
	}

	public float getFloatValue() {
		return ((Number) ((TermAtomic) value).getValue()).floatValue();
	}

	public PrologTerm[] getArguments() {
		return new JekejekePrologFloat[0];
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
	}

}
