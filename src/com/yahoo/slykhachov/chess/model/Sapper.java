package com.yahoo.slykhachov.chess.model;

import java.util.function.Predicate;
import com.yahoo.slykhachov.chess.White;
import com.yahoo.slykhachov.chess.function.*;

class Sapper {
	private Sapper() {}
	public static boolean isSpotUnderAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		return isSpotUnderBishopOrQueenAttack(board, adversary, row, col)
			|| isSpotUnderRookOrQueenAttack(board, adversary, row, col)
			|| isSpotUnderKnightAttack(board, adversary, row, col)
			|| isSpotUnderPawnAttack(board, adversary, row, col)
			|| isSpotUnderKingAttack(board, adversary, row, col);
	}
	private static boolean isSpotUnderBishopOrQueenAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		return isUnderDiagonallyMovingPieceAttack(//northEast
				board,
				adversary,
				row,
				col,
				0,
				7,
				(brd, r, c, val) -> brd[r - val][c + val],
				(r, c, limRow, limCol, val) -> r - val > limRow && c + val < limCol
			) 
			|| isUnderDiagonallyMovingPieceAttack(//northWest
				board,
				adversary,
				row,
				col,
				0,
				0,
				(brd, r, c, val) -> brd[r - val][c - val],
				(r, c, limRow, limCol, val) -> r - val > limRow && c - val > limCol
			) 
			|| isUnderDiagonallyMovingPieceAttack(//southWest
				board,
				adversary,
				row,
				col,
				7,
				0,
				(brd, r, c, val) -> brd[r + val][c - val],
				(r, c, limRow, limCol, val) -> r + val < limRow && c - val > limCol
			) 
			|| isUnderDiagonallyMovingPieceAttack(//southEast
				board,
				adversary,
				row,
				col,
				7,
				7,
				(brd, r, c, val) -> brd[r + val][c + val],
				(r, c, limRow, limCol, val) -> r + val < limRow && c + val < limCol
		);	
	}
	private static boolean isUnderDiagonallyMovingPieceAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col,
			int rowExtrema, int colExtrema, MyQuadFunction<PieceModel[][], PieceModel> squareMapper,
			MyQuintPredicate quintPredicate) {
		if (row != rowExtrema && col != colExtrema) {
			byte value = 0;
			do {
				value++;
				PieceModel p = squareMapper.apply(board, row, col, value);
				if (p != null) {                                           
					if (p.getAdversary().equals(adversary)) {
						if (p.getClass().equals(KingModel.class)) {
							continue;
						} else {
							return false;
						}
					} else {
						if (p.getClass().equals(BishopModel.class) 
								|| p.getClass().equals(QueenModel.class)                                                  
								|| (p.getClass().equals(PawnModel.class) 
									&& ((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED) 
									&& (((PawnModel) p).getPromotedStateMoveGenerator().equals(PawnModel.QUEEN_MOOVE_GENERATOR)
										|| ((PawnModel) p).getPromotedStateMoveGenerator().equals(PawnModel.BISHOP_MOOVE_GENERATOR)))) {
							//System.out.println("It's " + p.toString() + "; moves diagonally");
							return true;
						} else {
							return false;
						}
					}
				}
			} while (quintPredicate.test(row, col, rowExtrema, colExtrema, value));
		}
		return false;
	}
	private static boolean isSpotUnderRookOrQueenAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		return isUnderLinearlyMovingPieceAttack(//north
				0,
				row,
				col,
				board,
				adversary,
				(i, j, k) -> i != k,
				e -> --e,
				(b, r, c, val) -> b[r + val][c],
				(r, c, val, lim) -> r + val > lim
			)
			|| isUnderLinearlyMovingPieceAttack(//west
				0,
				row,
				col,
				board,
				adversary,
				(i, j, k) -> j != k,
				e -> --e,
				(b, r, c, val) -> b[r][c + val],
				(r, c, val, lim) -> c + val > lim
			)
			|| isUnderLinearlyMovingPieceAttack(//south
				7,
				row,
				col,
				board,
				adversary,
				(i, j, k) -> i != k,
				e -> ++e,
				(b, r, c, val) -> b[r + val][c],
				(r, c, val, lim) -> r + val < lim
			)
			|| isUnderLinearlyMovingPieceAttack(//east
				7,
				row,
				col,
				board,
				adversary,
				(i, j, k) -> j != k,
				e -> ++e,
				(b, r, c, val) -> b[r][c + val],
				(r, c, val, lim) ->	c + val < lim
		);
	}
	private static boolean isUnderLinearlyMovingPieceAttack(int limit, int row, 
			int col, PieceModel[][] board, Class<? extends AdversaryModel> adversary,
			MyTriIntPredicate initialTester, ByteFunction advanceFunction,
			MyQuadFunction<PieceModel[][], PieceModel> squareMapper, 
			MyQuadIntPredicate condition) {
		if (initialTester.test(row, col, limit)) {
 			byte value = 0;
 			do {
 				value = advanceFunction.apply(value);
 				PieceModel p = squareMapper.apply(board, row, col, value);                                                                                                                                                                      
 				if (p != null) {                                           
					if (p.getAdversary().equals(adversary)) {																						
						if (p.getClass().equals(KingModel.class)) {
							continue;
						} else {
							return false;
						}																																															        
					} else {
						if (p.getClass().equals(RookModel.class) 
								|| p.getClass().equals(QueenModel.class)                                                  
								|| (p.getClass().equals(PawnModel.class) 
									&& ((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED) 
									&& (((PawnModel) p).getPromotedStateMoveGenerator().equals(PawnModel.QUEEN_MOOVE_GENERATOR)
										|| ((PawnModel) p).getPromotedStateMoveGenerator().equals(PawnModel.ROOK_MOOVE_GENERATOR)))) {
							return true;
						} else {
							return false;
						}
					}
				}
 			} while (condition.test(row, col, value, limit));
 		}
 		return false;
 	}
	private static boolean isSpotUnderKnightAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		return isOpponentsKnightAtParticularSpot(board, adversary, row - 1, col + 2)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row - 2, col + 1)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row - 2, col - 1)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row - 1, col - 2)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row + 1, col - 2)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row + 2, col - 1)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row + 2, col + 1)
			   || isOpponentsKnightAtParticularSpot(board, adversary, row + 1, col + 2);
	}
	private static boolean isOpponentsKnightAtParticularSpot(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		if (((row <= 7) && (row >= 0)) && ((col <= 7) && (col >= 0))) {
			PieceModel p = board[row][col];
			if (p != null) {
				if (adversary.equals(p.getAdversary())) {
					return false;
				} else {
					if (p.getClass().equals(KnightModel.class)) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
		}
		return false;
	}
	private static boolean isSpotUnderPawnAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		if (adversary.equals(White.class)) {
			return isSpotUnderPawnAttack(
				board,
				adversary,
				row,
				col,
				1,
				(a, b) -> a <= b,
				(brd, r, c) -> brd[r - 1][c + 1],
				(brd, r, c) -> brd[r - 1][c - 1]
			);
		} else {
			return isSpotUnderPawnAttack(
				board,
				adversary,
				row,
				col,
				6,
				(a, b) -> a >= b,
				(brd, r, c) -> brd[r + 1][c + 1],
				(brd, r, c) -> brd[r + 1][c - 1]
			);
		}
	}
	private static boolean isSpotUnderPawnAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col, int limRow,
			MyBiPredicate condition1, TriFunction<PieceModel[][], PieceModel> squareMapper1,
			TriFunction<PieceModel[][], PieceModel> squareMapper2) {
		if (condition1.test(row, limRow)) {
			return false;
		} else {
			if (col != 0 && col != 7) {
				return anySpecificOpponentPieceFound(
					p -> p.getClass().equals(PawnModel.class) 
						&& (!(((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED))),
					adversary,
					squareMapper1.apply(board, row, col),
					squareMapper2.apply(board, row, col)
				);
			} else {
				if (col == 0) {
					return anySpecificOpponentPieceFound(
						p -> p.getClass().equals(PawnModel.class) 
							&& (!(((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED))),
						adversary,
						squareMapper1.apply(board, row, col)
					);
				} else {
					return anySpecificOpponentPieceFound(
						p -> p.getClass().equals(PawnModel.class) 
							&& (!(((PawnModel) p).getCurrentState().equals(PawnState.PROMOTED))),
						adversary,
						squareMapper2.apply(board, row, col)
					);
				}
			}
		}
	}
	private static boolean anySpecificOpponentPieceFound(Predicate<PieceModel> predicate,
			Class<? extends AdversaryModel> adversary, PieceModel... pieces) {
		boolean value = false;
		for (PieceModel piece : pieces) {
			if (piece != null) {
				if (!piece.getAdversary().equals(adversary)) {
					if (predicate.test(piece)) {
						value = true;
						break;
					}
				}
			}
		}
		return value;
	}
	private static boolean isSpotUnderKingAttack(PieceModel[][] board,
			Class<? extends AdversaryModel> adversary, int row, int col) {
		return anySpecificOpponentPieceFound(
			p -> p.getClass().equals(KingModel.class),
			adversary,
			getSpotType(row, col).getPieces(board, row, col)
		);
	}
	private static SpotType getSpotType(int row, int col) {
		if (row == 0) {
			if (col == 0) {
				return SpotType.UPPER_LEFT_CORNER_SPOT;
			} else {
				if (col == 7) {
					return SpotType.UPPER_RIGHT_CORNER_SPOT;
				} else {
					return SpotType.UPPER_ROW_SPOT;
				}
			}
		} else {
			if (row == 7) {
				if (col == 0) {
					return SpotType.LOWER_LEFT_CORNER_SPOT;
				} else {
					if (col == 7) {
						return SpotType.LOWER_RIGHT_CORNER_SPOT;
					} else {
						return SpotType.LOWER_ROW_SPOT;
					}
				}
			} else {
				if (col == 0) {
					return SpotType.LEFT_ROW_SPOT;
				} else {
					if (col == 7) {
						return SpotType.RIGHT_ROW_SPOT;
					} else {
						return SpotType.INNER_SPOT;
					}
				}
			}
		}
	}
	private static enum SpotType {
		UPPER_LEFT_CORNER_SPOT(0) {	
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				return pieces;
			}
		}, 
		UPPER_ROW_SPOT(5) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				pieces[0] = board[row][col + 1];
				pieces[1] = board[row + 1][col + 1];
				pieces[2] = board[row + 1][col];
				pieces[3] = board[row + 1][col - 1];
				pieces[4] = board[row][col - 1];
				return pieces;
			}
		},
		UPPER_RIGHT_CORNER_SPOT(0) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				return pieces;
				
			}
		}, 
		RIGHT_ROW_SPOT(5) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				pieces[0] = board[row - 1][col];
				pieces[1] = board[row - 1][col - 1];
				pieces[2] = board[row][col - 1];
				pieces[3] = board[row + 1][col - 1];
				pieces[4] = board[row + 1][col];
				return pieces;
			
			}
		}, 
		LOWER_RIGHT_CORNER_SPOT(0) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				return pieces;
			}
		},
		LOWER_ROW_SPOT(5) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				pieces[0] = board[row][col + 1];
				pieces[1] = board[row - 1][col + 1];
				pieces[2] = board[row - 1][col];
				pieces[3] = board[row - 1][col - 1];
				pieces[4] = board[row][col - 1];
				return pieces;
			}
		}, 
		LOWER_LEFT_CORNER_SPOT(0) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				return pieces;
			}
		}, 
		LEFT_ROW_SPOT(5) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				pieces[0] = board[row + 1][col];
				pieces[1] = board[row + 1][col + 1];
				pieces[2] = board[row][col + 1];
				pieces[3] = board[row - 1][col + 1];
				pieces[4] = board[row - 1][col];
				return pieces;
			}
		},
		INNER_SPOT(8) {
			PieceModel[] getPieces(PieceModel[][] board, int row, int col) {
				PieceModel[] pieces = new PieceModel[this.bucketNum];
				pieces[0] = board[row - 1][col + 1];
				pieces[1] = board[row - 1][col];
				pieces[2] = board[row - 1][col - 1];
				pieces[3] = board[row][col - 1];
				pieces[4] = board[row + 1][col - 1];
				pieces[5] = board[row + 1][col];
				pieces[6] = board[row + 1][col + 1];
				pieces[7] = board[row][col + 1];
				return pieces;
			}
		};
		final int bucketNum;
		SpotType(int bucketNum) {
			this.bucketNum = bucketNum;
		}
		abstract PieceModel[] getPieces(PieceModel[][] board, int row, int col);
	}
}
