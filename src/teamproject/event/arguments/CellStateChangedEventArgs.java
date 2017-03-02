package teamproject.event.arguments;

import teamproject.constants.CellState;
import teamproject.gamelogic.domain.Cell;

/**
 * Created by Boyan Bonev on 01/03/2017.
 */
public class CellStateChangedEventArgs {
    private Cell changeCell;
    private CellState state;

    /**
     *
     * Initialize new instance for the cell state change event arguments
     *
     * @param changeCell
     * @param state
     */
    public CellStateChangedEventArgs(Cell changeCell, CellState state){
        this.changeCell = changeCell;
        this.state = state;
    }

    /**
     *
     * Get the cell that needs to be changed
     *
     * @return the cell to be changed
     */
    public Cell getChangeCell() {
        return changeCell;
    }

    /**
     *
     * Get the state that the cell will be changed to
     *
     * @return the state
     */
    public CellState getState() {
        return state;
    }
}
