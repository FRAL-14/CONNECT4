package game;

import java.util.Date;

public class Score {
	private final String name;
	private int moves = 0;
	private int duration;
	private final long startTime;

	public Score(String name, long startTime) {
		this.name = name;
		this.startTime = startTime;
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
