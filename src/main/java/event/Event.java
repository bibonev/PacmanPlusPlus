package main.java.event;

import java.util.HashSet;
import java.util.function.BiConsumer;

/**
 * Represents an event that can be listened to and triggered.
 * 
 * @author Tom Galvin
 *
 * @param <TListener>
 *            The interface which will listen to this event.
 * @param <TEventArgs>
 *            The type of argument which will be passed to the event listener.
 */
public class Event<TListener, TEventArgs> {
	private HashSet<TListener> listeners;
	private BiConsumer<TListener, TEventArgs> trigger;

	/**
	 * Creates a new event with the given trigger function.
	 *
	 * @param trigger
	 *            A consumer function which calls the event listening function
	 *            on a listener, passing the event arguments to it.
	 */
	public Event(final BiConsumer<TListener, TEventArgs> trigger) {
		this.listeners = new HashSet<>();
		this.trigger = trigger;
	}

	/**
	 * Adds a listener to this event.
	 *
	 * @param listener
	 *            The listener to add to the event.
	 * @throws IllegalArgumentException
	 *             Thrown when the given listener object is already listening to
	 *             this event.
	 */
	public void addListener(final TListener listener) {
		if (!isListenedToBy(listener)) {
			listeners.add(listener);
		} else {
			throw new IllegalArgumentException("Listener already added to event.");
		}
	}

	/**
	 * Removes a listener from this event.
	 *
	 * @param listener
	 *            The listener to remove (ie. stop listening).
	 * @throws IllegalArgumentException
	 *             Thrown when the given listener is not listening to this
	 *             event, and so it can't stop listening (be removed).
	 */
	public void removeListener(final TListener listener) {
		if (isListenedToBy(listener)) {
			listeners.remove(listener);
		} else {
			throw new IllegalArgumentException("Listener cannot be removed because it isn't listening.");
		}
	}

	/**
	 * Removes all listeners from this event.
	 */
	public void clearListeners() {
		listeners.clear();
	}

	/**
	 * Determines whether the given listener object is listening to this event.
	 *
	 * @param listener
	 *            The listener to check,
	 * @return Returns {@code true} if {@code listener} is listening to this
	 *         event; {@code false} otherwise.
	 */
	public boolean isListenedToBy(final TListener listener) {
		return listeners.contains(listener);
	}

	/**
	 * Fires this event, triggering all of the event listeners which are
	 * listening to it. This will block until all of the event listeners have
	 * finished running, and the order in which the event listeners are
	 * triggered is unspecified.
	 *
	 * @param args
	 *            The arguments to pass to the event listeners.
	 */
	public void fire(final TEventArgs args) {
		for (final TListener listener : listeners) {
			trigger.accept(listener, args);
		}
	}
}
