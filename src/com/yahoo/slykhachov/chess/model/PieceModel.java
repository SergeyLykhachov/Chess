package com.yahoo.slykhachov.chess.model;

import com.yahoo.slykhachov.chess.Move;

public interface PieceModel {
	int getRow();
	int getCol();
	void setRow(int row);
	void setCol(int col);
	boolean isCaptured();
	void setCaptured(boolean b);
	Class<? extends AdversaryModel> getAdversary();
	Move[] generateAllPossibleMoves(BoardModel board);
}
