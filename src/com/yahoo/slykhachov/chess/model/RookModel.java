package com.yahoo.slykhachov.chess.model;

import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.Black;

public final class RookModel extends AbstractPieceModel {
	private int numberOfMovesMade;
	public RookModel(String s, Class<? extends AdversaryModel> adversary) {
		super(s, adversary);
		numberOfMovesMade = 0;
	}
	public boolean hasEverBeenMoved() {
		return numberOfMovesMade != 0;
	}
	public void incrementNumberOfMovesMade() {
		this.numberOfMovesMade++;
	}
	public void decrementNumberOfMovesMade() {
		if (numberOfMovesMade < 0) {
			throw new IllegalStateException();
		}
		this.numberOfMovesMade--;
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel b) {
		return MoveGenerators.rookMoves(b, this);
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Black.class) ? "B" : "W";
		return s + "R";
	}
}
