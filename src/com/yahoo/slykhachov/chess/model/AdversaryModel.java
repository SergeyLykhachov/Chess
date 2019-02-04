package com.yahoo.slykhachov.chess.model;

import java.util.List;
import com.yahoo.slykhachov.chess.Move;

public interface AdversaryModel {
	PieceModel[] getPieces();
	AdversaryModel getOpponent();
	void setOpponent(AdversaryModel a);
	void setEnPassantVulnerablePawn(PawnModel p);
	PawnModel getEnPassantVulnerablePawn();
	boolean isCheckMated(BoardModel board);
	boolean isChecked(BoardModel board);
	List<Move> generateAllPossibleMoves(BoardModel board);
	List<Move> generateAllPossibleLegalMoves(BoardModel board);
}
