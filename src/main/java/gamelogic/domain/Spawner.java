package main.java.gamelogic.domain;

public class Spawner extends Entity {
	private static final int MIN_NUMBER = 1, MAX_NUMBER = 5;
	private int timeRemaining;
	private boolean expired;
	private Entity entity;
	private SpawnerColor color;
	
	public Spawner(int spawnTime, Entity entity, SpawnerColor color) {
		this.timeRemaining = spawnTime * 2;
		this.entity = entity;
		this.color = color;
	}
	
	public int getTimeRemaining() {
		int number = (timeRemaining) / 2;
		
		if(number < MIN_NUMBER) number = MIN_NUMBER;
		if(number > MAX_NUMBER) number = MAX_NUMBER;
		return number;
	}
	
	@Override
	public void gameStep(Game game) {
		if(!expired) {
			timeRemaining -= 1;
			
			if(timeRemaining <= 0) {
				this.expired = true;
			}
		}
	}
	
	public boolean isExpired() {
		return expired;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public SpawnerColor getColor() {
		return color;
	}
	
	public static enum SpawnerColor {
		GREEN,
		YELLOW,
		RED,
		CYAN
	}
}
