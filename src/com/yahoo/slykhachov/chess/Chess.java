package com.yahoo.slykhachov.chess;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Chess {
	private ChessGame chessGame;
	private Chess() {
		final JFrame jf = new JFrame("ChessWorld");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setChessGame(createChessGame());
		jf.add(getChessGame().getChessGameView(), BorderLayout.CENTER);
		jf.add(getChessGame().createMoveListContainer(), BorderLayout.EAST);
		jf.add(getChessGame().createToolBar(), BorderLayout.NORTH);	
		jf.pack();
		jf.setResizable(true);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	public static void main(String[] sa) {
		EventQueue.invokeLater(Chess::new);
	}
	private ChessGame createChessGame() {
		return new ChessGame();
	}
	public ChessGame getChessGame() {
		return this.chessGame;
	}
	public void setChessGame(ChessGame chessGame) {
		this.chessGame = chessGame;
	}
}
