package bg.sofia.uni.fmi.ai.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State {
	
	private char[][] board;
	private Move move; 
	private char winner;
	private final static int BOARD_SIZE = 3;
	private final static String TOP_BOTTOM_BORDER = "---------------";
	private final static String LEFT_BORDER = "| ";
	private final static String RIGHT_BORDER = " |";
	public final static char X_SIGN = 'X';
	public final static char O_SIGN = 'O';
	public final static char EMPTY = '.';
	
	
	public State() {
		this.board = new char[BOARD_SIZE][BOARD_SIZE];
		for (char[] row : board) {
			Arrays.fill(row, EMPTY);
		}
		
		this.move = new Move();
		this.winner = EMPTY;
	}
	
	public State(char[][] board, char player, int row, int col) {
		this.board = new char[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.board[i][j] = board[i][j];
            }
        }
		
		this.board[row][col] = player;
		this.move = new Move(row, col);
		this.winner = EMPTY;
	}
	
	
	public boolean isEmpty(int row, int col) {
		return this.board[row][col] == EMPTY;
	}
	
	public void set(int row, int col, char playerSign) {
		board[row][col] = playerSign;
	}
	
	public boolean isValid(int row, int col) {
		return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && isEmpty(row, col);
	}
	
	public List<State> getSuccessorBoards(char player) {
		List<State> successorBoards = new ArrayList<State>();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (this.board[i][j] == EMPTY) {
					successorBoards.add(new State(this.board, player, i, j));
				}
			}
		}
		
		return successorBoards;
	}	
	
	public void print() {
		System.out.println(TOP_BOTTOM_BORDER);
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				System.out.print(LEFT_BORDER + board[i][j] + RIGHT_BORDER);
			}
			
			System.out.println("\n" + TOP_BOTTOM_BORDER);
		}
	}
	
	public boolean hasWinner() {
		return winner != EMPTY;
	}
	
	public char getWinner() {
		return winner;
	}
	
	public boolean isTerminal() {
		return hasWinningRowOrCol() || hasWinningDiagonal() || isDraw();		
	}
	
	public boolean hasWinningRowOrCol() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			if (!isEmpty(i, 0) && board[i][1] == board[i][0] && board[i][2] == board[i][0]) {
				winner = board[i][0];
				return true;
			}
				
			
			if (!isEmpty(0, i) && board[1][i] == board[0][i] && board[2][i] == board[0][i]) {
				winner = board[0][i];
				return true;
				
			}	
		
		}
		
		return false;
	}
	
	public boolean isDraw() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] == EMPTY) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean hasWinningDiagonal() {
		if (isEmpty(1, 1)) {
			return false;
		}
		
		if((board[0][0] == board[1][1] && board[1][1] == board[2][2]) 
				|| (board[2][0] == board[1][1] && board[1][1] == board[0][2])) {
			winner = board[1][1];
			return true;
		}
		
		return false;
	}
	
	
	public Move getMove() {
		return move;
	}
	
	public int getMoveRow() {
		return move.getRow();
	}
	
	public int getMoveCol() {
		return move.getCol();
	}
	
	

}
