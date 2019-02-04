package com.yahoo.slykhachov.chess.model;

import com.yahoo.slykhachov.chess.Black;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.White;

public enum PawnState {
	YET_TO_BE_MOVED {
		@Override
		Move[] generateAllPossibleMoves(BoardModel b, PawnModel p) {
			if (p.isCaptured()) {
				throw new UnsupportedOperationException();
			}
			Move[] allMoves = new Move[4];
			PieceModel[][] board = b.getBoard();
			int size = 0;
			Move northEast = diagonalMoveGenerator(p, board, DiagonalMoveDirection.NORTH_EAST);
			allMoves[size] = northEast;
			size = northEast != null ? ++size : size;
			Move north = northMoveGenerator(p, board, StraightLineMoveDirection.NORTH);
			allMoves[size] = north;
			size = north != null ? ++size : size;
			Move northWest = diagonalMoveGenerator(p, board, DiagonalMoveDirection.NORTH_WEST);
			allMoves[size] = northWest;
			size = northWest != null ? ++size : size;
			Move farNorth = northMoveGenerator(p, board, StraightLineMoveDirection.FAR_NORTH);
			allMoves[size] = farNorth;
			size = farNorth != null ? ++size : size;
			return extractNonNullsOnly(size, allMoves);
		}
	},
	EN_PASSANT_VULNERABLE, DEVELOPED, PROMOTED {
		@Override
		Move[] generateAllPossibleMoves(BoardModel b, PawnModel p) {
			throw new UnsupportedOperationException();
		}
	}, CAPTURED_EN_PASSANT {
		@Override
		Move[] generateAllPossibleMoves(BoardModel b, PawnModel p) {
			throw new UnsupportedOperationException();
		}
	};
	private static Move northMoveGenerator(PawnModel pawn, PieceModel[][] board, 
			StraightLineMoveDirection direction) {
		if (pawn.getAdversary().equals(Black.class)) {
			if (direction == StraightLineMoveDirection.FAR_NORTH) {	
				if (board[pawn.getRow() + 2][pawn.getCol()] == null
						&& board[pawn.getRow() + 1][pawn.getCol()] == null) {
					return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() + 2, pawn.getCol(), null);
				} else {
					return null;
				}
			} else {
				if (board[pawn.getRow() + 1][pawn.getCol()] == null) {
					return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol(), null);
				} else {
					return null;
				}
			}
		} else {
			if (direction == StraightLineMoveDirection.FAR_NORTH) {
				if (board[pawn.getRow() - 2][pawn.getCol()] == null
						&& board[pawn.getRow() - 1][pawn.getCol()] == null) {
					return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() - 2, pawn.getCol(), null);
				} else {
					return null;
				}
			} else {
				if (board[pawn.getRow() - 1][pawn.getCol()] == null) {
					return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol(), null);
				} else {
					return null;
				}
			}
		}
	}
	private static Move diagonalMoveGenerator(PawnModel pawn, PieceModel[][] board,
			DiagonalMoveDirection direction) {
		boolean isBlackCondition = pawn.getAdversary().equals(Black.class);
		int rowExtrema = isBlackCondition ? 7 : 0;
		int colExtremaRight = isBlackCondition ? 0 : 7;
		int colExtremaLeft = isBlackCondition ? 7 : 0;
		if (pawn.getRow() != rowExtrema) {		 
			if (isBlackCondition) {
				if (direction.equals(DiagonalMoveDirection.NORTH_EAST)) {
					if (pawn.getCol() != colExtremaRight) {
						PieceModel piece = board[pawn.getRow() + 1][pawn.getCol() - 1];
						if (piece != null) {
							return pawn.getAdversary().equals(piece.getAdversary()) ? null : new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() - 1, null);
						} else {
							PieceModel p = board[pawn.getRow()][pawn.getCol() - 1];
							if (p != null && p.getAdversary().equals(White.class)) {
								if (p.getClass().equals(PawnModel.class) && ((PawnModel) p).getCurrentState() == EN_PASSANT_VULNERABLE) {
									return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() - 1, /*(PawnModel)*/ p);
								} else {
									return null;
								}
							} else {
								return null;
							}
						}
					} else {
						return null;
					}
				} else {
					if (pawn.getCol() != colExtremaLeft) {
						PieceModel piece = board[pawn.getRow() + 1][pawn.getCol() + 1];
						if (piece != null) {
							return pawn.getAdversary().equals(piece.getAdversary()) ? null : new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() + 1, null);
						} else {
							PieceModel p = board[pawn.getRow()][pawn.getCol() + 1];
							if (p != null && p.getAdversary().equals(White.class)) {
								if (p.getClass().equals(PawnModel.class) && ((PawnModel) p).getCurrentState() == EN_PASSANT_VULNERABLE) {
									return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() + 1, pawn.getCol() + 1, /*(PawnModel)*/ p);
								} else {
									return null;
								}
							} else {
								return null;
							}									
						}
					} else {
						return null;
					}
				}
			} else {	
				if (direction.equals(DiagonalMoveDirection.NORTH_EAST)) {
					if (pawn.getCol() != colExtremaRight) {
						PieceModel piece = board[pawn.getRow() - 1][pawn.getCol() + 1];
						if (piece != null) {
							return pawn.getAdversary().equals(piece.getAdversary()) ? null : new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() + 1, null);
						} else {
							PieceModel p = board[pawn.getRow()][pawn.getCol() + 1];
							if (p != null && p.getAdversary().equals(Black.class)) {
								if (p.getClass().equals(PawnModel.class) && ((PawnModel) p).getCurrentState() == EN_PASSANT_VULNERABLE) {
									return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() + 1, /*(PawnModel)*/ p);
								} else {
									return null;
								}
							} else {
								return null;
							}				
						}
					} else {
						return null;
					}
				} else {
					if (pawn.getCol() != colExtremaLeft) {
						PieceModel piece = board[pawn.getRow() - 1][pawn.getCol() - 1];
						if (piece != null) {
							return pawn.getAdversary().equals(piece.getAdversary()) ? null : new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() - 1, null);
						} else {
							PieceModel p = board[pawn.getRow()][pawn.getCol() - 1];
							if (p != null && p.getAdversary().equals(Black.class)) {
								if (p.getClass().equals(PawnModel.class) && ((PawnModel) p).getCurrentState() == EN_PASSANT_VULNERABLE) {
									return new Move(pawn, pawn.getRow(), pawn.getCol(), pawn.getRow() - 1, pawn.getCol() - 1, /*(PawnModel)*/ p);
								} else {
									return null;
								}
							} else {
								return null;
							}							
						}
					} else {
						return null;
					}
				}		
			}
		}
		return null;
	}
	Move[] generateAllPossibleMoves(BoardModel b, PawnModel p) {
		if (p.isCaptured()) {
				throw new UnsupportedOperationException();
		}
		Move[] allMoves = new Move[3];
		PieceModel[][] board = b.getBoard();
		int size = 0;
		Move northEast = diagonalMoveGenerator(p, board, DiagonalMoveDirection.NORTH_EAST);
		allMoves[size] = northEast;
		size = northEast != null ? ++size : size;
		Move north = northMoveGenerator(p, board, StraightLineMoveDirection.NORTH);
		allMoves[size] = north;
		size = north != null ? ++size : size;
		Move northWest = diagonalMoveGenerator(p, board, DiagonalMoveDirection.NORTH_WEST);
		allMoves[size] = northWest;
		size = northWest != null ? ++size : size;
		return extractNonNullsOnly(size, allMoves);
	}
	private static Move[] extractNonNullsOnly(int size, Move[] allMoves) {
		Move[] moves = new Move[size];
		int index = 0;
		for (Move move : allMoves) {
			if (move != null) {
				moves[index] = move;
				index++;
			}
		}
		return moves;
	}
	private static enum DiagonalMoveDirection {
		NORTH_EAST, NORTH_WEST;
	}
	private static enum StraightLineMoveDirection {
		NORTH, FAR_NORTH;
	}
}
