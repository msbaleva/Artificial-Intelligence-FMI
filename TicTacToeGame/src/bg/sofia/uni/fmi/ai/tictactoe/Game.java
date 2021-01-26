package bg.sofia.uni.fmi.ai.tictactoe;

import java.util.List;
import java.util.Scanner;



public class Game {
	
	private int firstPlayer;
	private Scanner scanner;
	private State currentBoard;
	private final static String PROMPT = "First player>";
	private final static String FIRST_PLAYER_QUESTION = "Computer(O) or Player(X) plays first?";
	private final static String INVALID_MOVE = "Move is not valid. Try again.";
	private final static String WINNING_MESSAGE = " wins!";
	private final static String ROW_PROMPT = "row>";
	private final static String COL_PROMPT = "col>";
	private final static int UTILITY_CONST = 10;
	private final static int X_WINS = -1;
	private final static int O_WINS = 1;
	private final static int DRAW = 0;
	private final static String DRAW_MESSAGE = "Draw";
	private final static char USER = State.X_SIGN;
	private final static char COMPUTER = State.O_SIGN;
	
	
	private Game(int firstPlayer, Scanner scanner) {
		this.firstPlayer = firstPlayer;
		this.scanner = scanner;
		this.currentBoard = new State();
	}
	
	public static Game getInstance() {		
		Scanner scanner = new Scanner(System.in);
		System.out.println(FIRST_PLAYER_QUESTION);
		System.out.print(PROMPT);
		char firstPlayer = scanner.next().charAt(0);
		return new Game(firstPlayer, scanner);
	}
	
	
	
	public void play() {	
		if (firstPlayer == USER) {
			userPlaysFirst();
		} else {
			computerPlaysFirst();
		}
		
		scanner.close();		
		printEndgame();
		
	}
	
	private void userPlaysFirst() {
		while (!isEndgame()) {
			makeMove(currentBoard, USER);
			if (isEndgame()) {
				break;
			}
			
			makeMove(currentBoard, COMPUTER);
			currentBoard.print();
		}
	}
	
	private void computerPlaysFirst() {
		while (!isEndgame()) {
			makeMove(currentBoard, COMPUTER);
			currentBoard.print();
			if (isEndgame()) {
				break;
			}
			
			makeMove(currentBoard, USER);
		}
	}
	
	private boolean isEndgame() {
		return currentBoard.isTerminal();
	}
	
	private void printEndgame() {
		char winner = currentBoard.getWinner();
		String endgame = (winner == State.EMPTY) ? DRAW_MESSAGE : winner + WINNING_MESSAGE;
		if (firstPlayer == USER) {
			currentBoard.print();
		}
		
		System.out.println(endgame);
	}
	
	private void makeMove(State board, char playerSign) {
		Move move = (playerSign == COMPUTER) ? findBestMove(board) : readPlayerMove();
		board.set(move.getRow(), move.getCol(), playerSign);
	}
	
	private Move findBestMove(State board) {
		Move move = new Move();
		int value = Integer.MIN_VALUE;
		List<State> possibleBoards = board.getSuccessorBoards(COMPUTER);
		for (State successorBoard : possibleBoards) {
			int currentValue = minimaxAlgorithm(successorBoard, 0, USER);
			if (currentValue > value) {
				value = currentValue;
				move = new Move(successorBoard);
			}
		}
		
		return move;
	}
	
	private Move readPlayerMove() {
		System.out.print(ROW_PROMPT);
		int row = scanner.nextInt() - 1;
		System.out.print(COL_PROMPT);
		int col = scanner.nextInt() - 1;
		while (!currentBoard.isValid(row, col)) {
			System.out.println(INVALID_MOVE);
			System.out.print(ROW_PROMPT);
			row = scanner.nextInt() - 1;
			System.out.print(COL_PROMPT);
			col = scanner.nextInt() - 1;
		}
		
		return new Move(row, col);
	}
	
	private int minimaxAlgorithm(State board, int depth, char player) {
		if (board.isTerminal()) {
			return (board.hasWinner() ? utility(board, depth, board.getWinner()) : DRAW);
		}
		
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		
		if (player == COMPUTER) {
			return maxValue(board, depth + 1, alpha, beta);
		}
		
		return minValue(board, depth + 1, alpha, beta);
	}
	
	private int utility(State state, int depth, char winner) {
		int utility = UTILITY_CONST - depth;
		return utility * ((winner == COMPUTER) ? O_WINS : X_WINS);
	}
	
	private int maxValue(State board, int depth, int alpha, int beta) {
		if (board.isTerminal()) {
			return (board.hasWinner() ? utility(board, depth, board.getWinner()) : DRAW);
		}
		
		int value = Integer.MIN_VALUE;
		List<State> possibleBoards = board.getSuccessorBoards(COMPUTER);
		for (State successorBoard : possibleBoards) {
			value = Math.max(value, minValue(successorBoard, depth + 1, alpha, beta));
			if (value >= beta) {
				return value;
			}
			
			alpha = Math.max(alpha, value);
		}
		
		return value;
	}
	
	private int minValue(State board, int depth, int alpha, int beta) {
		if (board.isTerminal()) {
			return (board.hasWinner() ? utility(board, depth, board.getWinner()) : DRAW);
		}
		
		int value = Integer.MAX_VALUE;
		List<State> possibleBoards = board.getSuccessorBoards(USER);
		for (State successorBoard : possibleBoards) {
			value = Math.min(value, maxValue(successorBoard, depth + 1, alpha, beta));
			if (value <= alpha) {
				return value;
			}
			
			beta = Math.min(beta, value);
		}
		
		return value;
	}
	
	
}
