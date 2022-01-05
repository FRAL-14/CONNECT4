package game;

import java.util.Scanner;

import static game.Banners.*;

public class Game {
	public static void main(String[] args) {
		Leaderboard.createDatabase();
//		SaveGame.dropEverything();              // empty the database
		Leaderboard.createLeaderboardTable();
		SaveGame.createAllTables();

		String input;
		Scanner scanner = new Scanner(System.in);

		while (true) {
			printWelcomeScreen();

			input = askAndGetInput(scanner);
			while (!input.equals("n") && !input.equals("l") && !input.equals("e") && !input.equals("i") && !input.equals("s")) {
				System.out.println("Incorrect input.");
				input = askAndGetInput(scanner);
			}

			switch (input) {
				case "n" -> {

					printNewScreen();
					printLogo();
					String name = askForName(scanner);

					GameSession gameSession = new GameSession(name);

					//			main game loop
					gameSession.playGame();
				}
				case "e" -> {
					System.out.println("Closing game...");
					System.exit(0);
				}
				case "l" -> playLoadedGame(scanner);
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
		System.out.print("Input: ");
		return scanner.nextLine();
	}


	/**
	 Ask user for a name and looks for a saved game under that name

	 @param scanner <code>Scanner</code>
	 */
	private static void playLoadedGame(Scanner scanner) {
		String name;
		GameSession gameSession;

		printNewScreen();
		System.out.print("Enter your name to look for a saved game: ");
		name = scanner.nextLine();
		gameSession = SaveGame.loadGame(name);

		if (gameSession != null) {
			gameSession.playGame();
		}
	}
}
