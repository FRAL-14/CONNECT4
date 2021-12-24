package domainmodel;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameSession {
	public static void main(String[] args) {
		String input;
		Scanner scanner = new Scanner(System.in);
		Integer columnChoice;
		boolean winner = false;

		Banners.printWelcomeScreen();

		input = askAndGetInput(scanner);
		while (!input.equals("n") && !input.equals("l") && !input.equals("e")) {
			System.out.println("Incorrect input.");
			input = askAndGetInput(scanner);
		}

		switch (input) {
			case "n" -> {
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
				PlayerHuman playerHuman = new PlayerHuman(name, grid);
				PlayerCPU playerCPU = new PlayerCPU("Skynet", grid);


				//				main game loop
				while (grid.gridHasSpace() && !winner) {
					printNewScreen();
					grid.printGrid();
					System.out.printf("%s's turn\n", playerHuman.getNAME());

					boolean turnComplete = false;
					while (!turnComplete) {
						input = askAndGetInput(scanner);
						checkAndDoSideAction(input, scanner, grid);

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

					winner = grid.checkWin();
					if (winner) {
						System.out.printf("Player %s won!\n", playerHuman.getNAME());
						Banners.youWin();
						break;
					}

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

					winner = grid.checkWin();
					if (winner) {
						System.out.printf("%s won!\n", playerCPU.getNAME());
						Banners.printGameOver();
					}
				}

				// GRID FULL --> TIE
				if (!winner) {
					System.out.println();
					System.out.println("Game tied. You suck... Get gud...");
				}
			}
			case "e" -> {
				System.out.println("Closing game...");
				System.exit(0);
			}
			case "l" -> printLoadScreen(scanner);
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
	 Performs one of the secondary game actions like saving, printing instructions and ending the game

	 @param input   <code>String</code>
	 @param scanner <code>Scanner</code>
	 @param grid    <code>Grid</code>
	 */
	public static void checkAndDoSideAction(String input, Scanner scanner, Grid grid) {
		switch (input) {
			case "s" -> System.out.println("Game saved!");
			case "i" -> {
				Banners.printInstructions();
				scanner.nextLine();
				printNewScreen();
				grid.printGrid();
			}
			case "e" -> {
				System.out.println("Closing game...");
				System.exit(0);
			}
		}
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
