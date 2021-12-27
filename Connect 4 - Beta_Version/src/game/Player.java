package game;

import static game.Grid.ROWS_AMOUNT;

public abstract class Player {
	private final char SIGN;
	private final String NAME;
	private final Grid grid;
	private Score score;

	public Player(String NAME, char SIGN, Grid grid, Score score) {
		this.NAME = NAME;
		this.SIGN = SIGN;
		this.grid = grid;
		this.score = score;
	}

	public Grid getGrid() {
		return grid;
	}

	public char getSign() {
		return SIGN;
	}

	public Integer findLowestFreeSpot(int col) {
		for (int row = ROWS_AMOUNT - 1; row >= 0; row--) {
			if (getGrid().getSpot(row, col).getCoin() == null ) return row;
		}
		return null;
	}

	public String getNAME() {
		return NAME;
	}
}