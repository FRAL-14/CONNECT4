package game;

import java.util.Scanner;

import static game.Banners.*;
import static game.Game.askAndGetInput;

public class GameSession {
	private final Grid grid;
	private boolean winner = false;
	private boolean backToMainMenu = false;
	PlayerHuman playerHuman;
	PlayerCPU playerCPU;
	private final Scanner scanner = new Scanner(System.in);

	public GameSession(String name) {
		grid = new Grid();
		playerHuman = new PlayerHuman(name, grid, new Score());
		playerCPU = new PlayerCPU(grid, new Score());
	}

	public GameSession(Grid grid, PlayerHuman playerHuman, PlayerCPU playerCPU) {
		this.grid = grid;
		this.playerHuman = playerHuman;
		this.playerCPU = playerCPU;
	}

	public void playGame() {
		String input;
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

				Integer columnChoice = getNumber(input);
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
				youWin();
				pressEnterToContinue(scanner);
				Leaderboard.insertToLeaderboard(playerHuman.getNAME(), playerHuman.getMoves(), playerHuman.getDuration());
				break;
			}

			//      CPU's turn
			System.out.printf("%s's turn\n", playerCPU.getNAME());
			System.out.print("Calculating best move");
			dotDotDot();
			playerCPU.dropCoin();
			printNewScreen();
			grid.printGrid();

			//      check if CPU won
			winner = grid.checkWin();
			if (winner) {
				System.out.printf("%s won!\n", playerCPU.getNAME());
				printGameOver();
				pressEnterToContinue(scanner);
				break;
			}
		}

		//      no winner & no back to main menu command was given --> TIE
		if (!winner && !backToMainMenu) {
			System.out.println();
			System.out.print("Game tied");
			dotDotDot();
			System.out.println(" Better luck next time!");
			pressEnterToContinue(scanner);
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
				System.out.print("Saving Game");
				dotDotDot();
				System.out.print("Game Saved! Returning to Main Menu");
				dotDotDot();
				return true;
			}
			case "i" -> {
				printInstructions(scanner);
				printNewScreen();
				grid.printGrid();
				return false;
			}
			case "e" -> {
				System.out.println("Closing game...");
				System.exit(0);
			}
			case "q" -> {
				System.out.print("Returning to main menu");
				dotDotDot();
				return true;
			}
		}
		return false;
	}
}
