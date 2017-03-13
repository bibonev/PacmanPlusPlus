package main.java.gamelogic.domain;

/**
 * Represent an item from the inventory
 *
 * @author Lyubomir Pashev
 *
 */
public abstract class Ability {

	private String name;
	protected Entity owner;
	private int cooldown;

	public Ability(final String name) {
		this.name = name;
	}

	/**
	 * Get the name of the item
	 *
	 * @return the name as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the cooldown.
	 */
	public int getCD() {
		return cooldown;
	}

	/**
	 * Set the cooldown.
	 */
	public void setCD(final int cd) {
		cooldown = cd;
	}

	/**
	 * Gets the owner of the item.
	 *
	 * @return the owner
	 */
	public Entity getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of the item.
	 *
	 * @param owner
	 */
	public void setOwner(final Entity owner) {
		this.owner = owner;
	}

	/**
	 * Update the name of the item
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * The item effect. Some items may have a delay.
	 *
	 * @throws InterruptedException
	 */
	public abstract void activate();

}
