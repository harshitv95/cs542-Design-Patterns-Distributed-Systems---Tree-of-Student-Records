package studentskills.driver;

import java.io.IOException;
import java.util.Map;

import studentskills.factory.StudentFactory;
import studentskills.store.StoreHelperI;
import studentskills.tree.StudentRecord;
import studentskills.tree.StudentRecord.Keys;
import studentskills.tree.TreeHelper;
import studentskills.tree.factory.StudentTreeFactory;
import studentskills.util.FileProcessor;
import studentskills.util.Logger;
import studentskills.util.Logger.Level;
import studentskills.util.Results;
import studentskills.util.StudentRecordParser;
import studentskills.util.ValidationHelper;

public class Driver {
	public final static int REQUIRED_NUMBER_OF_CMDLINE_ARGS = 7;

	/**
	 * Hardcoding Replica count for now. A good design approach would be to also
	 * pass this value as a command line argument, or read from a config file. Open
	 * to changes in future versions.
	 * 
	 */
	public final static int NUMBER_OF_REPLICAS = 3;

	public static void main(String[] args) throws IOException {
		if ((args.length != REQUIRED_NUMBER_OF_CMDLINE_ARGS) || (args[0].equals("${input}"))
				|| (args[1].equals("${modify}")) || (args[2].equals("${out1}")) || (args[3].equals("${out2}"))
				|| (args[4].equals("${out3}")) || (args[5].equals("${error}")) || (args[6].equals("${debug}"))) {
			System.err.println("Error: Incorrect number of arguments. Program accepts "
					+ REQUIRED_NUMBER_OF_CMDLINE_ARGS + " arguments.");
			System.exit(0);
		}

		try {
			new Logger(Logger.Level.from(Integer.parseInt(args[6])), args[5], Level.ERROR);
		} catch (Exception e) {
			System.err.println("Failed to initialize logger");
			e.printStackTrace();
		}

		ValidationHelper validation = new ValidationHelper().critical();
		validation.validateFile(args[0]);
		validation.validateFile(args[1]);
		Logger.info("CmdLine args validation successful");

		// Declarations
		Results[] results = new Results[NUMBER_OF_REPLICAS];
		FileProcessor inputFile = null, modifyFile = null;
		StoreHelperI<StudentRecord> storeHelper;
		StudentRecordParser parser;

		try {
			// Initialization
			for (int i = 0; i < NUMBER_OF_REPLICAS; i++)
				results[i] = new Results(args[i + 2], true);
			inputFile = new FileProcessor(args[0]);
			modifyFile = new FileProcessor(args[1]);
			storeHelper = new TreeHelper(NUMBER_OF_REPLICAS, new StudentTreeFactory(), new StudentFactory());
			parser = new StudentRecordParser();

			Logger.info("Initialization of components successful");

			String line = null;
			int lineCount = 0;
			Map<Keys, Object> inputParams;

			while ((line = inputFile.poll()) != null) {
				lineCount++;
				inputParams = parser.parseStoreInput(line);
				if (line != null)
					storeHelper.store(inputParams);
			}
			if (lineCount == 0)
				throw new RuntimeException("Input file was empty, resulted in empty trees");
			Logger.info("Successfully processed [" + lineCount + "] lines from input file");
			line = null;
			Map<Object, Object> modifyParams;
			int storeId;
			lineCount = 0;

			while ((line = modifyFile.poll()) != null) {
				lineCount++;
				modifyParams = parser.parseStoreModify(line);
				if (line != null) {
					storeId = (int) modifyParams.remove("replicaId");
					storeHelper.modify(storeId, modifyParams);
				}
			}
			
			if (lineCount == 0)
				Logger.warn("Modify file was emoty, no modifications done to trees");
			Logger.info("Successfully processed [" + lineCount + "] lines from modify file");
			for (int i = 0; i < NUMBER_OF_REPLICAS; i++) {
				Logger.info("Now Printing Tree " + i);
				storeHelper.getStore(i).printAll(results[i]);
				Logger.info("Print Tree " + i + " done\n");
			}

			Logger.info("Excecution completed successfully");
		} catch (RuntimeException e) {
			Logger.error("Error in execution", e);
		} catch (IOException e) {
			Logger.error("Error in execution", e);
		} finally {
			Logger.debugLow("Closing Instances of file readers and writers");
			for (Results result : results)
				result.close();
			try {
				inputFile.close();
			} catch (IOException e) {
				Logger.error("Exception while attempting to close input file", e);
			}
			try {
				modifyFile.close();
			} catch (IOException e) {
				Logger.error("Exception while attempting to close modify file", e);
			}
		}

	}

}
