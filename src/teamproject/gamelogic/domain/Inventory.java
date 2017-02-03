package teamproject.gamelogic.domain;

import java.util.Map;

public class Inventory {

	private Map<Item, Integer> items;

	public Inventory(final Map<Item, Integer> items) {
		this.items = items;
	}

	public Map<Item, Integer> getItems() {
		return items;
	}

	public void setItems(final Map<Item, Integer> items) {
		this.items = items;
	}

}
