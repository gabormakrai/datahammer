package dh.algorithms.utils.normalization;

import dh.data.column.base.DoubleDataColumn;
import dh.repository.Table;

public class MinMaxNormalizationMethod implements NormalizationMethod {
	
	@Override
	public NormalizationParameter normalize(Table table, String columnName) {
		
		DoubleDataColumn column = table.getColumn(columnName);
		double[] data = column.getData();
		boolean[] nullElement = column.getNullElements();
		
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		for (int i = 0; i < data.length; ++i) {
			if (nullElement != null && nullElement[i]) {
				continue;
			}
			if (data[i] > max) {
				max = data[i];
			}
			if (data[i] < min) {
				min = data[i];
			}
		}
		
		MinMaxNormalizationParameter parameter = new MinMaxNormalizationParameter(min, max);
		
		normalize(table, columnName, parameter);
				
		return parameter;
	}

	@Override
	public NormalizationParameter normalize(Table table, String columnName, NormalizationParameter parameter) {
		
		MinMaxNormalizationParameter minmaxParameter = null;
		
		if (!(parameter instanceof MinMaxNormalizationParameter)) {
			throw new RuntimeException("Wrong normalization parameter...");
		}
		
		minmaxParameter = (MinMaxNormalizationParameter)parameter;
		
		DoubleDataColumn column = table.getColumn(columnName);
		double[] data = column.getData();
		boolean[] nullElement = column.getNullElements();
		
		double min = minmaxParameter.getMinValue();
		double max = minmaxParameter.getMaxValue();
		
		for (int i = 0; i < data.length; ++i) {
			if (nullElement != null && nullElement[i]) {
				continue;
			}
			data[i] = (data[i] - min) / (max - min); 
		}
		
		return parameter;
	}

	@Override
	public void deNormalize(Table table, String columnName, NormalizationParameter parameter) {
		
		MinMaxNormalizationParameter minmaxParameter = null;
		
		if (!(parameter instanceof MinMaxNormalizationParameter)) {
			throw new RuntimeException("Wrong normalization parameter...");
		}
		
		minmaxParameter = (MinMaxNormalizationParameter)parameter;
		
		DoubleDataColumn column = table.getColumn(columnName);
		double[] data = column.getData();
		boolean[] nullElement = column.getNullElements();
		
		double min = minmaxParameter.getMinValue();
		double max = minmaxParameter.getMaxValue();
		
		for (int i = 0; i < data.length; ++i) {
			if (nullElement != null && nullElement[i]) {
				continue;
			}
			data[i] = (data[i] * (max - min)) + min; 
		}		
		
	}

}
