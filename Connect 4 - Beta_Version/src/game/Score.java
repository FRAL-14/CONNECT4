package game;

import java.util.Date;

public class Score {
	private String name;
	private int moves = 0;
	private long duration;
	private final long startTime;

	public Score(String name, long startTime) {
		this.name = name;
		this.startTime = startTime;
	}

	public void addMove() {
		moves++;
	}

	private void setDuration() {
		Date end = new Date();
		duration = end.getTime() - startTime;
	}

	public long getDuration() {
		setDuration();
		return duration;
	}
}
