package dh.process;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.converters.*;
import dh.command.discovery.*;
import dh.command.io.*;
import dh.command.marking.*;
import dh.command.ml.*;
import dh.command.nulls.NominalNulls;
import dh.command.tools.*;
import dh.command.transformation.*;
import dh.repository.Repository;

public class CommandLine {

	private static final Logger logger = LoggerFactory.getLogger(CommandLine.class);

	protected final Repository repository;

	public CommandLine() {
		this(null);
	}

	public CommandLine(String inputRepositoryFile) {

		logger.info("Start creating repository...");

		if (inputRepositoryFile == null) {
			this.repository = new Repository();
		} else {
			try {
				ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(inputRepositoryFile)));
				this.repository = (Repository) ois.readObject();
				ois.close();
			} catch (Exception e) {
				throw new RuntimeException("Problem during loading repository...", e);
			}
		}

		logger.info("Repository has been created...");

		convertColumnStringToBoolean = new ConvertColumnStringToBoolean(repository);
		convertColumnStringToNominal = new ConvertColumnStringToNominal(repository);
		explodeNominal = new ExplodeNominal(repository);
		keepLabels = new KeepLabels(repository);
		keepTopLabels = new KeepTopLabels(repository);
		binning = new Binning(repository);

		createNominalStat = new CreateNominalStat(repository);
		showTables = new ShowTables(repository);
		statNominal = new StatNominal(repository);
		statTable = new StatTable(repository);

		generateRandomData = new GenerateRandomData(repository);
		loadCSV = new LoadCSV(repository);
		loadIris = new LoadIris(repository);
		writeCSV = new WriteCSV(repository);

		homogenMarking = new HomogenMarking(repository);
		xValidationMarking = new XValidationMarking(repository);

		learnerModel = new LearnerModel(repository);
		modelApply = new ModelApply(repository);
		modelEvaluation = new ModelEvaluation(repository);

		nominalNulls = new NominalNulls(repository);

		addClassificationWeightColumn = new AddClassificationWeightColumn(repository);
		addWeightColumn = new AddWeightColumn(repository);

		// addColumn = new AddColumn(repository);
		copyTable = new CopyTable(repository);
		// filterRows = new FilterRows(repository);
		// modifyColumn = new ModifyColumn(repository);
		removeColumn = new RemoveColumn(repository);
		removeTable = new RemoveTable(repository);
		renameColumn = new RenameColumn(repository);
		setRole = new SetRole(repository);
		sort = new Sort(repository);
	}

	final protected ConvertColumnStringToBoolean convertColumnStringToBoolean;
	final protected ConvertColumnStringToNominal convertColumnStringToNominal;
	final protected ExplodeNominal explodeNominal;
	final protected KeepLabels keepLabels;
	final protected KeepTopLabels keepTopLabels;
	final protected Binning binning;

	final protected CreateNominalStat createNominalStat;
	final protected ShowTables showTables;
	final protected StatNominal statNominal;
	final protected StatTable statTable;

	final protected GenerateRandomData generateRandomData;
	final protected LoadCSV loadCSV;
	final protected LoadIris loadIris;
	final protected WriteCSV writeCSV;

	final protected HomogenMarking homogenMarking;
	final protected XValidationMarking xValidationMarking;

	final protected LearnerModel learnerModel;
	final protected ModelApply modelApply;
	final protected ModelEvaluation modelEvaluation;

	final protected NominalNulls nominalNulls;

	final protected AddClassificationWeightColumn addClassificationWeightColumn;
	final protected AddWeightColumn addWeightColumn;

	// final protected AddColumn addColumn;
	final protected CopyTable copyTable;
	// final protected FilterRows filterRows;
	// final protected ModifyColumn modifyColumn;
	final protected RemoveColumn removeColumn;
	final protected RemoveTable removeTable;
	final protected RenameColumn renameColumn;
	final protected SetRole setRole;
	final protected Sort sort;

	protected void saveRepo(String fileName) {

		logger.info("Start saving repository...");

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(fileName)));
			oos.writeObject(repository);
			oos.close();
		} catch (IOException e) {
			throw new RuntimeException("Problem during saving the repository...", e);
		}

		logger.info("Repository has been saved...");

	}
}
