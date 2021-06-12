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
