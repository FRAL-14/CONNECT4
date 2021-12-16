package domainmodel;

public class PlayerHuman extends Player {
	public PlayerHuman(String name, Grid grid) {
		super(name, 'O', grid);
	}

	public boolean dropCoin(int col) {
        col--;
        Integer lowestFreeSpot = findLowestFreeSpot(col);
        if (lowestFreeSpot == null) {
            return true;
        }
        getGrid().getSpot(lowestFreeSpot, col).setCoin(new Coin(this));
        return false;
    }
}
