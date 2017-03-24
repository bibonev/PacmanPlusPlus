package main.java.gamelogic.domain;

/**
 * Represent an item from the inventory
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */
public abstract class Ability {

	private String name;
	protected Player owner;
	private int cooldown;
	private int shieldValue = 0;

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
	public Player getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of the item.
	 *
	 * @param owner
	 */
	public void setOwner(final Player owner) {
		this.owner = owner;
	}

	/**
	 * Gets the shield value of the item.
	 *
	 * @return the owner
	 */
	public int getShieldValue() {
		return shieldValue;
	}

	/**
	 * Sets the shieldValue of the item.
	 *
	 * @param shieldValue
	 */
	public void setShieldValue(int shieldValue) {
		this.shieldValue = shieldValue;
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

	public abstract boolean incrementCooldown();

	public abstract void reduceShieldValue();
}
