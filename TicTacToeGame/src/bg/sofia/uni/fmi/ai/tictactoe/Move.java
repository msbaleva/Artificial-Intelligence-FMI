package bg.sofia.uni.fmi.ai.tictactoe;

public class Move {
	
	private int row;
	private int col;
	private static final int DEFAULT = -1;
	
	public Move() {
		this.row = DEFAULT;
		this.col = DEFAULT;
	}
	
	public Move(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Move(State board) {
		this.row = board.getMoveRow();
		this.col = board.getMoveCol();
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

}
