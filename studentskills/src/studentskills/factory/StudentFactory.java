package studentskills.factory;

import java.util.Map;

import studentskills.tree.StudentRecord;
import studentskills.tree.StudentRecord.Keys;

/**
 * A factory method to construct an instance of {@link StudentRecord}. See the
 * {@link #create(Map)} method.
 * 
 * @author Harshit Vadodaria
 *
 */
public class StudentFactory {

	/**
	 * This factory method creates an instance of {@link StudentRecord} based on the
	 * parameters {@code Map} provided as parameter.
	 * 
	 * @param params Map<Keys, Object> : parameters required to construct the
	 *               StudentRecord. Must contain all required Keys defined in the
	 *               {@link StudentRecord.Keys} enum
	 * @return An instance of StudentRecord based on {@code params}
	 */
	public StudentRecord create(Map<Keys, Object> params) {
		if (!params.containsKey(Keys.B_NUMBER))
			throw new RuntimeException(
					Keys.B_NUMBER.name() + " not found in params. It is mandatory to create a StudentRecord");

		StudentRecord student = new StudentRecord((int) params.get(Keys.B_NUMBER));
		for (Keys key : Keys.values())
			key.setParam(student, params);

		return student;
	}

	public StudentRecord create(StudentRecord referenceRecord) throws CloneNotSupportedException {
		return referenceRecord.clone();
	}

}
