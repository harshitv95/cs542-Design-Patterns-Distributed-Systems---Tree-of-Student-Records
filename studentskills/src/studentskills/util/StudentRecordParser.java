package studentskills.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import studentskills.tree.StudentRecord.Keys;

public class StudentRecordParser {

	private final Pattern inputLinePattern = Pattern
			.compile("([0-9]+):([a-zA-Z0-9]+),([a-zA-Z0-9]+),([0-9]+\\.{0,1}[0-9]*),([a-zA-Z0-9]+),(.+)");
	private final Pattern modifyLinePattern = Pattern.compile("([0-9]+),([0-9]+),([a-zA-Z0-9]*):([a-zA-Z0-9]*)");

	/**
	 * Processes input file line, validates its syntax, and returns a
	 * Map<{@link Keys}, Object> containing the parameters extracted from the line
	 * 
	 * @param line String line read from an input file
	 * @return Instance of Map<{@link Keys}, Object> containing params extracted
	 *         from line
	 */
	public Map<Keys, Object> parseStoreInput(String line) {
		Logger.debugLow("Processing new input line", line);
		if (line == null || line.trim().isEmpty()) {
			Logger.debugLow("Line from input file was empty, skipped processing");
			return null;
		}
		Matcher m = inputLinePattern.matcher(line);
		if (!m.matches()) {
			Logger.error("Invalid syntax for input line", null, line);
			throw new RuntimeException("Invalid syntax for input line :\n[" + line + "]");
		}
		Logger.debugMed("Input file line format validated, extracting params", line);

		Map<Keys, Object> params = new HashMap<>();
		try {
			params.put(Keys.B_NUMBER, Integer.parseInt(m.group(1)));
			params.put(Keys.FIRST_NAME, m.group(2));
			params.put(Keys.LAST_NAME, m.group(3));
			params.put(Keys.GPA, Double.parseDouble(m.group(4)));
			params.put(Keys.MAJOR, m.group(5));
			if (Arrays.asList(m.group(6).split(",")).size() > 10)
				throw new RuntimeException(
						"Number of skills for student [" + m.group(1) + "] was greater than 10. Expected 10 or less.");
			params.put(Keys.SKILLS, new HashSet<String>(Arrays.asList(m.group(6).split(","))));
		} catch (Exception e) {
			Logger.error("Failed to parse input file line", null, line, e.getMessage());
			throw new RuntimeException("Failed to parse input file line", e);
		}

		return params;
	}

	/**
	 * Processes modify file line, validates its syntax, and returns a
	 * Map<{@link Keys}, Object> containing the parameters extracted from the line
	 * 
	 * @param line String line read from a modify input file
	 * @return Instance of Map<{@link Keys}, Object> containing params extracted
	 *         from line
	 */
	public Map<Object, Object> parseStoreModify(String line) {
		Logger.debugLow("Processing new modify line", line);
		if (line == null || line.trim().isEmpty()) {
			Logger.debugLow("Line from modify file was empty, skipped processing");
			return null;
		}
		Matcher m = modifyLinePattern.matcher(line);
		if (!m.matches()) {
			Logger.error("Invalid syntax for modify line", null, line);
			throw new RuntimeException("Invalid syntax for modify line :\n[" + line + "]");
		}
		Logger.debugMed("Modify file line format validated, extracting params", line);

		Map<Object, Object> params = new HashMap<>();
		try {
			params.put("replicaId", Integer.parseInt(m.group(1)));
			params.put(Keys.B_NUMBER, Integer.parseInt(m.group(2)));
			params.put("replaceValue", m.group(3));
			params.put("replacement", m.group(4));
		} catch (Exception e) {
			Logger.error("Failed to parse modify file line", null, line, e.getMessage());
			throw new RuntimeException("Failed to parse modify file line", e);
		}

		return params;
	}

}
