package dh.command.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.factory.AbstractColumnFactory;
import dh.data.column.factory.ColumnFactoryFactory;
import dh.repository.Repository;
import dh.repository.Table;

public class LoadCSV extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(LoadCSV.class);

	public LoadCSV(Repository repository) {
		super(repository);
	}

	public void run(String fileName, String tableName, String[] columns, String[] types, boolean[] nullAllowed, boolean skipFirstLine, char escapeChar, char quoteChar, char separatorChar, Integer bufferStepParameter) {

		if (repository.getTable(tableName) != null) {
			throw new RuntimeException("Repository has a table with name " + tableName + "...");
		}

		int bufferStep = AbstractColumnFactory.defaultBufferStep;
		if (bufferStepParameter != null) {
			bufferStep = bufferStepParameter;
		}

		File f = new File(fileName);

		if (!f.exists() || f.isDirectory()) {
			throw new RuntimeException("File not found: " + fileName);
		}

		int size = 0;

		AbstractColumnFactory[] factories = new AbstractColumnFactory[columns.length];

		try {

			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = "";

			for (int i = 0; i < factories.length; i++) {
				factories[i] = ColumnFactoryFactory.getFactory(columns[i], "", types[i], nullAllowed[i], bufferStep);
			}

			while ((line = br.readLine()) != null) {

				// empty row/line
				if (line.trim().equals("")) {
					continue;
				}

				if (skipFirstLine) {
					skipFirstLine = false;
					continue;
				}

				size++;

				ParserState state = ParserState.COLUMN_START;
				boolean quotedColumn = false;

				int columnNumber = 0;
				StringBuffer columnContent = new StringBuffer();
				boolean nullColumn = false;

				for (int i = 0; i < line.length(); i++) {

					char c = line.charAt(i);

					if (state == ParserState.COLUMN_START) {
						if (c == quoteChar) {
							quotedColumn = true;
							state = ParserState.COLUMN_CONTENT;
						} else if (c == separatorChar) {
							nullColumn = true;
							quotedColumn = false;
							state = ParserState.COLUMN_END;
						} else {
							quotedColumn = false;
							columnContent.append(c);
							state = ParserState.COLUMN_CONTENT;
						}
					} else if (state == ParserState.COLUMN_CONTENT) {

						if (!quotedColumn && c == separatorChar) {
							state = ParserState.COLUMN_END;
						} else if (!quotedColumn) {
							columnContent.append(c);
						} else if (quotedColumn && c == escapeChar) {
							state = ParserState.ESCAPE_CHAR;
						} else if (quotedColumn && c == quoteChar) {
							state = ParserState.COLUMN_END;
							continue;
						} else {
							columnContent.append(c);
						}
					} else if (state == ParserState.ESCAPE_CHAR) {
						if (c == quoteChar) {
							columnContent.append(quoteChar);
						} else if (c == escapeChar) {
							columnContent.append(escapeChar);
						} else {
							columnContent.append(escapeChar).append(c);
						}
						state = ParserState.COLUMN_CONTENT;
					}

					if (state == ParserState.COLUMN_END) {
						// end of the column
						if (nullColumn) {
							factories[columnNumber++].addElement(null);
						} else {
						    factories[columnNumber++].addElement(columnContent.toString());
						}
						nullColumn = false;
						quotedColumn = false;
						columnContent.setLength(0);
						state = ParserState.COLUMN_START;
					}
				}

				if (state == ParserState.COLUMN_CONTENT || state == ParserState.COLUMN_END) {
					factories[columnNumber++].addElement(columnContent.toString());
				}

			}

			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Problem with IO...", e);
		}

		Table table = new Table();
		table.setName(tableName);
		table.setSize(size);

		for (int i = 0; i < columns.length; i++) {
			AbstractDataColumn column = factories[i].finishColumn();
			table.getColumns().put(column.getName(), column);
		}

		repository.getTables().put(table.getName(), table);

		logger.info("Table " + tableName + " loaded with " + size + " row(s)...");
	}

	public enum ParserState {
		COLUMN_START, COLUMN_CONTENT, ESCAPE_CHAR, COLUMN_END
	}

}
