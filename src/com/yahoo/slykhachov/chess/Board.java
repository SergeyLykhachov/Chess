package com.yahoo.slykhachov.chess;

import com.yahoo.slykhachov.chess.model.AdversaryModel;
import com.yahoo.slykhachov.chess.model.BoardModel;
import com.yahoo.slykhachov.chess.view.BoardView;

public class Board {
	private BoardView boardView;
	private BoardModel boardModel;
	public Board(AdversaryModel adv1, AdversaryModel adv2, ChessGame chessGame) {
		setBoardModel(new BoardModel(adv1, adv2));
		setBoardView(new BoardView(chessGame, getBoardModel()));
	}
	public void setBoardView(BoardView boardView) {
		this.boardView = boardView;
	}
	public BoardView getBoardView() {
		return this.boardView;
	}
	public void setBoardModel(BoardModel boardModel) {
		this.boardModel = boardModel;
	}
	public BoardModel getBoardModel() {
		return this.boardModel;
	}
}