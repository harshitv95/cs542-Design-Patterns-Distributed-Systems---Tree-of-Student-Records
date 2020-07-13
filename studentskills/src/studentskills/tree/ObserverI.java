package studentskills.tree;

/**
 * An interface declaring methods to be implementing by an Observer of a
 * {@link SubjectI}
 * 
 * @author Harshit Vadodaria
 *
 * @param <S> A Class implementing the {@link SubjectI} interface
 */
public interface ObserverI<S extends SubjectI<? extends ObserverI<?>>> {
	/**
	 * Called by the Subject when changes are to be pushed to its registered
	 * observers. The Subject passes an instance of itself to the observers, so the
	 * observers can pull the required changes as per the event parameter
	 * 
	 * @param subject The instance of the calling subject itself
	 * @param arg     Any argument needed to be passed to the observers
	 */
	void update(S subject, Object arg);
}
