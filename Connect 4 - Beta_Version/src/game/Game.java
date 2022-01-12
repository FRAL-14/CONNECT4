package game;

import java.util.Scanner;

import static game.Utilities.*;

public class Game {
	public static void main(String[] args) {
		Database.createDatabase();
		Leaderboard.createLeaderboardTable();
		SaveGame.createSaveGameTables();

		String input;
		Scanner scanner = new Scanner(System.in);

		while (true) {
			printWelcomeScreen();

			input = null;
			while (input == null) {
				input = askAndGetInput(scanner);
				switch (input) {
					case "n", "l", "e", "i", "s", "d" -> {}
					default -> {
						System.out.println("Incorrect input.");
						input = null;
					}
				}
			}

			switch (input) {
				case "n" -> {
					printNewScreen();
					printLogo();
					String name = askForName(scanner);

					GameSession gameSession = new GameSession(name);
					gameSession.playGame();
				}
				case "e" -> {
					System.out.println("Closing game...");
					System.exit(0);
				}
				case "l" -> tryToLoadGame(scanner);
				case "i" -> printInstructions(scanner);
				case "s" -> {
					String name;

					printNewScreen();
					System.out.print("Enter a name to look for in the leaderboard: ");
					name = scanner.nextLine();
					System.out.println();
					Leaderboard.searchPlayer(name);
					pressEnterToContinue(scanner);
				}
				case "d" -> {
					Database.deleteData();
					System.out.print("Leaderboard and saved games deleted");
					dotDotDot();
					Leaderboard.createLeaderboardTable();
					SaveGame.createSaveGameTables();
				}
			}
		}
	}


	/**
	 Asks user to enter their name, removes leading and

	 @param scanner <code>Scanner</code>

	 @return <code>String</code> name of player
	 */
	private static String askForName(Scanner scanner) {
		System.out.print("Type in your name: ");
		String name;
		name = scanner.nextLine().trim();
		while (name.length() > 20 || name.length() == 0) {
			System.out.println("Name has invalid length. Please use between 1 and 20 characters.");
			System.out.print("Type in your name: ");
			name = scanner.nextLine().trim();
		}
		return name;
	}

	/**
	 "Input: " gets printed on screen

	 @param scanner needed to read input

	 @return input as <code>String</code>
	 */
	public static String askAndGetInput(Scanner scanner) {
		System.out.println();
		System.out.print("Input: ");
		return scanner.nextLine();
	}


	/**
	 Ask user for a name and looks for a saved game under that name

	 @param scanner <code>Scanner</code>
	 */
	private static void tryToLoadGame(Scanner scanner) {
		String name;
		GameSession gameSession;
		boolean gamesAvailable;

		printNewScreen();
		gamesAvailable = SaveGame.printSavedGames();
		if (!gamesAvailable) {
			System.out.println("No saved games found");
			dotDotDot();
			return;
		}

		System.out.print("Enter your name to look for a saved game: ");
		name = scanner.nextLine();
		gameSession = SaveGame.loadGame(name);

		if (gameSession != null) {
			gameSession.playGame();
		}
	}
}
