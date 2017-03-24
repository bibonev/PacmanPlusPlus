package test.java.ai;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import main.java.ai.AStar;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;

public class AStarTest {
	@Test
	public void shouldConstruct() {

		//Given
		Map map = Map.generateMap();
		
		//When
		final AStar astar = new AStar(map);
	
		//Then
		assertTrue(astar.AStarAlg(new Position(0,0), new Position(14,14)).size()>0);
		assertTrue(astar.AStarAlg(new Position(0,0), new Position(0,1)).size()==2);
		assertTrue(astar.AStarAlg(new Position(0,0), new Position(0,2)).size()==3);
	}
}
