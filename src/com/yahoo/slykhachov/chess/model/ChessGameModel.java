package com.yahoo.slykhachov.chess.model;

public class ChessGameModel {
	private BoardModel boardModel;
	public ChessGameModel(BoardModel boardModel) {
		this.boardModel = boardModel;
	}
	public BoardModel getBoardModel() {
		return this.boardModel;
	}
}
