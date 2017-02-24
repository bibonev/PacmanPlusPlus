package teamproject.gamelogic.domain;
/**
 * Represent a player's inventory
 */

import java.util.Map;

public class Inventory {

	private Map<Item, Integer> items;

	public Inventory(final Map<Item, Integer> items) {
		this.items = items;
	}

	/**
	 * Fetch items in the inventory
	 * 
	 * @return map of items
	 */
	public Map<Item, Integer> getItems() {
		return items;
	}

	/**
	 * Update items in inventory
	 * 
	 * @param items
	 *            new map of items
	 */
	public void setItems(final Map<Item, Integer> items) {
		this.items = items;
	}

}
