package studentskills.tree;

public interface SubjectI <Obs extends ObserverI<?>> {
	void notifyObservers(Action action);

	void registerObserver(Obs observer);

	void unregisterObserver(Obs observer);
	
}
