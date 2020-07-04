package com.yahoo.slykhachov.chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import com.yahoo.slykhachov.chess.ai.MoveChooser;
import com.yahoo.slykhachov.chess.model.AdversaryModel;
import com.yahoo.slykhachov.chess.model.ChessGameModel;
import com.yahoo.slykhachov.chess.model.BoardModel;
import com.yahoo.slykhachov.chess.view.ChessGameView;

public class ChessGame {
	public static final int SEARCH_DEPTH = 5;
	public static final ExecutorService POOL = Executors.newFixedThreadPool(1);
	private ChessGameModel chessGameModel;
	private ChessGameView chessGameView;
	private Board board;
	private AdversaryModel computerAdversary;
	private AdversaryModel whitePlayer;
	private AdversaryModel blackPlayer;
	private AdversaryModel adversaryToMove;
	private ExecutorService pool;
	private DefaultListModel<String> listModel;
	public ChessGame() {
		this.setBlackPlayer(new Black());
		this.setWhitePlayer(new White());
		this.setBoard(new Board(blackPlayer, whitePlayer, this));
		this.setChessGameModel(new ChessGameModel(getBoard().getBoardModel()));
		this.setChessGameView(new ChessGameView(getBoard().getBoardView()));
	    this.setAdversaryToMove(whitePlayer);
	    this.setComputerAdversary(blackPlayer);
	    //this.setComputerAdversary(whitePlayer);
	    if (getComputerAdversary() == whitePlayer) {
			this.doResponce();
	    }
	}
	public void doResponce() {
		ChessGame.POOL.submit(
			() -> {
				if (isCheckMate() || 
						getComputerAdversary().isStaleMate(getBoard().getBoardModel())) {
					System.out.println("You won");
					setAdversaryToMove(
						getAdversaryToMove().getOpponent()
					);
					int result = JOptionPane.showConfirmDialog(
						null,
						"You won.\nStart new game?",
						"Select an option",
						JOptionPane.OK_CANCEL_OPTION
					);
					if (result == JOptionPane.OK_OPTION) {
						this.reset();
					}
				} else {
					Best best = MoveChooser.miniMaxAlphaBeta(
						MoveChooser.MAXIMIZER,
						getComputerAdversary(),
						getComputerAdversary(),
						getBoard().getBoardModel(),
						Integer.MIN_VALUE, 
						Integer.MAX_VALUE,
						SEARCH_DEPTH
					);
					Move move = best.getMove();
					System.out.println(best.getScore());
					this.getBoard().getBoardModel().performMove(move);
					System.out.println(this.getBoard().getBoardModel());
					this.getBoard().getBoardView().updateBoardView();
					if (this.listModel != null) {
						String s = getBoard().getBoardModel()
							.getNumberOfMovesPerformed() + ".  " + move.toDisplayableString();
						this.listModel.addElement(s);
					}
					setAdversaryToMove(
							getAdversaryToMove().getOpponent()
					);
					if (isCheckMate()
							|| getComputerAdversary().getOpponent().isStaleMate(getBoard().getBoardModel())) {
						System.out.println("Computer player won");
						int result = JOptionPane.showConfirmDialog(
							null,
							"You lost.\nStart new game?",
							"Select an option",
							JOptionPane.OK_CANCEL_OPTION
						);
						if (result == JOptionPane.OK_OPTION) {
							this.reset();
						}
					}
				}
			}
		);
	}
	private void reset() {
		this.setBlackPlayer(new Black());
		this.setWhitePlayer(new White());
		this.setAdversaryToMove(this.getWhitePlayer());
	    this.setComputerAdversary(this.getBlackPlayer());
	    this.getBoard().getBoardModel().reset(this.getWhitePlayer(), this.getBlackPlayer());
	    System.out.println(this.getBoard().getBoardModel());
	    this.getChessGameView().getBoardView().updateBoardView();
		if (listModel != null) {
			listModel.removeAllElements();
		}
	}
	JPanel createMoveListContainer() {
		JPanel moveListContainer = new JPanel();
		moveListContainer.setPreferredSize(
			new Dimension(
				200,
				(int) this.getChessGameView().getPreferredSize().getHeight()
			)
		);
		JList<String> moveList = new JList<>();
		moveList.setBackground(Color.LIGHT_GRAY);
		this.listModel = new DefaultListModel<>();
		moveList.setModel(this.listModel);
		moveListContainer.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(moveList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		moveListContainer.add(scrollPane, BorderLayout.CENTER);
		moveListContainer.add(new JScrollPane(moveList), BorderLayout.CENTER);
		this.getBoard().getBoardView().setListModel(this.listModel);
		return moveListContainer;
	}
	JToolBar createToolBar() {
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.setLayout(new FlowLayout());
		jtb.setBackground(Color.GRAY);
		JButton undoButton = new JButton("Undo Move");
		JButton newGameButton = new JButton("New Game");
		undoButton.addActionListener(
			ae -> {
				if (getComputerAdversary() != getAdversaryToMove()) {
					BoardModel bm = getChessGameModel().getBoardModel();
					if (bm.isSafeToUndo()) {
						bm.undoMove();
						bm.undoMove();
						getChessGameView().getBoardView()
					    	.updateBoardView();
					    if (this.listModel != null) {	
					    	this.listModel.removeElementAt(this.listModel.getSize() - 1);
					    	this.listModel.removeElementAt(this.listModel.getSize() - 1);
						}
					}
				}		  
		});
		newGameButton.addActionListener(ae -> this.reset());
		jtb.add(undoButton);
		jtb.add(newGameButton);
		return jtb;
	}
	public boolean isCheckMate() {
		return blackPlayer.isCheckMate(getBoard().getBoardModel()) 
			|| whitePlayer.isCheckMate(getBoard().getBoardModel());
	}
	public AdversaryModel getComputerAdversary() {
		return this.computerAdversary;
	}
	public void setComputerAdversary(AdversaryModel adv) {
		this.computerAdversary = adv;
	}
	public void setAdversaryToMove(AdversaryModel adv) {
		this.adversaryToMove = adv;
	}
	public AdversaryModel getAdversaryToMove() {
		return this.adversaryToMove;
	}
	public Board getBoard() {
		return this.board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public void setBlackPlayer(AdversaryModel adv) {
		this.blackPlayer = adv;
	}
	public void setWhitePlayer(AdversaryModel adv) {
		this.whitePlayer = adv;
	}
	public AdversaryModel getWhitePlayer() {
		return this.whitePlayer;
	}
	public AdversaryModel getBlackPlayer() {
		return this.blackPlayer;
	}
	public ChessGameModel getChessGameModel() {
		return this.chessGameModel;
	}
	private void setChessGameModel(ChessGameModel chessGameModel) {
		this.chessGameModel = chessGameModel;
	}
	public ChessGameView getChessGameView() {
		return this.chessGameView;
	}
	private void setChessGameView(ChessGameView chessGameView) {
		this.chessGameView = chessGameView;
	}
}
