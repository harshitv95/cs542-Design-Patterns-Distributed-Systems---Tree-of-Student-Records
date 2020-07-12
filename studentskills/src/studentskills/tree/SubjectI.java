package studentskills.tree;

public interface SubjectI <Obs extends ObserverI<?>> {
	void notifyObservers(StudentRecordAction action);

	void registerObserver(Obs observer);

	void unregisterObserver(Obs observer);
	
}
