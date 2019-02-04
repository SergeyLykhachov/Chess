package com.yahoo.slykhachov.chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.Black;

public final class KingModel extends AbstractPieceModel {
	private int numberOfMovesMade;
	public KingModel(String s, Class<? extends AdversaryModel> adversary) {
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
		if (isCaptured()) {
			throw new UnsupportedOperationException();
		}
		PieceModel[][] board = b.getBoard();
		List<Move> list = new ArrayList<>();
		generateParticularMoveAddToTheList(board, this, this.getRow() - 1, this.getCol() + 1, list);
		generateParticularMoveAddToTheList(board, this, this.getRow() - 1, this.getCol(), list);
		generateParticularMoveAddToTheList(board, this, this.getRow() - 1, this.getCol() - 1, list);
		generateParticularMoveAddToTheList(board, this, this.getRow(), this.getCol() - 1, list);
		generateParticularMoveAddToTheList(board, this, this.getRow() + 1, this.getCol() - 1, list);
		generateParticularMoveAddToTheList(board, this, this.getRow() + 1, this.getCol(), list);
		generateParticularMoveAddToTheList(board, this, this.getRow() + 1, this.getCol() + 1, list);
		generateParticularMoveAddToTheList(board, this, this.getRow(), this.getCol() + 1, list);
		Object[] objectArray = list.toArray();
		return Arrays.copyOf(objectArray, objectArray.length, Move[].class);
	}
	private static void generateParticularMoveAddToTheList(PieceModel[][] board,
			PieceModel piece, int row, int col, List<Move> list) {
		if (((row <= 7) && (row >= 0)) && ((col <= 7) && (col >= 0))) {
			PieceModel p = board[row][col];
			if (p != null) {
				if (piece.getAdversary().equals(p.getAdversary())) {
					return;
				} else {
					if (!Sapper.isSpotUnderAttack(board, piece.getAdversary(), row, col)) {
						list.add(
							new Move(
								piece,
								piece.getRow(),
								piece.getCol(),
								row,
								col,
								null
							)
						);
					}
				}
			} else {
				if (!Sapper.isSpotUnderAttack(board, piece.getAdversary(), row, col)) {
					list.add(
						new Move(
							piece,
							piece.getRow(),
							piece.getCol(),
							row,
							col,
							null
						)
					);
				}
			}
		}
	}
	public boolean isInCheck(BoardModel b) {
		return Sapper.isSpotUnderAttack(
			b.getBoard(), 
			this.getAdversary(), 
			this.getRow(), 
			this.getCol()
		);
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Black.class) ? "B" : "W";
		return s + "K";
	}
}
