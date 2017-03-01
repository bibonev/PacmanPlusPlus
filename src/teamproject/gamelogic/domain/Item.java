package teamproject.gamelogic.domain;

/**
 * Represent an item from the inventory
 *
 * @author aml
 *
 */
public class Item {

	private String name;
	private String description;

	public Item(final String name, final String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Fetch the name of the item
	 * 
	 * @return the name as a string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Fetch the description of the item
	 * 
	 * @return the description as a string
	 */
	public String getDescription() {
		return description;
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
	 * Update the description of the item
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

}
