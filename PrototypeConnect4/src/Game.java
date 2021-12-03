import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input;
		String name;

		printWelcomeScreen();
		input = scanner.nextLine().toLowerCase();


		switch (input) {
			case "n" -> gameLoop(scanner);
			case "l" -> System.out.println("loadscreen");
			case "i" -> printInstructions();
		}

	}

	public static void printWelcomeScreen() {
		String s = ("""
				  ______   ______   .__   __. .__   __.  _______   ______ .___________.    _  _    
				 /      | /  __  \\  |  \\ |  | |  \\ |  | |   ____| /      ||           |   | || |   
				|  ,----'|  |  |  | |   \\|  | |   \\|  | |  |__   |  ,----'`---|  |----`   | || |_  
				|  |     |  |  |  | |  . `  | |  . `  | |   __|  |  |         |  |        |__   _| 
				|  `----.|  `--'  | |  |\\   | |  |\\   | |  |____ |  `----.    |  |           | |   
				 \\______| \\______/  |__| \\__| |__| \\__| |_______| \\______|    |__|           |_|   
				 
				 
				 
				Start a new game..........n
				Load a saved game.........l
				View game instruction.....i
								
				Input:\040""");
		printExtraLines();
		System.out.print(s);
	}

	private static void printGameScreen1() {
		String s = """
				| _ | _ | _ | _ | _ | _ | _ |           Controls:
				| _ | _ | _ | _ | _ | _ | _ |           Save...................s
				| _ | _ | _ | _ | _ | _ | _ |           Exit...................e
				| _ | _ | _ | _ | _ | _ | _ |           Instructions...........i
				| _ | _ | _ | _ | _ | _ | _ |           Choose column........1-7
				| _ | _ | _ | _ | _ | _ | _ |
				  1   2   3   4   5   6   7
				  
				Input:\040""";

		printExtraLines();
		System.out.print(s);
	}

	private static void printGameScreen2() {
		String s = """
				| _ | _ | _ | _ | _ | _ | _ |           Controls:
				| _ | _ | _ | _ | _ | _ | _ |           Save...................s
				| _ | _ | _ | _ | _ | _ | _ |           Exit...................e
				| _ | _ | _ | _ | _ | _ | _ |           Instructions...........i
				| _ | _ | _ | _ | _ | _ | _ |           Choose column........1-7
				| _ | _ | O | _ | _ | _ | _ |
				  1   2   3   4   5   6   7
				  
				Waiting for computer's turn.""";
		printExtraLines();
		System.out.print(s);
		sleep(1000);
		System.out.print(".");
		sleep(1000);
		System.out.print(".");
		sleep(1000);
	}

	private static void printGameScreen3() {
		String s = """
				| _ | _ | _ | _ | _ | _ | _ |           Controls:
				| _ | _ | _ | _ | _ | _ | _ |           Save...................s
				| _ | _ | _ | _ | _ | _ | _ |           Exit...................e
				| _ | _ | _ | _ | _ | _ | _ |           Instructions...........i
				| _ | _ | _ | _ | _ | _ | _ |           Choose column........1-7
				| _ | _ | O | X | _ | _ | _ |
				  1   2   3   4   5   6   7
				  
				Input:\040""";

		printExtraLines();
		System.out.print(s);
	}

	private static void printWinningTurnScreen() {
		String s = """
				| _ | _ | _ | _ | _ | _ | _ |           Controls:
				| _ | _ | _ | _ | _ | _ | _ |           Save...................s
				| _ | _ | _ | _ | _ | O | _ |           Exit...................e
				| _ | _ | X | _ | O | X | O |           Instructions...........i
				| _ | _ | O | O | O | X | X |           Choose column........1-7
				| _ | _ | O | X | X | X | O |
				  1   2   3   4   5   6   7
				  
				Congrats!
				Press enter to continue""";

		printExtraLines();
		System.out.print(s);
	}

	private static void printLosingTurnScreen() {
		String s = """
				| _ | _ | _ | _ | _ | _ | _ |           Controls:
				| _ | _ | _ | _ | _ | _ | _ |           Save...................s
				| _ | _ | _ | _ | _ | X | O |           Exit...................e
				| _ | _ | X | _ | O | X | O |           Instructions...........i
				| _ | _ | O | O | O | X | X |           Choose column........1-7
				| _ | _ | O | X | X | X | O |
				  1   2   3   4   5   6   7
				  
				Congrats!
				Press enter to continue
				""";
		printExtraLines();
		System.out.println(s);
	}

	private static void printLoserScreen() {
		String s = """
				              _______      ___      .___  ___.  _______      ______   ____    ____  _______ .______     \s
				             /  _____|    /   \\     |   \\/   | |   ____|    /  __  \\  \\   \\  /   / |   ____||   _  \\    \s
				            |  |  __     /  ^  \\    |  \\  /  | |  |__      |  |  |  |  \\   \\/   /  |  |__   |  |_)  |   \s
				            |  | |_ |   /  /_\\  \\   |  |\\/|  | |   __|     |  |  |  |   \\      /   |   __|  |      /    \s
				            |  |__| |  /  _____  \\  |  |  |  | |  |____    |  `--'  |    \\    /    |  |____ |  |\\  \\----.
				             \\______| /__/     \\__\\ |__|  |__| |_______|    \\______/      \\__/     |_______|| _| `._____|
				                                                                                                        \s
				Time played    : xx
				Amount of moves: xx
								
				Highscores:
				1...............xxx - 1:16
				2...............abc - 1:23
				3............pinkie - 1:46
				4............blabla - 2:12
				5........helloworld - 2:43
				    
				0 --> exit     1 --> restart
				Input:\040""";
		printExtraLines();
		System.out.print(s);
	}

	private static void printWinnerScreen() {
		String s = """
				   ____    ____  ______    __    __     ____    __    ____  __  .__   __.\s
				   \\   \\  /   / /  __  \\  |  |  |  |    \\   \\  /  \\  /   / |  | |  \\ |  |\s
				    \\   \\/   / |  |  |  | |  |  |  |     \\   \\/    \\/   /  |  | |   \\|  |\s
				     \\_    _/  |  |  |  | |  |  |  |      \\            /   |  | |  . `  |\s
				       |  |    |  `--'  | |  `--'  |       \\    /\\    /    |  | |  |\\   |\s
				       |__|     \\______/   \\______/         \\__/  \\__/     |__| |__| \\__|\s
				                                                                         \s
				Time played    : xx
				Amount of moves: xx
				    
				Highscores:
				1...............xxx - 1:16
				2...............abc - 1:23
				3............pinkie - 1:46
				4............blabla - 2:12
				5........helloworld - 2:43
				    
				0 --> exit     1 --> restart
				Input:\040""";
		printExtraLines();
		System.out.print(s);
	}

	private static void printInstructions() {
		String s = """
				Rules and instructions:
				 - Your objective is to be the first to connect 4
						
				 - You can connect four vertically, horizontally, diagonally
				    
				 - The game ends when a player connects four or all squares are filled (draw)
				    
				    
				Press Enter to continue
				""";
		printExtraLines();
		System.out.print(s);
	}

	private static void checkInput(String input) {
		if (input.equals("e")) {
			System.out.println("Closing game...");
			System.exit(0);
		} else if (input.equals("i")) {
			printInstructions();
		}
	}

	private static void gameLoop(Scanner scanner) {
		String input;
		printGameScreen1();
		input = scanner.nextLine().toLowerCase();
		checkInput(input);
		printGameScreen2();
		printGameScreen3();
		input = scanner.nextLine().toLowerCase();
		checkInput(input);
		if (input.equals("l")) {
			printLosingTurnScreen();
			printLoserScreen();
		} else {
			printWinningTurnScreen();
			printWinnerScreen();
		}
		input = scanner.nextLine();
		while (!input.equals("0") && !input.equals("1")) {
			System.out.print("Incorrect input.\nInput: ");
			input = scanner.nextLine();
		}
		switch (input) {
			case "0" -> {
				System.out.println("Closing game...");
				System.exit(0);
			}
			case "1" -> gameLoop(scanner);
		}
	}

	private static void printExtraLines() {
		final int MAX_LINES = 20;
		for (int i = 0; i < MAX_LINES; i++) {
			System.out.println();
		}
	}

	private static void sleep(int milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
