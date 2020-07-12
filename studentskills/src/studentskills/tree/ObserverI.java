package studentskills.tree;

public interface ObserverI <Sub extends SubjectI<?>> {
	void update(Sub subject, StudentRecordAction action);
}
