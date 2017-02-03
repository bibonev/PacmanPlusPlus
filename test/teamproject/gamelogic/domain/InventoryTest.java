package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.gamelogic.random.Randoms;

public class InventoryTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Map<Item, Integer> items = new HashMap<Item, Integer>();
		items.put(Randoms.randomItem(), Randoms.randomInteger());

		// When
		final Inventory inventory = new Inventory(items);

		// Then
		assertThat(inventory.getItems(), Is.is(items));
	}

}
