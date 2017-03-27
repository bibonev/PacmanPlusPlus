package main.java.gamelogic.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import main.java.constants.CellState;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.Map;
import main.java.graphics.PositionVisualisation;

public class MapService {
	private HashMap<String, Map> availableMaps;
	
	public MapService() {
		availableMaps = null;
	}
	
	public Set<String> getAvailableMaps() {
		if(availableMaps == null) loadMaps();
		
		return availableMaps.keySet();
	}
	
	public Map getMap(String mapName) {
		if(availableMaps == null) loadMaps();
		if(availableMaps.containsKey(mapName)) {
			return availableMaps.get(mapName);
		} else {
			throw new IllegalArgumentException("Unknown map: " + mapName);
		}
	}
	
	private void loadMaps() {
		availableMaps = new HashMap<>();
		availableMaps.put("Default Map", Map.generateMap());
		try {
			
			List<String> mapFile = getStringArrayResource(getStream("maps.txt"));
			
			for(String mapLine : mapFile) {
				String[] data = mapLine.split("\\:");
				String mapName = data[0];
				String mapPath = data[1];
				
				Map map = loadMapFromImage(mapPath);
				
				availableMaps.put(mapName, map);
			}
		} catch(IOException e) {
			System.out.println("Could not load maps");
		}
	}

	private CellState cellStateFromPixelcolor(int color) {
		color = color & 0xFFFFFF; // only want RGB
		
		switch(color) {
		case 0x000000: return CellState.EMPTY;
		case 0x0000ff: return CellState.OBSTACLE;
		case 0xffff00: return CellState.FOOD;
		default: return CellState.EMPTY;
		}
	}
	
	private Map loadMapFromImage(String mapPath) {
		try {
			BufferedImage img = getImageResource(getStream(mapPath));
			
			int rows = img.getHeight(null), cols = img.getWidth(null);
			if(rows != cols) {
				System.out.println("map " + mapPath + " is not square!");
				return null;
			} else {
				Map map = new Map(rows);
				for(int i = 0; i < rows; i++) {
					for(int j = 0; j < cols; j++) {
						int pixelColor = img.getRGB(j, i) & 0xffffff;
						CellState state = cellStateFromPixelcolor(pixelColor);
						map.addCell(new Cell(state, new PositionVisualisation(i, j)));
					}
				}
				
				return map;
			}
		} catch(IOException e) {
			System.out.println("Could not load " + mapPath + ": " + e.getMessage());
			return null;
		}
	}

	private InputStream getStream(String path) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(path);
		
		return is == null ?
				new FileInputStream(path) : is;
	}
	
	public String getStringResource(InputStream stream) throws IOException {
		try(Scanner scanner = new Scanner(stream, "UTF-8")) {
			scanner.useDelimiter("\\A");
			
			if(scanner.hasNext()) {
				return scanner.next();
			} else {
				return "";
			}
		}
	}
	
	public List<String> getStringArrayResource(InputStream stream) throws IOException {
		try(Scanner scanner = new Scanner(stream, "UTF-8")) {
			List<String> strings = new ArrayList<>();
			
			while(scanner.hasNext()) {
				strings.add(scanner.nextLine());
			}
			
			return strings;
		}
	}
	
	public BufferedImage getImageResource(InputStream stream) throws IOException {
		return ImageIO.read(stream);
	}
}
