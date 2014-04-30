package dh.command.converters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dh.command.AbstractCommand;
import dh.data.column.AbstractDataColumn;
import dh.data.column.base.DoubleDataColumn;
import dh.data.column.base.FloatDataColumn;
import dh.data.column.base.IntegerDataColumn;
import dh.data.column.base.LongDataColumn;
import dh.data.column.special.NominalDataColumn;
import dh.repository.Repository;
import dh.repository.Table;

public class Binning extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Binning.class);

	public Binning(Repository repository) {
		super(repository);
	}

	private DistanceCalc getDistanceCalc(String inputTableName, AbstractDataColumn inputColumn) {
		if (inputColumn instanceof DoubleDataColumn) {
			return new DoubleDistanceCals(((DoubleDataColumn) inputColumn).getData());
		} else if (inputColumn instanceof FloatDataColumn) {
			return new FloatDistanceCals(((FloatDataColumn) inputColumn).getData());
		} else if (inputColumn instanceof LongDataColumn) {
			return new LongDistanceCals(((LongDataColumn) inputColumn).getData());
		} else if (inputColumn instanceof IntegerDataColumn) {
			return new IntegerDistanceCals(((IntegerDataColumn) inputColumn).getData());
		} else {
			throw new RuntimeException("Table " + inputTableName + "'s column " + inputColumn.getName() + " is not double/float/int/long column...");
		}
	}

	private void addNominalColumnToTable(Table table, int k, String outputColumnName, int[] data) {

		HashMap<String, Integer> mapping = new HashMap<>();
		HashMap<Integer, String> reverseMapping = new HashMap<>();

		for (int i = 0; i < k; ++i) {
			mapping.put("bin" + i, i);
			reverseMapping.put(i, "bin" + i);
		}

		NominalDataColumn newColumn = new NominalDataColumn();
		newColumn.setName(outputColumnName);
		newColumn.setRole("");
		newColumn.setData(data);
		newColumn.setMapping(mapping);
		newColumn.setReverseMapping(reverseMapping);

		table.getColumns().put(outputColumnName, newColumn);

	}

	public void runStatic(String inputTableName, String inputColumnName, String outputColumnName, double[] centers) {

		Table table = getTable(inputTableName);

		AbstractDataColumn inputColumn = table.getColumn(inputColumnName);

		checkTableDoesNotHaveColumn(table, outputColumnName);

		DistanceCalc dc = getDistanceCalc(inputTableName, inputColumn);

		boolean[] nullData = inputColumn.getNullElements();

		double[] distances = new double[centers.length];

		int[] data = new int[table.getSize()];

		findGroup(data, nullData, centers, dc, distances);

		addNominalColumnToTable(table, centers.length, outputColumnName, data);

		logger.info("StaticBinning is done, table: " + table.getName() + ", column: " + inputColumnName + ", outputColumn: " + outputColumnName + "...");
	}

	interface DistanceCalc {
		public void calculateDistance(double[] distance, double[] centers, int index);

		public double getNumber(int index);

		public int calculateUniqueItems();
	}

	static class DoubleDistanceCals implements DistanceCalc {
		double[] data;

		public DoubleDistanceCals(double[] data) {
			this.data = data;
		}

		@Override
		public void calculateDistance(double distance[], double centers[], int index) {
			for (int i = 0; i < centers.length; ++i) {
				distance[i] = Math.abs(data[index] - centers[i]);
			}
		}

		@Override
		public double getNumber(int index) {
			return data[index];
		}

		@Override
		public int calculateUniqueItems() {
			HashSet<Double> set = new HashSet<>();
			for (int i = 0; i < data.length; ++i) {
				set.add(data[i]);
			}
			return set.size();
		}
	}

	static class FloatDistanceCals implements DistanceCalc {
		float[] data;

		public FloatDistanceCals(float[] data) {
			this.data = data;
		}

		@Override
		public void calculateDistance(double distance[], double centers[], int index) {
			for (int i = 0; i < centers.length; ++i) {
				distance[i] = Math.abs(data[index] - centers[i]);
			}
		}

		@Override
		public double getNumber(int index) {
			return data[index];
		}

		@Override
		public int calculateUniqueItems() {
			HashSet<Float> set = new HashSet<>();
			for (int i = 0; i < data.length; ++i) {
				set.add(data[i]);
			}
			return set.size();
		}
	}

	static class IntegerDistanceCals implements DistanceCalc {
		int[] data;

		public IntegerDistanceCals(int[] data) {
			this.data = data;
		}

		@Override
		public void calculateDistance(double distance[], double centers[], int index) {
			for (int i = 0; i < centers.length; ++i) {
				distance[i] = Math.abs(data[index] - centers[i]);
			}
		}

		@Override
		public double getNumber(int index) {
			return data[index];
		}

		@Override
		public int calculateUniqueItems() {
			HashSet<Integer> set = new HashSet<>();
			for (int i = 0; i < data.length; ++i) {
				set.add(data[i]);
			}
			return set.size();
		}
	}

	static class LongDistanceCals implements DistanceCalc {
		long[] data;

		public LongDistanceCals(long[] data) {
			this.data = data;
		}

		@Override
		public void calculateDistance(double distance[], double centers[], int index) {
			for (int i = 0; i < centers.length; ++i) {
				distance[i] = Math.abs(data[index] - centers[i]);
			}
		}

		@Override
		public double getNumber(int index) {
			return data[index];
		}

		@Override
		public int calculateUniqueItems() {
			HashSet<Long> set = new HashSet<>();
			for (int i = 0; i < data.length; ++i) {
				set.add(data[i]);
			}
			return set.size();
		}
	}

	private double[] findCenters(int k, boolean[] nullData, Integer randomSeed, AbstractDataColumn inputColumn) {

		Random random = (randomSeed == null) ? new Random() : new Random(randomSeed);

		double[] centers = new double[k];

		// find random initial centers

		if (inputColumn instanceof DoubleDataColumn) {
			HashSet<Double> set = new HashSet<>();
			double[] data = ((DoubleDataColumn) inputColumn).getData();
			int i = 0;
			while (true) {
				if (i == centers.length) {
					break;
				}
				int index = random.nextInt(data.length);
				if ((nullData != null && nullData[index]) || set.contains(data[index])) {
					continue;
				}
				centers[i++] = data[index];
				set.add(data[index]);
			}
		} else if (inputColumn instanceof IntegerDataColumn) {
			HashSet<Integer> set = new HashSet<>();
			int[] data = ((IntegerDataColumn) inputColumn).getData();
			int i = 0;
			while (true) {
				if (i == centers.length) {
					break;
				}
				int index = random.nextInt(data.length);
				if ((nullData != null && nullData[index]) || set.contains(data[index])) {
					continue;
				}
				centers[i++] = data[index];
				set.add(data[index]);
			}
		} else if (inputColumn instanceof LongDataColumn) {
			HashSet<Long> set = new HashSet<>();
			long[] data = ((LongDataColumn) inputColumn).getData();
			int i = 0;
			while (true) {
				if (i == centers.length) {
					break;
				}
				int index = random.nextInt(data.length);
				if ((nullData != null && nullData[index]) || set.contains(data[index])) {
					continue;
				}
				centers[i++] = data[index];
				set.add(data[index]);
			}
		} else if (inputColumn instanceof FloatDataColumn) {
			HashSet<Float> set = new HashSet<>();
			float[] data = ((FloatDataColumn) inputColumn).getData();
			int i = 0;
			while (true) {
				if (i == centers.length) {
					break;
				}
				int index = random.nextInt(data.length);
				if ((nullData != null && nullData[index]) || set.contains(data[index])) {
					continue;
				}
				centers[i++] = data[index];
				set.add(data[index]);
			}
		}

		Arrays.sort(centers);

		logger.trace("Initial centers: {}", Arrays.toString(centers));

		return centers;
	}

	private double findGroup(int[] data, boolean[] nullData, double[] centers, DistanceCalc dc, double[] distances) {

		double accumulatedError = 0.0;

		if (nullData == null) {
			for (int i = 0; i < data.length; ++i) {
				dc.calculateDistance(distances, centers, i);
				double minDistance = Double.MAX_VALUE;
				for (int j = 0; j < centers.length; ++j) {
					if (distances[j] < minDistance) {
						minDistance = distances[j];
						data[i] = j;
					}
				}
				accumulatedError += minDistance;
			}
		} else {
			for (int i = 0; i < data.length; ++i) {
				if (nullData[i]) {
					data[i] = -1;
					continue;
				}
				dc.calculateDistance(distances, centers, i);
				double minDistance = Double.MAX_VALUE;
				for (int j = 0; j < centers.length; ++j) {
					if (distances[j] < minDistance) {
						minDistance = distances[j];
						data[i] = j;
					}
				}
				accumulatedError += minDistance;
			}
		}

		return accumulatedError;
	}

	private void updateCenters(int[] groups, DistanceCalc dc, boolean[] nullData, int[] centersCount, double[] centersValue, double[] centers) {

		for (int i = 0; i < centersCount.length; ++i) {
			centersCount[i] = 0;
			centersValue[i] = 0.0;
		}

		if (nullData == null) {
			for (int i = 0; i < groups.length; ++i) {
				++centersCount[groups[i]];
				centersValue[groups[i]] += dc.getNumber(i);
			}
		} else {
			for (int i = 0; i < groups.length; ++i) {
				if (nullData[i]) {
					continue;
				}
				++centersCount[groups[i]];
				centersValue[groups[i]] += dc.getNumber(i);
			}
		}

		for (int i = 0; i < centers.length; ++i) {
			if (centersCount[i] == 0) {
				throw new RuntimeException("Too much groups...");
			}
			centers[i] = centersValue[i] / centersCount[i];
		}
	}

	public double[] runDynamic(String inputTableName, String inputColumnName, String outputColumnName, int iterations, int k, Integer randomSeed) {

		Table table = getTable(inputTableName);

		AbstractDataColumn inputColumn = table.getColumn(inputColumnName);

		checkTableDoesNotHaveColumn(table, outputColumnName);

		DistanceCalc dc = getDistanceCalc(inputTableName, inputColumn);

		boolean[] nullData = inputColumn.getNullElements();

		int possibleMaxK = dc.calculateUniqueItems();

		if (k > possibleMaxK) {
			k = possibleMaxK;
		}

		double[] centers = findCenters(k, nullData, randomSeed, inputColumn);

		int[] data = new int[table.getSize()];

		double[] distances = new double[centers.length];

		int[] centersCount = new int[k];
		double[] centersValue = new double[k];

		for (int iter = 0; iter < iterations; ++iter) {

			findGroup(data, nullData, centers, dc, distances);

			updateCenters(data, dc, nullData, centersCount, centersValue, centers);

			logger.trace("" + iter + ". iteration centers: {}", Arrays.toString(centers));
		}

		// last update on groups
		double error = findGroup(data, nullData, centers, dc, distances);

		addNominalColumnToTable(table, k, outputColumnName, data);

		logger.info("DynamicBinning is done, table: " + table.getName() + ", column: " + inputColumnName + ", outputColumn: " + outputColumnName + ", error: " + error + "...");

		return centers;
	}

	public double[] runAdaptive(String inputTableName, String inputColumnName, String outputColumnName, int iterations, int minK, int maxK, Integer randomSeed) {

		Table table = getTable(inputTableName);

		AbstractDataColumn inputColumn = table.getColumn(inputColumnName);

		checkTableDoesNotHaveColumn(table, outputColumnName);

		DistanceCalc dc = getDistanceCalc(inputTableName, inputColumn);

		boolean[] nullData = inputColumn.getNullElements();

		int[] data = new int[table.getSize()];

		double[] bestCenters = null;
		double minError = Double.MAX_VALUE;

		int possibleMaxK = dc.calculateUniqueItems();

		if (minK > possibleMaxK) {
			minK = possibleMaxK;
		}
		if (maxK > possibleMaxK) {
			maxK = possibleMaxK;
		}

		for (int k = minK; k < maxK + 1; ++k) {
			double[] centers = findCenters(k, nullData, randomSeed, inputColumn);
			double[] distances = new double[centers.length];
			int[] centersCount = new int[k];
			double[] centersValue = new double[k];
			for (int iter = 0; iter < iterations; ++iter) {

				findGroup(data, nullData, centers, dc, distances);

				updateCenters(data, dc, nullData, centersCount, centersValue, centers);

				logger.trace("" + iter + ". iteration centers: {}", Arrays.toString(centers));
			}
			// last update on groups
			double error = findGroup(data, nullData, centers, dc, distances) / k;

			logger.trace("Binning with " + k + " centers: error: " + error);

			if (error < minError) {
				bestCenters = centers;
				minError = error;
			}
		}

		logger.info("Best (min)error: {}, k: {}, centers: {}", minError, bestCenters.length, Arrays.toString(bestCenters));

		addNominalColumnToTable(table, bestCenters.length, outputColumnName, data);

		logger.info("AdaptiveBinning is done, table: " + table.getName() + ", column: " + inputColumnName + ", outputColumn: " + outputColumnName + "...");

		return bestCenters;
	}

}
