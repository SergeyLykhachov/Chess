package com.yahoo.slykhachov.chess.model;

import java.util.List;
import com.yahoo.slykhachov.chess.Move;

public interface AdversaryModel {
	PieceModel[] getPieces();
	AdversaryModel getOpponent();
	PawnModel getEnPassantVulnerablePawn();
	List<Move> generateAllPossibleMoves(BoardModel board);
	List<Move> generateAllPossibleLegalMoves(BoardModel board);
	boolean isCheckMate(BoardModel board);
	boolean isChecked(BoardModel board);
	boolean isStaleMate(BoardModel board);
	void setOpponent(AdversaryModel a);
	void setEnPassantVulnerablePawn(PawnModel p);
}
