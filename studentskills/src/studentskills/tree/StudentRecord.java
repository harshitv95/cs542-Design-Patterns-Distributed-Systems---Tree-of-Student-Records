package studentskills.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentRecord implements Cloneable, ObserverI<StudentRecord>, SubjectI<StudentRecord> {

	public StudentRecord(int bNumber) {
		this.bNumber = bNumber;
	}

	protected StudentRecord(StudentRecord record) {
		this.bNumber = record.bNumber;
		this.update(record, StudentRecordAction.MODIFY);
	}
	
	protected final int bNumber;
	protected final Set<String> skills = new HashSet<String>();
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
	public void notifyObservers(StudentRecordAction action) {
		for (ObserverI<StudentRecord> observer : this.observers)
			if (!this.equals(observer))
				observer.update(this, action);
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof StudentRecord) && ((StudentRecord) obj).getbNumber() == (this.getbNumber()));
	}

	@Override
	public int hashCode() {
		return this.getbNumber();
	}

	@Override
	public StudentRecord clone() throws CloneNotSupportedException {
		return new StudentRecord(this);
	}

	@Override
	public void registerObserver(StudentRecord observer) {
		if (observer != null && !observer.equals(this))
			this.observers.add(observer);
	}

	@Override
	public void unregisterObserver(StudentRecord observer) {
		if (observer != null)
			this.observers.remove(observer);
	}

	@Override
	public void update(StudentRecord subject, StudentRecordAction action) {
		switch (action) {
		case MODIFY:
			this.getSkills().clear();
		case INSERT:
			this.setFirstName(subject.getFirstName());
			this.setLastName(subject.getLastName());
			this.setMajor(subject.getMajor());
			this.getSkills().addAll(subject.getSkills());
			break;
		default:
			throw new IllegalArgumentException("Invalid action [" + action + "]");
		}
	}

	public void replaceValue(Object oldVal, Object replacement) {
		if (this.getFirstName().equals(oldVal))
			this.setFirstName((String) replacement);
		if (this.getLastName().equals(oldVal))
			this.setLastName((String) replacement);
		if (this.getMajor().equals(oldVal))
			this.setMajor((String) replacement);
		if (this.getSkills().remove(oldVal))
			this.getSkills().add((String) replacement);
	}
	
	public void replaceValues(Map<Keys, Object> values) {
		for (Keys key : Keys.values())
			if (!key.equals(Keys.SKILLS))
				key.setParam(this, values);
		this.addSkills((Set<String>) values.get(Keys.SKILLS));
		this.notifyObservers(StudentRecordAction.INSERT);
	}
	
	public void replaceValues(StudentRecord replaceRecord) {
		this.setFirstName(replaceRecord.getFirstName());
		this.setLastName(replaceRecord.getLastName());
		this.setMajor(replaceRecord.getMajor());
		this.setGpa(replaceRecord.getGpa());
		this.addSkills(replaceRecord.getSkills());
	}

	public void addSkills(Set<String> skills) {
		this.skills.addAll(skills);
	}

	@Override
	public String toString() {
		return "{B#: "+this.getbNumber()+", skills: "+this.getSkills()+"}";
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
		 * @param params Map<{@link Keys}, Object>
		 */
		public void setParam(StudentRecord student, Map<Keys, Object> params) {
			if (!params.containsKey(this))
				throw new RuntimeException("Could not find parameter " + this.name() + " in map");
			this.setParam(student, params.get(this));
		}
	}

}
