package main.java.event.listener;

/**
 * Represents objects which receive messages when the number of dots
 * that the local player has eaten changes.
 * 
 * @author Tom Galvin
 *
 */
public interface DotsEatenChangedListener {
	public void onDotsEatenChanged(int number);
}
