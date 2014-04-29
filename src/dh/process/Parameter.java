package dh.process;

import java.util.HashMap;

public class Parameter {
	private HashMap<String, String> parameter = new HashMap<String, String>();

	public Parameter() {
	}

	public Parameter(HashMap<String, String> parameter) {
		this.parameter = parameter;
	}

	public HashMap<String, String> getParameter() {
		return parameter;
	}

}
