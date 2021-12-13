package domainmodel;

import java.util.Scanner;

public class testing {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input = askAndGetInput(scanner);
		Integer col = getNumber(input);

		if (col != null) System.out.println("Number is " + col);
		else System.out.println("Input is " + input);
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
}
