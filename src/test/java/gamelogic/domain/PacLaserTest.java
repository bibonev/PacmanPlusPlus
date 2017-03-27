package test.java.gamelogic.domain;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import main.java.ai.AIPlayer;
import main.java.gamelogic.domain.*;
import org.hamcrest.core.Is;
import org.junit.Test;
import test.java.gamelogic.random.Randoms;

import java.util.Collection;

/**
 *
 * @author Lyubomir Pashev
 * @author Simeon Kostadinov
 *
 */

public class PacLaserTest {

	@Test
	public void shouldConstruct() {

        final int LASER_COOLDOWN = 20;
		final RuleChecker ruleEnforcer = Randoms.randomRuleEnforcer();
		final Map map = Randoms.randomMap();

		// When
		final World world = new World(ruleEnforcer, map, true);
		// When
		final PacLaser pacLaser = new PacLaser();

		final Player owner = new LocalPlayer("Owner");
		final Position posOwner = new Position(5, 5);
		owner.setPosition(posOwner);
		owner.setID(1);
		pacLaser.setOwner(owner);
		pacLaser.getOwner().setAngle(0.0);
		pacLaser.setCD(LASER_COOLDOWN);
		owner.setWorld(world);
		world.addEntity(owner);

		final Player player1 = new LocalPlayer("Player1");
		final Position pos1 = new Position(5, 6);
		player1.setID(2);
		player1.setPosition(pos1);
		player1.setWorld(world);
		world.addEntity(player1);

		final Player player2 = new LocalPlayer("Player2");
		final Position pos2 = new Position(5, 7);
		player2.setID(3);
		player2.setPosition(pos2);
		player2.setWorld(world);
		world.addEntity(player2);

		final Player player3 = new LocalPlayer("Player3");
		final Position pos3 = new Position(5, 8);
		player3.setID(4);
		player3.setPosition(pos3);
		player3.setWorld(world);
		world.addEntity(player3);

        pacLaser.activate();

		final World world1 = pacLaser.getOwner().getWorld();

		final Collection<Entity> entities = world1.getEntities();

		final int row = pacLaser.getOwner().getPosition().getRow();
		final int col = pacLaser.getOwner().getPosition().getColumn();

		boolean testValue = true;


		for(Entity entity:entities){
            final int enrow = entity.getPosition().getRow();
			final int encol = entity.getPosition().getColumn();
			if(enrow==row && encol>col){
			    if(!entity.getIsKilled()) {
                    testValue = false;
                }
			}
		}
		// Then
		assertThat(pacLaser.getName(), Is.is("PacLaser"));
		assertTrue(testValue);
	}

}
