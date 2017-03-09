package teamproject.gamelogic.domain;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.Is;
import org.junit.Test;

import teamproject.abilities.Ability;
import teamproject.gamelogic.random.Randoms;

public class InventoryTest {

	@Test
	public void shouldConstruct() {
		// Given
		final Map<Ability, Integer> items = new HashMap<Ability, Integer>();
		items.put(Randoms.randomItem(), Randoms.randomInteger());

		// When
		final SkillSet inventory = new SkillSet(items);

		// Then
		assertThat(inventory.getItems(), Is.is(items));
	}

}
