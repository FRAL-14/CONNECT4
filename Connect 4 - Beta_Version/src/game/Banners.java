package game;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Banners {

	/**
	 program waits for specified time

	 @param milliseconds 1000 == 1 second
	 */
	public static void sleep(int milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 Moves the screen up by 25 rows to clear the screen
	 */
	public static void printNewScreen() {
		final int MAX_LINES = 35;
		for (int i = 0; i < MAX_LINES; i++) {
			System.out.println();
		}
	}

	/**
	 Prints "Press Enter to continue" and waits for a new line input
	 */
	public static void pressEnterToContinue(Scanner scanner) {
		System.out.print("Press Enter to continue. ");
		scanner.nextLine();
	}

	/**
	 Prints three dots with half a second in between each dot
	 */
	public static void dotDotDot() {
		sleep(500);
		System.out.print(".");
		sleep(500);
		System.out.print(".");
		sleep(500);
		System.out.print(".");
		sleep(500);
		System.out.println();
	}

	public static void printLogo() {
		String logo = """
				  ______   ______   .__   __. .__   __.  _______   ______ .___________.    _  _
				 /      | /  __  \\  |  \\ |  | |  \\ |  | |   ____| /      ||           |   | || |
				|  ,----'|  |  |  | |   \\|  | |   \\|  | |  |__   |  ,----'`---|  |----`   | || |_
				|  |     |  |  |  | |  . `  | |  . `  | |   __|  |  |         |  |        |__   _|
				|  `----.|  `--'  | |  |\\   | |  |\\   | |  |____ |  `----.    |  |           | |
				 \\______| \\______/  |__| \\__| |__| \\__| |_______| \\______|    |__|           |_|
				""";
		System.out.println(logo);
	}

	public static void printGameOver() {
		String gameOver = """
				  _______      ___      .___  ___.  _______      ______   ____    ____  _______ .______     \s
				 /  _____|    /   \\     |   \\/   | |   ____|    /  __  \\  \\   \\  /   / |   ____||   _  \\    \s
				|  |  __     /  ^  \\    |  \\  /  | |  |__      |  |  |  |  \\   \\/   /  |  |__   |  |_)  |   \s
				|  | |_ |   /  /_\\  \\   |  |\\/|  | |   __|     |  |  |  |   \\      /   |   __|  |      /    \s
				|  |__| |  /  _____  \\  |  |  |  | |  |____    |  `--'  |    \\    /    |  |____ |  |\\  \\----.
				 \\______| /__/     \\__\\ |__|  |__| |_______|    \\______/      \\__/     |_______|| _| `._____|
				                                                                                            \s
				""";
		System.out.println(gameOver);
	}

	public static void youWin() {
		String youWin = """
				  ____    ____  ______    __    __     ____    __    ____  __  .__   __.\s
				  \\   \\  /   / /  __  \\  |  |  |  |    \\   \\  /  \\  /   / |  | |  \\ |  |\s
				   \\   \\/   / |  |  |  | |  |  |  |     \\   \\/    \\/   /  |  | |   \\|  |\s
				    \\_    _/  |  |  |  | |  |  |  |      \\            /   |  | |  . `  |\s
				      |  |    |  `--'  | |  `--'  |       \\    /\\    /    |  | |  |\\   |\s
				      |__|     \\______/   \\______/         \\__/  \\__/     |__| |__| \\__|\s
				""";
		System.out.println(youWin);
	}

	public static void printInstructions(Scanner scanner) {
		String instructions = """
				Rules and instructions:
				 - Your objective is to be the first to connect 4
						
				 - You can connect four vertically, horizontally, diagonally
				    
				 - The game ends when a player connects four or all squares are filled (draw)
				    
				    
				""";
		printNewScreen();
		System.out.print(instructions);
		pressEnterToContinue(scanner);
	}


	public static void printWelcomeScreen() {
		String instructions = """ 
				Start a new game..........n
				Load a saved game.........l
				Search the leaderboard....s
				View game instruction.....i
				Close game................e
								
				""";
		printNewScreen();
		Banners.printLogo();
		Leaderboard.printTop5Scores();
		System.out.println();
		System.out.println();
		System.out.print(instructions);
	}
}
