package studentskills.tree;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StudentRecord implements Cloneable, ObserverI<StudentRecord>, SubjectI<StudentRecord> {

	public StudentRecord(double bNumber) {
		this.bNumber = bNumber;
	}
	
	protected final UUID uuid = UUID.randomUUID();

	protected final double bNumber;
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

	public UUID getUuid() {
		return uuid;
	}

	public double getbNumber() {
		return bNumber;
	}

	public Set<String> getSkills() {
		return skills;
	}

	

	protected final Set<ObserverI<StudentRecord>> observers = new HashSet<>();


	@Override
	public void notifyObservers(Action action) {
		for (ObserverI<StudentRecord> observer : this.observers)
			observer.update(this, action);
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof StudentRecord) && ((StudentRecord) obj).uuid.equals(this.uuid));
	}

	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}

	@Override
	public StudentRecord clone() throws CloneNotSupportedException {
		return (StudentRecord) super.clone();
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
	public void update(StudentRecord subject, Action action) {
		switch (action) {
		case MODIFY:
			this.setFirstName(subject.getFirstName());
			this.setLastName(subject.getLastName());
			this.setMajor(subject.getMajor());
			this.getSkills().clear();
		case INSERT:
			this.getSkills().addAll(subject.getSkills());
			break;
		default:
			throw new IllegalArgumentException("Invalid action ["+action+"]");
		}
	}
	
	public void replaceValue(String oldVal, String replacement) {
		if (this.getFirstName().equals(oldVal))
			this.setFirstName(replacement);
		else if (this.getLastName().equals(oldVal))
			this.setLastName(replacement);
		else if (this.getMajor().equals(oldVal))
			this.setMajor(replacement);
		else if (this.getSkills().remove(oldVal))
			this.getSkills().add(replacement);
		
	}
	
	public void addSkills(Set<String> skills) {
		this.skills.addAll(skills);
		this.notifyObservers(Action.INSERT);
	}

}
