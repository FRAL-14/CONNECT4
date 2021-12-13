package domainmodel;

public class Grid {
	public static final int ROWS_AMOUNT = 6;
	public static final int COLUMNS_AMOUNT = 7;
	Spot[][] spots = new Spot[ROWS_AMOUNT][COLUMNS_AMOUNT];
	private static final char EMPTY_SPOT = '_';
	private static final char WALL = '|';
	String[] controls = {
			"\t\t\tControls:",
			"\t\t\tSave...................s",
			"\t\t\tExit...................e",
			"\t\t\tInstructions...........i",
			"\t\t\tChoose column........1-7",
			""
	};

	public Grid() {
		for (int row = 0; row < ROWS_AMOUNT; row++) {
			for (int col = 0; col < COLUMNS_AMOUNT; col++) {
				spots[row][col] = new Spot(row, col, null);
			}
		}
	}

	public Spot getSpot(int row, int col) {
		return spots[row][col];
	}

	public void printGrid() {
		for (int row = 0; row < ROWS_AMOUNT; row++) {
			for (int col = 0; col < COLUMNS_AMOUNT; col++) {
				System.out.printf("%c %c ", WALL, getSpot(row, col).getCoin() == null ? EMPTY_SPOT : getSpot(row, col).getCoin().getSign());
			}
			System.out.println(WALL + controls[row]);
		}
		for (int i = 1; i <= COLUMNS_AMOUNT; i++) {
			System.out.printf("  %d ", i);
		}
		System.out.println();
	}
}
