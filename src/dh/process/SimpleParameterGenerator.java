package dh.process;

import java.util.HashMap;

public class SimpleParameterGenerator extends ParameterGenerator {

	private boolean firstParameter;

	private HashMap<String, String> parameter;

	public SimpleParameterGenerator(HashMap<String, String> parameter) {
		this.firstParameter = true;
		this.parameter = parameter;
	}

	@Override
	public boolean hasNext() {
		if (firstParameter) {
			firstParameter = false;
			return true;
		}
		return false;
	}

	@Override
	public Parameter next() {
		return new Parameter(parameter);
	}

}
