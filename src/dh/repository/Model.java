package dh.repository;

import java.io.Serializable;

public abstract class Model implements Serializable {

	private static final long serialVersionUID = 1L;

	public abstract void apply(Table table);

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Model(String name) {
		this.name = name;
	}
}
