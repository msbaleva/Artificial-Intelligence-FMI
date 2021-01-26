package bg.sofia.uni.fmi.ai.homework.puzzle.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import bg.sofia.uni.fmi.ai.homework.puzzle.solver.PuzzleSolver;

class ChildrenComparator implements Comparator<Board>{
	 
    @Override
    public int compare(Board e1, Board e2) {
        if(e1.manhattanDistance() < e2.manhattanDistance()){
            return -1;
        } else {
            return 1;
        }
    }
}

public class Board {

	private int[][] tiles;
	private int emptyTileRow;
	private int emptyTileCol;
	private int size;
	private String move;
	private static final String DOWN = "down";
	private static final String UP = "up";
	private static final String LEFT = "left";
	private static final String RIGHT = "right";

	public Board(int[][] tiles, int emptyTileRow, int emptyTileCol, String move) {
		this.tiles = tiles;
		this.size = tiles.length;
		this.emptyTileRow = emptyTileRow;
		this.emptyTileCol = emptyTileCol;
		this.move = move;
	}
	
	public Board(int[][] tiles, int emptyTileRow, int emptyTileCol) {
		this(tiles, emptyTileRow, emptyTileCol, "none");
	}

	public Board(Board other, int row, int col, String move) {
		int otherSize = other.size;
		int[][] newTiles = new int[otherSize][otherSize];
		for (int i = 0; i < otherSize; i++) {
			newTiles[i] = Arrays.copyOf(other.tiles[i], otherSize);
		}
		
		newTiles[other.emptyTileRow + row][other.emptyTileCol + col] = 0;
		newTiles[other.emptyTileRow][other.emptyTileCol] = other.tileAt(other.emptyTileRow + row, other.emptyTileCol + col);
		this.tiles = newTiles;
		this.size = otherSize;
		this.emptyTileRow = other.emptyTileRow + row;
		this.emptyTileCol = other.emptyTileCol + col;
		this.move = move;
	}

	public void print() {
		for (int[] tileRow : tiles) {
			for (int tileCol : tileRow) {
				System.out.print(tileCol + " ");
			}

			System.out.println();
		}
	}

	public int tileAt(int row, int col) {
		return tiles[row][col];
	}

	public int manhattanDistance() {
		int euristic = 0;
		Board goal = PuzzleSolver.getGoal();
		boolean isFound = false;
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (tiles[i][j] != 0)
				{
					for (int p = 0; p < size; p++)
					{
						for (int q = 0; q < size; q++)
						{
							if (tiles[i][j] == goal.tileAt(p, q)) {
								euristic += Math.abs(i - p) + Math.abs(j - q);
								isFound = true;
								break;
							}
						}
						
						if (isFound) {
							break;
						}
					}
					
					isFound = false;
					continue;
				}
			}
		}

		return euristic;
	}
	
	public boolean isValidTile(int row, int col) {
		return emptyTileRow + row != -1 && emptyTileRow + row != size && emptyTileCol + col != -1 && emptyTileCol + col != size;
	}
	
	public Board getDown() {
		if (isValidTile(-1, 0)) {
			return new Board(this, -1, 0, DOWN);
		}
		
		return null;
	}
	
	public Board getUp() {
		if (isValidTile(1, 0)) {
			return new Board(this, 1, 0, UP);
		}
		
		return null;
	}
	
	public Board getLeft() {
		if (isValidTile(0, 1)) {
			return new Board(this, 0, 1, LEFT);
		}
		
		return null;
	}
	
	public Board getRight() {
		if (isValidTile(0, -1)) {
			return new Board(this, 0, -1, RIGHT);
		}
		
		return null;
	}

	public List<Board> getChildren() {
		List<Board> children = new LinkedList<>();
		
		if (isValidTile(-1, 0)) {
			children.add(new Board(this, -1, 0, DOWN));
		}		
		
		if (isValidTile(1, 0)) {
			children.add(new Board(this, 1, 0, UP));
		}
		
		if (isValidTile(0, 1)) {
			children.add(new Board(this, 0, 1, LEFT));
		}
		
		if (isValidTile(0, -1)) {
			children.add(new Board(this, 0, -1, RIGHT));
		}
					
    	Collections.sort(children, new ChildrenComparator());
    	return children;
    }
	
	public int inversionCounter() {
		int doubleSize = size * size;
		int[] array = new int[doubleSize];
		int cnt = 0;
		int k = 0;
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				array[k] = tiles[i][j];
				k++;
			}
		}
		
		for (int i = 0; i < doubleSize - 1; i++)
		{
			for (int j = i + 1; j < doubleSize; j++)
			{
				if (array[i] > 0 && array[j] > 0 && array[i] > array[j])
					cnt++;
			}
		}
		
		return cnt;
	}
	
	public boolean isSolveable() {
		int inversionCnt = inversionCounter();
		boolean inversionCntIsEven = inversionCnt % 2 == 0;
		boolean sizeIsEven = size % 2 == 0;
		boolean emptyRowIsEven = emptyTileRow % 2 == 0;
		return !sizeIsEven ? inversionCntIsEven : (emptyRowIsEven && !inversionCntIsEven) || (!emptyRowIsEven && inversionCntIsEven);
	}

	public String getMove() {
		return move;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(tiles);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (!Arrays.deepEquals(tiles, other.tiles))
			return false;
		return true;
	}

}
