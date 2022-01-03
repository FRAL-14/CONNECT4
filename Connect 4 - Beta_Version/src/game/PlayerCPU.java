package game;

import java.util.Random;

import static game.Grid.COLUMNS_AMOUNT;


public class PlayerCPU extends Player {
	private Random random = new Random();

	public PlayerCPU(String name, Grid grid, Score score) {
		super(name, 'X', grid, score);
	}

	public PlayerCPU(Grid grid, Score score) { super("Skynet",'X',grid,score); }


	public void dropCoin() {
		int col;
		Integer lowestFreeSpot;
		col = random.nextInt(COLUMNS_AMOUNT);
		do {
			col = (col + 1) % COLUMNS_AMOUNT;
			lowestFreeSpot = findLowestFreeSpot(col);
		} while (lowestFreeSpot == null);

		getGrid().getSpot(lowestFreeSpot, col).setCoin(new Coin(this));
		getGrid().addCoin();
	}
}
