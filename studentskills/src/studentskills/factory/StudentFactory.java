package studentskills.factory;

import java.util.Map;

import studentskills.tree.StudentRecord;
import studentskills.tree.StudentRecord.Keys;
import studentskills.util.Logger;

/**
 * A factory class to construct an instance of {@link StudentRecord}.
 * StudentRecord is constructed in any of the 2 ways:
 * <ul>
 * <li>{@link #create(Map)} method, and</li>
 * <li>{@link #create(StudentRecord)} method</li>
 * </ul>
 * 
 * @author Harshit Vadodaria
 *
 */
public class StudentFactory {

	/**
	 * This factory method creates an instance of {@link StudentRecord} based on the
	 * parameters {@code Map} provided as parameter. It accepts a Map containing all
	 * parameters required by a StudentRecord instance (Refer
	 * {@link StudentRecord.Keys})
	 * 
	 * @param params Map<Keys, Object> : parameters required to construct the
	 *               StudentRecord. Must contain all required Keys defined in the
	 *               {@link StudentRecord.Keys} enum
	 * @return An instance of StudentRecord based on {@code params}
	 */
	public StudentRecord create(Map<Keys, Object> params) {
		Logger.debugLow("Factory - Initializing new StudentRecord", params);
		if (!params.containsKey(Keys.B_NUMBER))
			throw new RuntimeException(
					Keys.B_NUMBER.name() + " not found in params. It is mandatory to create a StudentRecord");

		StudentRecord student = new StudentRecord((int) params.get(Keys.B_NUMBER));
		for (Keys key : Keys.values())
			key.setParam(student, params);

		return student;
	}

	/**
	 * This method makes use of the Prototype pattern. It accepts an existing
	 * instance of {@link StudentRecord}, and attempts to create a clone, and return
	 * it
	 * 
	 * @param referenceRecord Instance of {@link StudentRecord}
	 * @return Clone of {@code referenceRecord}
	 * @throws CloneNotSupportedException
	 */
	public StudentRecord create(StudentRecord referenceRecord) throws CloneNotSupportedException {
		if (referenceRecord == null) return null;
		Logger.debugLow("Cloning Prototype StudentRecord", referenceRecord);
		return referenceRecord.clone();
	}

}
