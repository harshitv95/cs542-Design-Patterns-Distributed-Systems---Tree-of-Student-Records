package studentskills.tree;

/**
 * A Generic interface declaring methods to be implemented by a Subject, that
 * notifies observers ({@link ObserverI}) its about its updates
 * 
 * @author Harshit Vadodaria
 *
 * @param <O> A class implementing the {@link ObserverI} interface
 */
public interface SubjectI<O extends ObserverI<? extends SubjectI<?>>> {
	/**
	 * Notify all observers
	 * 
	 * @param arg - Any argument that needs to be passed to the observers
	 */
	void notifyObservers(Object arg);

	void registerObserver(O observer);

	void unregisterObserver(O observer);

}
