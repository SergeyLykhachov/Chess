package com.yahoo.slykhachov.chess.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.function.IntFunction;

public abstract class AbstractAdversaryModel implements AdversaryModel {
	private final PieceModel[] pieces;
	private AdversaryModel opponent;
	private PawnModel enPassantVulnerablePawn = null;
	public AbstractAdversaryModel(PieceModel[] arrayOfPieces) {
		this.pieces = arrayOfPieces;
	}
	@Override
	public PieceModel[] getPieces() {
		return pieces;
	}
	@Override
	public AdversaryModel getOpponent() {
		return this.opponent;
	}
	@Override 
	public void setOpponent(AdversaryModel a) {
		this.opponent = a;
	}
	@Override
	public void setEnPassantVulnerablePawn(PawnModel p) {
		enPassantVulnerablePawn = p;
	}
	@Override
	public PawnModel getEnPassantVulnerablePawn() {
		return enPassantVulnerablePawn;
	}
	@Override 
	public List<Move> generateAllPossibleLegalMoves(BoardModel board) {
		List<Move> list = generateMoves(board, this);
		for (Iterator<Move> it = list.iterator(); it.hasNext();) {
			Move move = it.next();
			board.performMove(move);
			if (this.isChecked(board)) {
				it.remove();
			}
			board.undoMove();
		}
		addCastlings(board, list, this);
		return list;
	}
	@Override 
	public List<Move> generateAllPossibleMoves(BoardModel board) {
		List<Move> list = generateMoves(board, this);
		addCastlings(board, list, this);
		return list;
	}
	private static List<Move> generateMoves(BoardModel board, AdversaryModel adversary) {
		List<Move> list = new ArrayList<>();
		for (PieceModel piece : adversary.getPieces()) {
			if (!piece.isCaptured()) {
				for (Move move : piece.generateAllPossibleMoves(board)) {
					list.add(move);
				}
			}
		}
		return list;
	}
	private static void addCastlings(BoardModel board, List<Move> list,
			AdversaryModel adversary) {
		if (!((KingModel) adversary.getPieces()[15]).hasEverBeenMoved()) {
			addCastling(board, list, adversary, 7, 6, index -> ++index);
			addCastling(board, list, adversary, 0, 2, index -> --index);
		}
	}
	private static void addCastling(BoardModel board, List<Move> list, 
			AdversaryModel adversary, int rookCol, int finKingCol, IntFunction function) {
		KingModel king = (KingModel) adversary.getPieces()[15];
		int kingRow = king.getRow();
		PieceModel[][] bord = board.getBoard();
		PieceModel supposedRook = bord[kingRow][rookCol];
		if (supposedRook != null) {
			if (supposedRook.getClass().equals(RookModel.class)) {
				if (!((RookModel) supposedRook).hasEverBeenMoved()) {
					if (!adversary.isChecked(board)) {
						boolean addCastling = true;
						int testCol = function.apply(king.getCol());
						for (int lim = 0; lim < 2; lim++) {
							testCol = function.apply(testCol);
							if (bord[kingRow][testCol] != null
								|| Sapper.isSpotUnderAttack(
									board.getBoard(),
									adversary.getClass(),
									kingRow,
									testCol
								)
							) {
								addCastling = false;
								break;
							}
						}
						if (addCastling) {
							list.add(
								new Move(
									king,
									kingRow,
									king.getCol(),
									kingRow,
									finKingCol,
									supposedRook
								)
							);
						}
					}	
				}
			}
		}
	}
	@Override
	public boolean isCheckMated(BoardModel board) {
		KingModel king = (KingModel) this.pieces[15];
		if (king.isInCheck(board)) {
			if (king.generateAllPossibleMoves(board).length == 0) {
				for (Move move : this.generateAllPossibleMoves(board)) {
		            board.performMove(move);
		            if (king.isInCheck(board)) {
		            	board.undoMove();
		            } else {
		            	board.undoMove();
		            	return false;
		            }
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	@Override
	public boolean isChecked(BoardModel board) {
		return ((KingModel) this.pieces[15]).isInCheck(board);
	}
}
