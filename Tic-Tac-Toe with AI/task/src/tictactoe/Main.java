package tictactoe;

import java.util.Random;
import java.util.Scanner;

public class Main {
	private static String machinePlayLevel =
			"easy";
			// "machineAlwaysLoose";

	private static boolean skipEntryInitialStatus = true;  // false for nen default initial status

    final private static String INITIAL_STATUS = "";
	private static String gameStatus = INITIAL_STATUS;
	private static int[] nextCoordinates = new int[2];
	final private static char[] nextEntryChar = {'X', 'O'};
	private static int countX_O = 0;
	private static char[][] table = {
			{'-','-','-','-','-','-','-','-','-'},
			{'|',' ',' ',' ',' ',' ',' ',' ','|'},
			{'|',' ',' ',' ',' ',' ',' ',' ','|'},
			{'|',' ',' ',' ',' ',' ',' ',' ','|'},
			{'-','-','-','-','-','-','-','-','-'}
	};

    final private static String PLAYING_STATUS = "Game not finished";
    final private static String X_WIN_STATUS = "X wins";
    final private static String O_WIN_STATUS = "O wins";
    final private static String DRAW_STATUS = "Draw";

    final private static String ENTER_COORDINATES = "Enter the coordinates: ";
    final private static String ENTER_NUMBERS = "You should enter numbers!";
    final private static String ENTER_INTERVAL = "Coordinates should be from 1 to 3!";
    final private static String ENTER_TWO_NUMBERS = "There should be two coordinates";
    final private static String OCCUPIED_COORDINATES = "This cell is occupied! Choose another one!";

	private static Scanner sc = new Scanner(System.in);
	private static Random random = new Random();

	public static void main(String[] args) {
        // write your code here
        while (!enterInitialCells(skipEntryInitialStatus)) {
			/* loop until success */
		}
        while (continueGame()) {
            while (!nextSuccessfullyEnteredMovement()) {
				/* loop until success */
			}
			table[nextCoordinates[0]][2 * nextCoordinates[1]] = nextEntryChar[countX_O % 2];
			countX_O++;
        }
        System.out.println(gameStatus);
    }

	private static boolean nextSuccessfullyEnteredMovement() {
		if (countX_O % 2 == 0) {
			return successfullyEnterCoordinates();
		}
		switch (machinePlayLevel) {
			case "easy":
				nextCoordinates[0] = 1 + random.nextInt(3);
				nextCoordinates[1] = 1 + random.nextInt(3);
				System.out.println("Making move level \"easy\"");
				return true;
			case "machineAlwaysLoose":
				do {
					nextCoordinates[0] = 1 + random.nextInt(3);
					nextCoordinates[1] = 1 + random.nextInt(3);
				} while (nextMoveHasOccupiedCoordinates() ||
						nextMoveMakesWinner());
				System.out.println("Making move level \"machineAlwaysLoose\"");
				return true;
			default:
				throw new IllegalStateException("Unexpected value: " + machinePlayLevel);
		}
	}

	private static boolean nextMoveHasOccupiedCoordinates() {
		return ' ' != (table[nextCoordinates[0]][2 * nextCoordinates[1]]);
	}

	private static int counterOfSpecificCharacter(char searchedCharacter, char ... array) {
		int result = 0;
		for (char item: array) {
			if (searchedCharacter == item) {
				result++;
			}
		}
		return result;
	}

	private static boolean nextMoveMakesWinner() {
		/* checks if the next position is the third of the diagonal, line or column*/
		char nextMoveChar = nextEntryChar[countX_O % 2];
		if (nextCoordinates[0] == nextCoordinates[1]) {
			/* main disgonal winner */
			return 2 == counterOfSpecificCharacter(nextMoveChar,
					table[1][1], table[2][4], table[3][6]);
		}
		if (4 == nextCoordinates[0] + nextCoordinates[1]) {
			/* antidisgonal winner */
			return 2 == counterOfSpecificCharacter(nextMoveChar,
					table[1][6], table[2][4], table[3][2]);
		}
		if (2 == counterOfSpecificCharacter(nextMoveChar,
				table[nextCoordinates[0]][2],
				table[nextCoordinates[0]][4],
				table[nextCoordinates[0]][6])) {
			/* line winner */
			return true;
		}
		return false;
		/*
		return 2 == // Column winner if true is not defined in this game
				counterOfSpecificCharacter(nextMoveChar,
				table[1][2 * nextCoordinates[1]],
				table[1][2 * nextCoordinates[1]],
				table[1][2 * nextCoordinates[1]]); */

	}

