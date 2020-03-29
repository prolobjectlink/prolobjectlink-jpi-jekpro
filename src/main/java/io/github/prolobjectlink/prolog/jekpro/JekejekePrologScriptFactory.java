/*-
 * #%L
 * prolobjectlink-jpi-jekpro
 * %%
 * Copyright (C) 2012 - 2019 Prolobjectlink Project
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

import java.util.Iterator;

import javax.script.ScriptEngineFactory;

import io.github.prolobjectlink.prolog.ArrayIterator;
import io.github.prolobjectlink.prolog.PrologScriptEngineFactory;

public final class JekejekePrologScriptFactory extends PrologScriptEngineFactory implements ScriptEngineFactory {

	public JekejekePrologScriptFactory() {
		super(new JekejekeProlog().newEngine());
	}

	public String getMethodCallSyntax(String obj, String m, String... args) {
		StringBuilder result = new StringBuilder();
		result.append(obj + " <- " + m);
		result.append('(');
		Iterator<String> i = new ArrayIterator<String>(args);
		if (i.hasNext()) {
			while (i.hasNext()) {
				result.append(i.next());
				if (i.hasNext()) {
					result.append(',');
				}
			}
		}
		result.append(')');
		return "" + result + "";
	}

}
