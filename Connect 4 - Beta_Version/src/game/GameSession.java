package game;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameSession {
	public static void main(String[] args) {
		SaveGame.dropEverything();              // empty the database
		Leaderboard.createLeaderboardTable();
		SaveGame.createALLTABLES();
		String input;
		Integer columnChoice;
		Scanner scanner = new Scanner(System.in);

		while (true) {
			Banners.printWelcomeScreen();

			input = askAndGetInput(scanner);
			while (!input.equals("n") && !input.equals("l") && !input.equals("e") && !input.equals("i")) {
				System.out.println("Incorrect input.");
				input = askAndGetInput(scanner);
			}

			switch (input) {
				case "n" -> {
					boolean winner = false;
					boolean backToMainMenu = false;

					printNewScreen();
					Banners.printLogo();
					System.out.print("Type in your name: ");
					String name;
					name = scanner.nextLine().trim();
					while (name.length() > 20) {
						System.out.println("Name too long. Please use 20 characters or less.");
						System.out.print("Type in your name: ");
						name = scanner.nextLine().trim();
					}

					//			Initialization
					Grid grid = new Grid();
					PlayerHuman playerHuman = new PlayerHuman(name, grid, new Score());
					PlayerCPU playerCPU = new PlayerCPU("Skynet", grid, new Score());


					//			main game loop
					gameLoop:
					while (grid.gridHasSpace()) {
						printNewScreen();
						grid.printGrid();
						System.out.printf("%s's turn\n", playerHuman.getNAME());

						//      playerHuman's turn
						boolean turnComplete = false;
						while (!turnComplete) {
							input = askAndGetInput(scanner);
							backToMainMenu = checkAndDoSideAction(input, scanner, grid, playerHuman);
							if (backToMainMenu) break gameLoop;

							columnChoice = getNumber(input);
							turnComplete = isInBounds(columnChoice);
							if (turnComplete) {
								boolean colFull = playerHuman.dropCoin(columnChoice);
								if (colFull) {
									System.out.println("Column already full!");
									turnComplete = false;
								}
							}
						}

						printNewScreen();
						grid.printGrid();

						//      check if playerHuman won
						winner = grid.checkWin();
						if (winner) {
							System.out.printf("Player %s won!\n", playerHuman.getNAME());
							Banners.youWin();
							scanner.nextLine();
							break;
						}

						//      CPU's turn
						System.out.printf("%s's turn\n", playerCPU.getNAME());
						System.out.print("Calculating best move");
						sleep(500);
						System.out.print(".");
						sleep(500);
						System.out.print(".");
						sleep(500);
						System.out.print(".");
						sleep(500);
						playerCPU.dropCoin();

						printNewScreen();
						grid.printGrid();

						//      check if CPU won
						winner = grid.checkWin();
						if (winner) {
							System.out.printf("%s won!\n", playerCPU.getNAME());
							Banners.printGameOver();
							scanner.nextLine();
							break;
						}
					}

					//      no winner & no back to main menu command was given --> TIE
					if (!winner && !backToMainMenu) {
						System.out.println();
						System.out.println("Game tied... Better luck next time!");
						scanner.nextLine();
					}
				}
				case "e" -> {
					System.out.println("Closing game...");
					System.exit(0);
				}
				case "l" -> printLoadScreen(scanner);
				case "i" -> Banners.printInstructions(scanner);
			}
		}
	}

	/**
	 Checks not null and within boundaries (1-7)

	 @param val <code>Integer</code>

	 @return <code>boolean</code>
	 */
	public static boolean isInBounds(Integer val) {
		return val != null && val > 0 && val < 8;
	}

	/**
	 Moves the screen up by 25 rows to clear the screen
	 */
	public static void printNewScreen() {
		final int MAX_LINES = 25;
		for (int i = 0; i < MAX_LINES; i++) {
			System.out.println();
		}
	}

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
	 "Input: " gets printed on screen

	 @param scanner needed to read input

	 @return input as <code>String</code>
	 */
	public static String askAndGetInput(Scanner scanner) {
		System.out.print("Input: ");
		return scanner.nextLine();
	}

	/**
	 Tries to parse a <code>String</code> into an <code>Integer</code>. Returns <code>NULL</code> otherwise

	 @param input user input as <code>String</code>

	 @return <code>Integer</code> or <code>NULL</code>
	 */
	public static Integer getNumber(String input) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 Performs one of the secondary game actions like saving, printing instructions, ending the game and returning to main menu

	 @param input   <code>String</code>
	 @param scanner <code>Scanner</code>
	 @param grid    <code>Grid</code>
	 */
	public static boolean checkAndDoSideAction(String input, Scanner scanner, Grid grid, Player player) {
		switch (input) {
			case "s" -> {
				SaveGame.saveGame(player.getNAME(), player.getMoves(), player.getDuration(), grid);
				return false;
			}
			case "i" -> {
				Banners.printInstructions(scanner);
				printNewScreen();
				grid.printGrid();
				return false;
			}
			case "e" -> {
				System.out.println("Closing game...");
				System.exit(0);
			}
			case "q" -> {
				System.out.println("Returning to main menu...");
				return true;
			}
		}
		return false;
	}

	private static void printLoadScreen(Scanner scanner) {
		String s = """
				Saved games:
								
				Choice          Name    Moves   Time (seconds)
				1..............peter........3.............0:21
				2...............seif.......20.............2:13
								
				Press 0 to return to the main menu
				Choice:\040""";
		printNewScreen();
		System.out.print(s);
		scanner.nextLine();
	}
}
