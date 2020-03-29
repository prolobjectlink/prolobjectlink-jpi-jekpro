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
package io.github.prolobjectlink.prolog.jekpro;

import static io.github.prolobjectlink.prolog.jekpro.JekejekeProlog.varCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jekpro.tools.term.AbstractTerm;

/**
 * Util class for {@link JekeJekePrologTerm}
 * 
 * @author Jose Zalacain
 * @since 1.0
 *
 */
final class JekejekePrologUtil {

	static final Map<String, String> FUNCTORS = new HashMap<String, String>();

	static final AbstractTerm parseTerm(String term) {
		return new JekejekePrologParser().parseTerm(term);
	}

	static final AbstractTerm[] parseTerms(String stringTerms) {
		return new JekejekePrologParser().parseTerms(stringTerms);
	}

	/**
	 * Replace cached inter-prolog variables with real variable name
	 * 
	 * @param string with inter-prolog variables
	 * @return string with variable replacement
	 * @since 1.0
	 */
	static final String replace(String string) {
		for (Entry<String, String> entry : varCache.entrySet()) {
			if (string.contains(entry.getKey())) {
				string = string.replace(entry.getKey(), entry.getValue());
			}
		}
		return string;
	}

	private JekejekePrologUtil() {
	}

}
