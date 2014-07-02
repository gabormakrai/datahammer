package dh.algorithms.utils.normalization;

import dh.repository.Table;

public interface NormalizationMethod {
	public NormalizationParameter normalize(Table table, String columnName);
	public NormalizationParameter normalize(Table table, String columnName, NormalizationParameter parameter);
	public void deNormalize(Table table, String columnName, NormalizationParameter parameter);
}
