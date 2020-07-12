package studentskills.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ValidationHelper {

	public ValidationHelper() {
		this(false);
	}

	public ValidationHelper(boolean critical) {
		this.critical = critical;
	}

	private final boolean critical;

	public ValidationHelper critical() {
		return new ValidationHelper(true);
	}

	public boolean validateOnlyAlphaNumeric(String str) {
		return this.validateOnlyAlphaNumeric(str, "[" + str + "] contains non-alphanumeric characters");
	}

	public boolean validateNotEmpty(String str) {
		return this.validateNotEmpty(str, "Empty word found");
	}

	public boolean validateNotEmpty(String str, String message) {
		if (str == null || str.trim().isEmpty()) {
			if (this.critical)
				throw new ValidationException(message);
			return false;
		}
		return true;
	}

	public boolean validateOnlyAlphaNumeric(String str, String message) {
		if (str.matches(".*[^a-zA-Z0-9\\.].*")) {
			Logger.error("Validation Error: ", null, message);
			if (critical)
				throw new ValidationException(message);
			return false;
		}
		return true;
	}

//	public boolean validateFilename(String filename) {
//		for (String s : filename.split("\\.")) {
//			if (!validateOnlyAlphaNumeric(s, "File name (" + filename
//					+ ") should not contain special characters, it should only contain alpha-numeric characters"))
//				return false;
//		}
//		return true;
//	}

	public boolean validateFile(String filename) {
		if (!validateNotEmpty(filename, "Filename should not be empty"))
			return false;
		if (!Files.exists(Paths.get(filename))) {
			Logger.error("Validation Error: ", null, "File [" + Paths.get(filename).toAbsolutePath() + "] does not exist.");
			if (critical)
				throw new ValidationException("File [" + filename + "] does not exist.");
			return false;
		}
		return true;
	}

}

class ValidationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super("[VALIDATION FAILED] " + message);
	}

}