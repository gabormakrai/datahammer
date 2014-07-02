package dh.command.transformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.algorithms.utils.normalization.NormalizationFactory;
import dh.algorithms.utils.normalization.NormalizationMethod;
import dh.algorithms.utils.normalization.NormalizationParameter;
import dh.command.AbstractCommand;
import dh.repository.Repository;
import dh.repository.Table;

public class DeNormalize extends AbstractCommand{
	
	private static final Logger logger = LoggerFactory.getLogger(DeNormalize.class);

	public DeNormalize(Repository repository) {
		super(repository);
	}

	public void run(String tableName, String columnName, NormalizationParameter parameter) {
		Table table = repository.getTable(tableName);
		NormalizationMethod method = NormalizationFactory.create(parameter.getMethod());
		method.deNormalize(table, columnName, parameter);
		logger.info("Normalization applied on {}: {}", columnName, parameter);		
	}
	
}
