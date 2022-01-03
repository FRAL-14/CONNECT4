package game;

import java.util.Date;

public class Score {
	private int moves;
	private int duration;
	Date beginning = new Date();
	private final long startTime = beginning.getTime();

	public Score() {
		moves = 0;
	}

	public Score(int moves) {
		this.moves = moves;
	}

	public void addMove() {
		moves++;
	}

	public int getMoves() {
		return moves;
	}

	private void setDuration() {
		Date end = new Date();
		duration = (int) ((end.getTime() - startTime) / 1000);
	}

	public int getDuration() {
		setDuration();
		return duration;
	}
}
