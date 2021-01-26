package bg.sofia.uni.fmi.ai.nqueens.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Solver {

	private int n;
	private int[] queens;
	private int[] conflicts;
	private int[] diagonals;
	private int[] reverseDiagonals;
	private Random randomGenerator;
	private boolean initing;
	private static final int MIN_N_TO_PRINT_BOARD = 100;
	private static final int NO_CONFLICTS = 0;
	private static final int SOLUTION_FOUND = -1;
	private static final int K = 10;

	private Solver(int n) {
		this.n = n;
		this.queens = new int[n];
		this.conflicts = new int[n];
		this.diagonals = new int[2 * n - 1];
		this.reverseDiagonals = new int[2 * n - 1];
		this.randomGenerator = new Random();
		this.initing = true;
	}

	private void initQueens(int n) {
		int firstQueen = randomGenerator.nextInt(n);
		initQueenPosition(firstQueen, 0);
		for (int col = 1; col < n; col++) {
			int row = getMinConflictRow(col);
			initQueenPosition(row, col);
		}
		
		initing = false;

	}

	private int encodeDiagonal(int row, int col) {
		return n - 1 + col - row;
	}

	private int encodeReverseDiagonal(int row, int col) {
		return row + col;
	}

	private int getTotalConflicts(int row, int col) {
		return conflicts[row] + diagonals[encodeDiagonal(row, col)] + reverseDiagonals[encodeReverseDiagonal(row, col)];
	}

	private int getMinConflictRow(int col) {
		int minConflicts = Integer.MAX_VALUE;
		List<Integer> minConflictRows = new ArrayList<>();
		for (int row = 0; row < n; row++) {
			int totalConflicts = getTotalConflicts(row, col);
			if (!initing && row == queens[col]) {
				totalConflicts -= 3;
			}

			if (totalConflicts < minConflicts) {
				minConflictRows = new ArrayList<>();
				minConflicts = totalConflicts;
				minConflictRows.add(row);
			} else if (totalConflicts == minConflicts) {
				minConflictRows.add(row);
			}
		}

		return minConflictRows.get(randomGenerator.nextInt(minConflictRows.size()));
	}

	private int getMaxConflictQueen() {
		int maxConflicts = NO_CONFLICTS;
		List<Integer> maxConflictQueens = new ArrayList<>();
		for (int col = 0; col < n; col++) {
			int row = queens[col];
			int totalConflicts = getTotalConflicts(row, col) - 3;
			if (totalConflicts == NO_CONFLICTS) {
				continue;
			}

			if (totalConflicts > maxConflicts) {
				maxConflictQueens = new ArrayList<>();
				maxConflicts = totalConflicts;
				maxConflictQueens.add(col);
			} else if (totalConflicts == maxConflicts) {
				maxConflictQueens.add(col);
			}

		}

		return (maxConflicts == NO_CONFLICTS) ? SOLUTION_FOUND
				: maxConflictQueens.get(randomGenerator.nextInt(maxConflictQueens.size()));
	}

	private void initQueenPosition(int row, int col) {
		queens[col] = row;
		conflicts[row]++;
		diagonals[encodeDiagonal(row, col)]++;
		reverseDiagonals[encodeReverseDiagonal(row, col)]++;
	}

	private void updateQueenPosition(int row, int col) {
		int oldRow = queens[col];
		conflicts[oldRow]--;
		diagonals[encodeDiagonal(oldRow, col)]--;
		reverseDiagonals[encodeReverseDiagonal(oldRow, col)]--;

		queens[col] = row;
		conflicts[row]++;
		diagonals[encodeDiagonal(row, col)]++;
		reverseDiagonals[encodeReverseDiagonal(row, col)]++;
	}

	private void print() {
		if (n < MIN_N_TO_PRINT_BOARD) {
			printAsBoard();
		} else {
			printAsPoints();
		}
	}

	private void printAsPoints() {
		int col = 0;
		for (col = 0; col < n - 1; col++) {
			System.out.println("(" + col + " " + queens[col] + "), ");
		}

		System.out.println("(" + col + " " + queens[col] + ")");
	}

	private void printAsBoard() {
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				if (queens[col] == row) {
					System.out.print("* ");
				} else {
					System.out.print("_ ");
				}
			}

			System.out.println();
			
		}
	}

//	private void removeConflicts() {
//		int maxConflictQueenCol = getMaxConflictQueen();
//		while (maxConflictQueenCol != SOLUTION_FOUND) {
//			int row = getMinConflictRow(maxConflictQueenCol);
//			updateQueenPosition(row, maxConflictQueenCol);
//			maxConflictQueenCol = getMaxConflictQueen();
//		}
//	}
	
	private void removeConflicts() {
		int iter = 0;		
		while (iter++ < n * K) {			
			int maxConflictQueenCol = getMaxConflictQueen();
			if (maxConflictQueenCol == SOLUTION_FOUND) {
				return;
			}
			int row = getMinConflictRow(maxConflictQueenCol);
			updateQueenPosition(row, maxConflictQueenCol);
			if (maxConflictQueenCol == SOLUTION_FOUND) {
				return;
			}
		}
		
		clearQueens();
		initQueens(n);
		removeConflicts();
	}
	
	private void clearQueens() {
		this.queens = new int[n];
		this.conflicts = new int[n];
		this.diagonals = new int[2 * n - 1];
		this.reverseDiagonals = new int[2 * n - 1];
		this.initing = false;
	}

	public void solve() {
		long start = System.currentTimeMillis();
		initQueens(n);
		removeConflicts();
		long end = System.currentTimeMillis();
		print();
		if (n == 10000) {
			System.out.println(end - start + "ms");
		}
	}

	public static Solver getSolver(int n) {
		return new Solver(n);
	}
}
