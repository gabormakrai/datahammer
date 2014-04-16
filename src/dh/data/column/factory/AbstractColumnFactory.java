package dh.data.column.factory;

import dh.data.column.AbstractDataColumn;

public abstract class AbstractColumnFactory {

	public static final int defaultBufferStep = 2048;

	protected String name;

	protected String role;

	protected int bufferStep;

	protected boolean nullAllowed;

	protected int size;

	boolean[] nullElements;

	public abstract void addElement(String element);

	public abstract AbstractDataColumn finishColumn();

	public AbstractColumnFactory(String name, String role, int bufferStep, boolean nullAllowed, boolean createNullArray) {
		this.bufferStep = bufferStep;
		this.nullAllowed = nullAllowed;
		if (createNullArray) {
			nullElements = new boolean[bufferStep];
		} else {
			nullElements = null;
		}
		this.name = name;
		this.role = role;
	}
}
