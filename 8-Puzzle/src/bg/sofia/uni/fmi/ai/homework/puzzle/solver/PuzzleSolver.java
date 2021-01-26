package bg.sofia.uni.fmi.ai.homework.puzzle.solver;

import java.util.ArrayDeque;
import java.util.Scanner;

import bg.sofia.uni.fmi.ai.homework.puzzle.board.Board;

public class PuzzleSolver {
	
	private int N;
	private int I;
	private ArrayDeque<Board> statePath;
	private static Board goal;
	private static final int FOUND = -1;
	private static final int COST_FROM_PARENT = 1;
	private static final int NUM_CHILDREN = 4;
	private static final String NO_SOLUTION = "The puzzle has no solution.";
	
	private PuzzleSolver(int N, int I, Board goal, ArrayDeque<Board> statePath) {
		this.N = N;
		this.I = I;
		this.statePath = statePath;
		PuzzleSolver.goal = goal;
	}

	public static PuzzleSolver getPuzzleSolver() {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		int index = scanner.nextInt();
		int size = (int) Math.sqrt(n+1);
		int emptyRowGoal = 0;
		int emptyColGoal = 0;
		int emptyRowBoard = 0;
		int emptyColBoard = 0;
		int cnt = 1;
		int position = 0;
		int[][] board = new int[size][size];
		int[][] goal = new int[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = scanner.nextInt();
				if (board[i][j] == 0) {
					emptyRowBoard = i;
					emptyColBoard = j;
				}				
				
				if (position == index || (position == n  && index == -1)) {
					goal[i][j] = 0;
					emptyRowGoal = i;
					emptyColGoal = j;
				} else if (position != index) {
					goal[i][j] = cnt;
					cnt++;
				}
				
				position++;
				
			}
		}
		
		scanner.close();		
		Board initialBoard = new Board(board, emptyRowBoard, emptyColBoard);
		ArrayDeque<Board> states = new ArrayDeque<>();
		states.add(initialBoard);
	    return new PuzzleSolver(n, index, new Board(goal, emptyRowGoal, emptyColGoal), states);
	}
	
	public static Board getGoal() {
		return goal;
	}
	
	public int IDAStar() {
		int minFoundThreshold = search(0, statePath.peekFirst().manhattanDistance());
		while (minFoundThreshold != FOUND) {			
			minFoundThreshold = search(0, minFoundThreshold);
		}
		
		return statePath.size() - 1;
	}
	
	public int search(int currentPathCost, int threshold) {
		Board currentBoard = statePath.peekFirst();
		int pathCost = currentPathCost + currentBoard.manhattanDistance(); //f = g + h
		if (pathCost > threshold) {
			return pathCost;
		}
		
		if (currentBoard.equals(goal)) {
			return FOUND;
		}
		
		int minPathCost = Integer.MAX_VALUE;
		for (int i = 0; i < NUM_CHILDREN; i++) {
			Board child;
			switch (i) {
			case 0 : child = currentBoard.getDown(); break;
			case 1 : child = currentBoard.getUp(); break;
			case 2 : child = currentBoard.getLeft(); break;
			default : child = currentBoard.getRight();
			}
			
			if (child != null && !statePath.contains(child)) {
				
				statePath.addFirst(child);
				int newFoundThreshold = search(currentPathCost + COST_FROM_PARENT, threshold);
				if (newFoundThreshold == FOUND) {
					return FOUND;
				} else if (newFoundThreshold < minPathCost) {
					minPathCost = newFoundThreshold;
				}
				
				statePath.removeFirst();
			}
		}
		
		return minPathCost;
	}
	
	public void printSolution() {
		if (!statePath.peekFirst().isSolveable()) {
			System.out.println(NO_SOLUTION);
			return;
		}
		
		long start = System.currentTimeMillis();
		int pathSize = IDAStar();
		System.out.println(pathSize);
		ArrayDeque<String> path = new ArrayDeque<>();
		for (int i = 0; i < pathSize; i++) {
			path.addFirst(statePath.peekFirst().getMove());
			statePath.removeFirst(); 			
		}
		
		for (int i = 0; i < pathSize; i++) {
			System.out.println(path.peekFirst());
			path.removeFirst();
		}
		
		long end = System.currentTimeMillis();
		//System.out.println(end - start + "ms");
	}

}
