package studentskills.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import studentskills.util.Logger;

/**
 * A POJO to store Student Information like B# (Student ID), FirstName,
 * LastName, GPA, Major and student's skills. This StudentRecord is also an
 * observable and subject, since multiple replicas of a Student Record will
 * listen to each other's changes, and implement the same changes, to stay
 * consistent
 * 
 * @author Harshit Vadodaria
 *
 */
public class StudentRecord implements Cloneable, ObserverI<StudentRecord>, SubjectI<StudentRecord> {

	/**
	 * Differentiates each instance of StudentRecord from another. Used in method
	 * {@link #hashCode()}
	 */
	protected UUID uuid = UUID.randomUUID();

	/**
	 * @param bNumber int
	 */
	public StudentRecord(int bNumber) {
		Logger.debugHigh("StudentRecord public constructor called [" + bNumber + "]");
		this.bNumber = bNumber;
	}

	protected StudentRecord(StudentRecord record) {
		Logger.debugHigh("StudentRecord protected constructor called [" + record + "]");
		this.bNumber = record.bNumber;
		this.update(record, StudentRecordEvent.MODIFY);
	}

	protected final int bNumber;
	protected final Set<String> skills = new TreeSet<>();
	protected String firstName, lastName, major;
	protected double gpa;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public int getbNumber() {
		return bNumber;
	}

	public Set<String> getSkills() {
		return skills;
	}

	protected final Set<ObserverI<StudentRecord>> observers = new HashSet<>();