	private static boolean successfullyEnterCoordinates() {
        System.out.print(ENTER_COORDINATES);
        String[] line = sc.nextLine().trim().split("\\s+");
        for (String item: line) {
            if (!item.matches("\\d+") ) {
                System.out.println(ENTER_NUMBERS);
                return false;
            } else if (!item.matches("[1-3]")) {
                System.out.println(ENTER_INTERVAL);
                return false;
            }
        }
        if (line.length != 2) {
            System.out.println(ENTER_TWO_NUMBERS);
            return false;
        }
		nextCoordinates[0] = Integer.parseInt(line[0]);
		nextCoordinates[1] = Integer.parseInt(line[1]);
        if (!(table[nextCoordinates[0]][2 * nextCoordinates[1]] == ' ')) {
            System.out.println(OCCUPIED_COORDINATES);
            return false;
        }
        return true;
    }

	private static boolean diagonalWinnerIdentified() {
		String diagonals = new String(new char[]{'(',
				table[1][2], table[2][4], table[3][6],
				'|',
				table[1][6], table[2][4], table[3][2],
				')'});
		if ("XXX".matches(diagonals)) {
			gameStatus = X_WIN_STATUS;
			return true;
		} else if ("OOO".matches(diagonals)) {
			gameStatus = O_WIN_STATUS;
			return true;
		} else {
			return false;
		}
	}

	private static void checkLineWinnerOrDrawStatus() {
		for (int i = 1; i < 4; i++) {
			String line = new String(new char[]{table[i][2], table[i][4], table[i][6]});
			if ("XXX".equals(line)) {
				gameStatus = X_WIN_STATUS;
				return;
			} else if ("OOO".equals(line)) {
				gameStatus = O_WIN_STATUS;
				return;
			} else if (line.contains(" ")) {
				gameStatus = PLAYING_STATUS;
			}
		}
	}

	private static boolean continueGame() {
        /* Game status evaluation */
        boolean initialTable = INITIAL_STATUS.equals(gameStatus);
        boolean gameCanContinue = false;
        gameStatus = DRAW_STATUS;
        if (!diagonalWinnerIdentified()) {
			checkLineWinnerOrDrawStatus();
		}
        displayTable();
        if (PLAYING_STATUS.equals(gameStatus)) {
            if (!initialTable) {
                System.out.println(PLAYING_STATUS);
            }
            gameCanContinue = true;
        }
        return gameCanContinue;
    }

	private static void displayTable() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(table[i][j]);
            }
            System.out.println();
        }
    }

    private static boolean enterInitialCells(boolean skipEntry) {
		if (skipEntry) {
			return true;
		}
        System.out.print("Enter the cells: ");
        String inputCells = sc.nextLine();
        if (!inputCells.matches("[XO_]{9}")) {
            System.out.println("Expected 9 characters X, O or _ but received \"" + inputCells + "\" !");
            return false;
        }
        inputCells = inputCells.replaceAll("_"," ");
        for (int i = 0; i < 9; i++) {
            switch (inputCells.charAt(i)) {
                case 'X':
                    countX_O++;
                    break;
                case 'O':
                    countX_O--;
                    break;
                default:
            }
        }
        if (countX_O < 0 || countX_O > 1) {
            System.out.println("Expected the difference between X and O occurencies to be 0 or 1 !");
            return false;
        }
        for (int i = 0; i < 9; i++) {
            table[(1 + i / 3)][(1 + i % 3) * 2] = inputCells.charAt(i);
        }
        return true;
    }

}
