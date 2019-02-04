package com.yahoo.slykhachov.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import com.yahoo.slykhachov.chess.model.BoardModel;

public class Chess {
	private ChessGame chessGame;
	private Chess() {
		setChessGame(createChessGame());
		final JFrame jf = new JFrame("ChessWorld");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(getChessGame().getChessGameView(), BorderLayout.CENTER);
		jf.add(createToolBar(), BorderLayout.NORTH);
		jf.pack();
		jf.setResizable(true);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	private JToolBar createToolBar() {
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.setLayout(new FlowLayout());
		jtb.setBackground(Color.GRAY);
		JButton undoButton = new JButton("Undo move");
		undoButton.addActionListener(
			ae -> {
				if (getChessGame().getComputerAdversary() !=
							getChessGame().getAdversaryToMove()) {
					BoardModel bm = getChessGame().getChessGameModel()
						.getBoardModel();
					if (bm.isSafeToUndo()) {
						bm.undoMove();
						bm.undoMove();
						getChessGame().getChessGameView()
					    			  .getBoardView()
					        		  .updateBoardView();
					}
				}		  
		});
		jtb.add(undoButton);
		return jtb;
	}
	public static void main(String[] sa) {
		EventQueue.invokeLater(Chess::new);
	}
	private ChessGame createChessGame() {
		ChessGame chessGame = new ChessGame();
		return chessGame;
	}
	public ChessGame getChessGame() {
		return this.chessGame;
	}
	public void setChessGame(ChessGame chessGame) {
		this.chessGame = chessGame;
	}
}
