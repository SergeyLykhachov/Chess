package com.yahoo.slykhachov.chess;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.yahoo.slykhachov.chess.ai.MoveChooser;
import com.yahoo.slykhachov.chess.model.AdversaryModel;
import com.yahoo.slykhachov.chess.model.ChessGameModel;
import com.yahoo.slykhachov.chess.view.ChessGameView;

public class ChessGame {
	public static final int SEARCH_DEPTH = 4;
	private ChessGameModel chessGameModel;
	private ChessGameView chessGameView;
	private Board board;
	private AdversaryModel computerAdversary;
	//private AdversaryModel humanAdversary;
	private AdversaryModel whitePlayer;
	private AdversaryModel blackPlayer;
	private AdversaryModel adversaryToMove;
	private ExecutorService pool;
	public ChessGame() {
		this.pool = Executors.newCachedThreadPool();
		setBlackPlayer(new Black());
		setWhitePlayer(new White());
		setBoard(new Board(blackPlayer, whitePlayer, this));
		setChessGameModel(new ChessGameModel(getBoard().getBoardModel()));
		setChessGameView(new ChessGameView(getBoard().getBoardView()));
	    setAdversaryToMove(whitePlayer);
	    setComputerAdversary(blackPlayer);
	    //setComputerAdversary(whitePlayer);
	    if (getComputerAdversary() == whitePlayer) {
	    	pool.submit(
				() -> {
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
					getBoard().getBoardModel().performMove(move);
					getBoard().getBoardView().updateBoardView();
					if (isGameOver()) {
						System.out.println("Computer player won");
					} else {
						if (getComputerAdversary().getOpponent()
								.generateAllPossibleLegalMoves(
									getBoard().getBoardModel()
							    ).size() == 0
						) {
							System.out.println("Stall mate");
						}
					}
					setAdversaryToMove(
						getAdversaryToMove().getOpponent()
					);
				}
			);
	    }
	}
	public void doResponce() {
		if (isGameOver()) {
			System.out.println("Human player won");
		}
		pool.submit(
			() -> {
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
				getBoard().getBoardModel().performMove(move);
				getBoard().getBoardView().updateBoardView();
				if (isGameOver()) {
					System.out.println("Computer player won");
				} else {
					if (getComputerAdversary().getOpponent()
							.generateAllPossibleLegalMoves(
								getBoard().getBoardModel()
						    ).size() == 0
					) {
						System.out.println("Stall mate");
					}
				}
				setAdversaryToMove(
					getAdversaryToMove().getOpponent()
				);
			}
		);
	}
	public boolean isGameOver() {
		return blackPlayer.isCheckMated(getBoard().getBoardModel()) 
			|| whitePlayer.isCheckMated(getBoard().getBoardModel());
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
