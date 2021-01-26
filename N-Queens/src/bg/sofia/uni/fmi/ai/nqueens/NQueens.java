package bg.sofia.uni.fmi.ai.nqueens;

import java.util.Scanner;

import bg.sofia.uni.fmi.ai.nqueens.board.Solver;

public class NQueens {
	
	public static int input() {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		return n;
		
	}
	
	public static void main(String[] args) {
		int n = input();		
		Solver.getSolver(n).solve();		
	}
	
}
