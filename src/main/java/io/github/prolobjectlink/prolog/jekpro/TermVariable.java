package io.github.prolobjectlink.prolog.jekpro;

import static io.github.prolobjectlink.prolog.jekpro.JekejekeProlog.varCache;

import jekpro.model.b.j;
import jekpro.tools.term.AbstractTerm;
import jekpro.tools.term.TermVar;

public class TermVariable extends AbstractTerm {

	private final String name;
	private final TermVar var;

	TermVariable() {
		this("_", new TermVar());
	}

	TermVariable(String name) {
		this(name, new TermVar());
	}

	TermVariable(String name, TermVar var) {
		varCache.put("" + var + "", name);
		this.name = name;
		this.var = var;
	}

	public boolean isAnonymous() {
		return name.equals("_");
	}

	@Override
	public j getDisplay() {
		return var.getDisplay();
	}

	@Override
	public Object getSkel() {
		return var.getSkel();
	}

	public String getName() {
		return name;
	}

	public TermVar getVar() {
		return var;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermVariable other = (TermVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
