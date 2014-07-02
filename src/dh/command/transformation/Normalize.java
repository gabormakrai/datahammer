package dh.command.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.algorithms.utils.normalization.NormalizationFactory;
import dh.algorithms.utils.normalization.NormalizationMethod;
import dh.algorithms.utils.normalization.NormalizationParameter;
import dh.command.AbstractCommand;
import dh.repository.Repository;
import dh.repository.Table;

public class Normalize extends AbstractCommand {
	
	private final static Logger logger = LoggerFactory.getLogger(Normalize.class);

	public Normalize(Repository repository) {
		super(repository);
	}
	
	public NormalizationParameter run(String tableName, String columnName, String methodName) {
		Table table = repository.getTable(tableName);
		NormalizationMethod method = NormalizationFactory.create(methodName);
		NormalizationParameter parameter = method.normalize(table, columnName);
		logger.info("Normalization is done on {}: {}", columnName, parameter);
		return parameter;
	}
	
	public void run(String tableName, String columnName, NormalizationParameter parameter) {
		Table table = repository.getTable(tableName);
		NormalizationMethod method = NormalizationFactory.create(parameter.getMethod());
		method.normalize(table, columnName, parameter);
		logger.info("Normalization applied on {}: {}", columnName, parameter);
	}
	

}
