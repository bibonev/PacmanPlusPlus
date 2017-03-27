/*
 * Algorithm property of Jatin Thakur
 */

package main.java.ai;

import java.util.ArrayList;
import java.util.PriorityQueue;

import main.java.constants.CellState;
import main.java.gamelogic.domain.Cell;
import main.java.gamelogic.domain.Map;
import main.java.gamelogic.domain.Position;

/**
 * The Class AStar. Contains the algorithm as well as the basic outline of the
 * game map
 *
 * @author Lyubomir Pashev
 */
public class AStar {

	/** The vertical/horizontal cost. */
	private final int V_H_COST = 10;

	/** The grid. */
	private AStarCell[][] grid;

	/** The priority queue. */
	PriorityQueue<AStarCell> open;

	/** The closed cells. */
	private boolean closed[][];
	private int mapSize;

	/** The start coords. */
	private int startI, startJ;

	/** The end coords. */
	private int endI, endJ;

	/**
	 * Instantiates a new A*. Gets the game map and based on that it creates its
	 * own grid filled with AStarCells that have costs and parent cells
	 *
	 * @param map
	 *            the map
	 */
	public AStar(final Map map) {

		mapSize = map.getMapSize();
		final Cell[][] cells = map.getCells();
		final ArrayList<Position> blocked = new ArrayList<Position>();
		grid = new AStarCell[mapSize][mapSize];
		closed = new boolean[mapSize][mapSize];

		// create the A* grid
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				grid[i][j] = new AStarCell(i, j);
				if (cells[i][j].getState() == CellState.OBSTACLE) {
					blocked.add(new Position(i, j));
				}
			}
		}

		// set permanently blocked cells(walls)
		// walls are just null cells in the grid
		for (int i = 0; i < blocked.size(); ++i) {
			setBlocked(blocked.get(i).getRow(), blocked.get(i).getColumn());
		}

	}

	/**
	 * The Class AStarCell.
	 */
	class AStarCell {

		/** The heuristic cost. */
		int heuristicCost = 0; // Heuristic cost

		/** The final cost. */
		int finalCost = 0; // G+H

		/** The j. */
		int i, j;

		/** The parent. */
		AStarCell parent;

		/**
		 * Instantiates a new a star cell.
		 *
		 * @param cell
		 *            the cell
		 */
		AStarCell(final int i, final int j) {
			this.i = i;
			this.j = j;
		}

		/**
		 * Gets the position.
		 *
		 * @return the position
		 */
		public Position getPos() {
			return new Position(i, j);
		}

		@Override
		public String toString() {
			return "[" + i + ", " + j + "]";
		}
		
		public boolean equals(AStarCell a){
			return (a.getPos().getRow()==i && a.getPos().getColumn()==j);
		}
	}

	/**
	 * Sets a blocked cell.
	 *
	 * @param i
	 *            the X coord
	 * @param j
	 *            the Y coord
	 */
	private void setBlocked(final int i, final int j) {
		grid[i][j] = null;
	}

	/**
	 * Sets the start cell.
	 *
	 * @param start
	 *            the new start cell
	 */
	private void setStartCell(final Position start) {
		startI = start.getRow();
		startJ = start.getColumn();
	}

	/**
	 * Sets the end cell.
	 *
	 * @param target
	 *            the new end cell
	 */
	private void setEndCell(final Position target) {
		endI = target.getRow();
		endJ = target.getColumn();
	}

	/**
	 * Check and update cost.
	 *
	 * @param current
	 *            the current cell
	 * @param t
	 *            the next cell
	 * @param cost
	 *            the cost
	 */
	void checkAndUpdateCost(final AStarCell current, final AStarCell t, final int cost) {
		if (t == null || closed[t.i][t.j]) {
			return;
		}
		final int t_final_cost = t.heuristicCost + cost;

		final boolean inOpen = open.contains(t);
		if (!inOpen || t_final_cost < t.finalCost) {
			t.finalCost = t_final_cost;
			t.parent = current;
			if (!inOpen) {
				open.add(t);
			}
		}
	}

	/**
	 * A* algorithm. Initializes costs for every cell, the start cell and the
	 * goal cell Updates costs at each step until the goal cell is reached
	 * Computes a path from start to goal
	 *
	 * @param start
	 *            the start cell
	 * @param target
	 *            the target cell
	 * @return the back traced path
	 */
	public ArrayList<Position> AStarAlg(final Position start, final Position target) {

		//new cell queue
		open = new PriorityQueue<>((final Object o1, final Object o2) -> {
			final AStarCell c1 = (AStarCell) o1;
			final AStarCell c2 = (AStarCell) o2;

			return c1.finalCost < c2.finalCost ? -1 : c1.finalCost > c2.finalCost ? 1 : 0;
		});
		
		// set up the costs
		for (int i = 0; i < mapSize; ++i) {
			for (int j = 0; j < mapSize; ++j) {
				if (grid[i][j] != null) {
					grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);
				}
			}
		}
		//visited cells
		closed = new boolean[mapSize][mapSize];
		
		// Set start position
		setStartCell(start);

		// Set End Location
		setEndCell(target);

		// start has 0 cost
		grid[start.getRow()][start.getColumn()].finalCost = 0;

		// add the start location to open list.
		open.add(grid[startI][startJ]);

		AStarCell current;

		// the algorithm
		while (true) {
			current = open.poll();
			if (current == null) {
				break;
			}
			if(current.equals(grid[endI][endJ])){
	            break;
	        }
			closed[current.i][current.j] = true;

			AStarCell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

			}

			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);
			}

			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost + V_H_COST);

			}
		}
		open = null;
		
		// trace back the path
		// path is reversed; goes from END to START
		final ArrayList<Position> path = new ArrayList<Position>();
		AStarCell finalcell = grid[endI][endJ];
		path.add(finalcell.getPos());
		
		while (finalcell.parent != null) {
			path.add(finalcell.parent.getPos());
			finalcell = finalcell.parent;
		}
		
		// resets all parent cells
		for(int i=0;i<mapSize;i++){
			for(int j=0;j<mapSize;j++){
				if(grid[i][j]!=null)
					grid[i][j].parent=null;
			}
		}
		return path;
	}
}