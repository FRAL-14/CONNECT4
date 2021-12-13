package domainmodel;

import java.io.Serializable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameSession implements Serializable {
	public static void main(String[] args) {
		String input;
		Scanner scanner = new Scanner(System.in);
		Integer columnChoice;

		Banners.printWelcomeScreen();

		input = scanner.nextLine();
		while (!input.equals("n") && !input.equals("l") && !input.equals("e")) {
			System.out.println("Incorrect input.");
			System.out.print("Input: ");
			input = scanner.nextLine();
		}

		switch (input) {
			case "n" -> {
				printNewScreen();
				Banners.printLogo();
				System.out.print("Type in your name: ");
				String name = scanner.nextLine();

				//			Initialization
				Grid grid = new Grid();
				PlayerHuman playerHuman = new PlayerHuman(name, grid);
				PlayerCPU playerCPU = new PlayerCPU("Skynet", grid);

				for (int i = 0; i < 42; i++) {
					printNewScreen();
					grid.printGrid();
					System.out.printf("%s's turn\n", playerHuman.getNAME());
					input = askAndGetInput(scanner);
					columnChoice = getNumber(input);



//					 do {
//					 	try {
//					 		input = null;
//					 		columnChoice = scanner.nextInt();
//					 		if (columnChoice > 0 && columnChoice <= Grid.COLUMNS_AMOUNT) {
//					 			break;
//					 		} else {
//					 			System.out.print("Wrong command.Try again: ");
//					 		}
//					 	} catch (InputMismatchException e) {
//					 		columnChoice = 1;
//					 		input = scanner.nextLine();
//					 	}
//					 } while (input == null || (!input.equals("s") && !input.equals("e") && !input.equals("i")));

					if (input != null) {
						switch (input) {
							case "s" -> {
								System.out.println("Game saved!");
								sleep(2000);
								printNewScreen();
								grid.printGrid();
							}
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
					boolean colFull = playerHuman.dropCoin(columnChoice);
					while (colFull) {
						System.out.println("Column already full! Please choose another one: ");
						columnChoice = scanner.nextInt();
						colFull = playerHuman.dropCoin(columnChoice);
					}
					;
					printNewScreen();
					grid.printGrid();
					System.out.printf("%s's turn\n", playerCPU.getNAME());
					System.out.print("Calculating best move...");
					sleep(2000);
					playerCPU.dropCoin();
					printNewScreen();
					grid.printGrid();
				}
			}
			case "e" -> {
				System.out.println("Closing game...");
				System.exit(0);
			}
			case "l" -> printLoadScreen(scanner);
		}
	}

	public static void printNewScreen() {
		final int MAX_LINES = 25;
		for (int i = 0; i < MAX_LINES; i++) {
			System.out.println();
		}
	}

	public static void sleep(int milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String askAndGetInput(Scanner scanner) {
		System.out.print("Input: ");
		return scanner.nextLine();
	}

	public static Integer getNumber(String input) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return null;
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

	//	useless?
	private static String checkInput(String input) {
		if (input.equals("e")) {
			System.out.println("Closing game...");
			System.exit(0);
		} else if (input.equals("i")) {
			Banners.printInstructions();
		}
		return input;
	}




}