	@Override
	public void notifyObservers(Object arg) {
		Logger.debugLow("Notifying all Observers", arg);
		for (ObserverI<StudentRecord> observer : this.observers)
			observer.update(this, arg);
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof StudentRecord) && ((StudentRecord) obj).getbNumber() == (this.getbNumber()));
	}

	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}

	/**
	 * Clones the current instance of this class, and returns the clone. It ensures
	 * only required properties are cloned, and not the <b>Observers</b>
	 */
	@Override
	public StudentRecord clone() throws CloneNotSupportedException {
		return new StudentRecord(this);
	}

	@Override
	public void registerObserver(StudentRecord observer) {
		if (observer != null) {
			Logger.info("New Observer (" + observer.getbNumber() + ") registered for Student " + this.getbNumber());
			this.observers.add(observer);
		}
	}

	@Override
	public void unregisterObserver(StudentRecord observer) {
		if (observer != null)
			this.observers.remove(observer);
	}

	@Override
	public void update(StudentRecord subject, Object obj) {
		if (!(obj instanceof StudentRecordEvent)) {
			Logger.warn("Invalid argument received to update, instead of event", obj);
			throw new RuntimeException("Invalid argument received to update, instead of event: (" + obj + ")");
		}
		StudentRecordEvent event = (StudentRecordEvent) obj;
		Logger.debugLow("Received update from Subject StudentRecord for action " + event + "", subject);
		switch (event) {
		case MODIFY:
			this.getSkills().clear();
		case INSERT:
			this.setFirstName(subject.getFirstName());
			this.setLastName(subject.getLastName());
			this.setMajor(subject.getMajor());
			this.getSkills().addAll(subject.getSkills());
			break;
		default:
			throw new IllegalArgumentException("Invalid action [" + event + "]");
		}
	}

	public void replaceValue(Object oldVal, Object replacement) {
		if (oldVal == null || oldVal.equals("")) {
			Logger.warn("No value specified to be replaced", "replaceValue: \"\", replacement: " + replacement + "}");
			return;
		}
		if (replacement == null || replacement.equals("")) {
			Logger.warn("New replacement value not specified", "replaceValue: " + oldVal + ", replacement: \"\"}");
			return;
		}
		Logger.debugLow("Replacing " + oldVal + " in StudentRecord with " + replacement, this);
		boolean modified = false;
		if (modified = this.getFirstName().equals(oldVal)) {
			Logger.debugMed("Modifying first name of Student with ID [" + this.getbNumber() + "] from ["
					+ this.getFirstName() + "] to [" + replacement + "]");
			this.setFirstName((String) replacement);
		}
		if (modified = this.getLastName().equals(oldVal)) {
			Logger.debugMed("Modifying last name of Student with ID [" + this.getbNumber() + "] from ["
					+ this.getLastName() + "] to [" + replacement + "]");
			this.setLastName((String) replacement);
		}
		if (modified = this.getMajor().equals(oldVal)) {
			Logger.debugMed("Modifying major of Student with ID [" + this.getbNumber() + "] from [" + this.getMajor()
					+ "] to [" + replacement + "]");
			this.setMajor((String) replacement);
		}
		if (modified = this.getSkills().remove(oldVal)) {
			Logger.debugMed("Modifying a Skill of Student with ID [" + this.getbNumber() + "] from [" + oldVal
					+ "] to [" + replacement + "]");
			this.getSkills().add((String) replacement);
		}

		if (modified)
			this.notifyObservers(StudentRecordEvent.MODIFY);
	}

	public void replaceValues(Map<Keys, Object> values) {
		if (values == null)
			return;
		Logger.debugLow("Updating current values of StudentRecord with new ones",
				"{oldStudentRecord: " + this + ", newValues: " + values + "}");
		for (Keys key : Keys.values())
			if (!key.equals(Keys.SKILLS))
				key.setParam(this, values);
		this.addSkills((Set<String>) values.get(Keys.SKILLS));
		this.notifyObservers(StudentRecordEvent.INSERT);
	}

	public void replaceValues(StudentRecord replaceRecord) {
		if (replaceRecord == null)
			return;
		Logger.debugLow("Updating current values of StudentRecord with new ones",
				"{oldStudentRecord: " + this + ", newValues: " + replaceRecord + "}");
		this.setFirstName(replaceRecord.getFirstName());
		this.setLastName(replaceRecord.getLastName());
		this.setMajor(replaceRecord.getMajor());
		this.setGpa(replaceRecord.getGpa());
		this.addSkills(replaceRecord.getSkills());
		this.notifyObservers(StudentRecordEvent.INSERT);
	}

	public void addSkills(Set<String> skills) {
		if (skills == null)
			return;
		Logger.debugMed("Adding new skills " + skills + " to Student with ID [" + this.getbNumber()
				+ "] having existing skills: " + this.getSkills());
		this.skills.addAll(skills);
	}

	@Override
	public String toString() {
		return "{B#: " + this.getbNumber() + ", skills: " + this.getSkills() + "}";
	}

	/**
	 * An enum defining the minimum keys required in the params Map, to construct an
	 * instance of the parent {@link StudentRecord} class. The public
	 * {@link #setParam(StudentRecord, Map)} can be called to set the corresponding
	 * property of the student, represented by the enum's value
	 * 
	 * @author Harshit Vadodaria
	 *
	 */
	public static enum Keys {
		B_NUMBER {
			@Override
			protected void setParam(StudentRecord student, Object param) {
			}
		},
		FIRST_NAME {
			@Override
			protected void setParam(StudentRecord student, Object param) {
				student.setFirstName((String) param);
			}
		},
		LAST_NAME {
			@Override
			protected void setParam(StudentRecord student, Object param) {
				student.setLastName((String) param);
			}
		},
		MAJOR {
			@Override
			protected void setParam(StudentRecord student, Object param) {
				student.setMajor((String) param);
			}
		},
		GPA {
			@Override
			protected void setParam(StudentRecord student, Object param) {
				student.setGpa((double) param);
			}
		},
		SKILLS {
			@SuppressWarnings("unchecked")
			@Override
			protected void setParam(StudentRecord student, Object param) {
				if (!(param instanceof Collection<?>))
					throw new ClassCastException("Skills needs to have type Collection<String>, found type: ["
							+ param.getClass().getName() + "]");
				student.getSkills().addAll((Collection<String>) param);
			}
		},;

		protected abstract void setParam(StudentRecord student, Object param);

		/**
		 * Sets a property of {@code student}, by fetching the corresponding value from
		 * the {@code params} map, and casting it to the appropriate type required by
		 * the corresponding property of {@link StudentRecord}
		 * 
		 * @param student {@link StudentRecord}
		 * @param params  Map<{@link Keys}, Object>
		 */
		public void setParam(StudentRecord student, Map<Keys, Object> params) {
			if (!params.containsKey(this))
				throw new RuntimeException("Could not find parameter " + this.name() + " in map");
			this.setParam(student, params.get(this));
		}
	}

}
