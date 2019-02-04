package com.yahoo.slykhachov.chess.model;

import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.Black;

public final class KnightModel extends AbstractPieceModel {
	public KnightModel(String s, Class<? extends AdversaryModel> adversary) {
		super(s, adversary);
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel b) {
		return MoveGenerators.knightMoves(b, this);
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Black.class) ? "B" : "W";
		return s + "N";
	}
}
