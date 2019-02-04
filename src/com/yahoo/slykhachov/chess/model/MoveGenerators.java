package com.yahoo.slykhachov.chess.model;

import java.util.Arrays;
import java.util.ArrayList;
import com.yahoo.slykhachov.chess.Move;
import com.yahoo.slykhachov.chess.function.*;

class MoveGenerators {
	private MoveGenerators() {}
	static Move[] rookMoves(BoardModel b, PieceModel piece) {
		if (piece.isCaptured()) {
			throw new UnsupportedOperationException();
		}
		PieceModel[][] board = b.getBoard();
		return turnNumbersIntoMoves(
			piece,
			SpecializedTriple.of(
				lineMoveGenerator(//south
 					piece, 
 					7, 
 					board,
 					PieceModel::getRow,
					e -> ++e,
					e -> --e,
					(i, j) -> i < j,
					(brd, p, val) -> brd[p.getRow() + val][p.getCol()]
				),
				(p, dir) -> p.getRow() + dir,
				(p, dir) -> p.getCol()
			),
			SpecializedTriple.of(
				lineMoveGenerator(//north
 					piece, 
 					0, 
 					board,
 					PieceModel::getRow,
					e -> --e,
					e -> ++e,
					(i, j) -> i > j,
					(brd, p, val) -> brd[p.getRow() + val][p.getCol()]
				),
				(p, dir) -> p.getRow() - dir,
				(p, dir) -> p.getCol()
			),
			SpecializedTriple.of(
				lineMoveGenerator(//east
 					piece, 
 					7, 
 					board,
 					PieceModel::getCol,
					e -> ++e,
					e -> --e,
					(i, j) -> i < j,
					(brd, p, val) -> brd[p.getRow()][p.getCol() + val]
				),
				(p, dir) -> p.getRow(),
				(p, dir) -> p.getCol() + dir
			),
			SpecializedTriple.of(
				lineMoveGenerator(//west
 					piece, 
 					0, 
 					board,
 					PieceModel::getCol,
					e -> --e,
					e -> ++e,
					(i, j) -> i > j,
					(brd, p, val) -> brd[p.getRow()][p.getCol() + val]
				),
				(p, dir) -> p.getRow(),
				(p, dir) -> p.getCol() - dir
			)
		);
	}
	static Move[] bishopMoves(BoardModel b, PieceModel piece) {
		if (piece.isCaptured()) {
			throw new UnsupportedOperationException();
		}
		PieceModel[][] board = b.getBoard();
		return turnNumbersIntoMoves(
			piece,
			SpecializedTriple.of(
				diagonalMoveGenerator(//northEast
 					piece,
 					board,
 					0,
 					7,
 					(brd, p, val) -> brd[p.getRow() - val][p.getCol() + val],
 					(p, row, col, val) -> p.getRow() - val > row && p.getCol() + val < col
 				),
 				(p, dir) -> p.getRow() - dir,
				(p, dir) -> p.getCol() + dir		 	
			),
			SpecializedTriple.of(
				diagonalMoveGenerator(//northWest
 					piece,
 					board,
 					0,
 					0,
 					(brd, p, val) -> brd[p.getRow() - val][p.getCol() - val],
 					(p, row, col, val) -> p.getRow() - val > row && p.getCol() - val > col
 				),
 				(p, dir) -> p.getRow() - dir,
				(p, dir) -> p.getCol() - dir
			),
			SpecializedTriple.of(
				diagonalMoveGenerator(//southWest
 					piece,
 					board,
 					7,
 					0,
 					(brd, p, val) -> brd[p.getRow() + val][p.getCol() - val],
 					(p, row, col, val) -> p.getRow() + val < row && p.getCol() - val > col
 				),
 				(p, dir) -> p.getRow() + dir,
				(p, dir) -> p.getCol() - dir
			),
			SpecializedTriple.of(
				diagonalMoveGenerator(//southEast
 					piece,
 					board,
 					7,
 					7,
 					(brd, p, val) -> brd[p.getRow() + val][p.getCol() + val],
 					(p, row, col, val) -> p.getRow() + val < row && p.getCol() + val < col
 				),
 				(p, dir) -> p.getRow() + dir,
				(p, dir) -> p.getCol() + dir
			)
		);
	}
	static Move[] knightMoves(BoardModel b, PieceModel piece) {
		if (piece.isCaptured()) {
			throw new UnsupportedOperationException();
		}
		PieceModel[][] board = b.getBoard();
		ArrayList<Move> list = new ArrayList<>();
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() - 1, piece.getCol() + 2, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() - 2, piece.getCol() + 1, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() - 2, piece.getCol() - 1, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() - 1, piece.getCol() - 2, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() + 1, piece.getCol() - 2, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() + 2, piece.getCol() - 1, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() + 2, piece.getCol() + 1, list);
		generateParticularMoveIfAvailableAddToList(board, piece, piece.getRow() + 1, piece.getCol() + 2, list);
		Object[] objectArray = list.toArray();
		return Arrays.copyOf(objectArray, objectArray.length, Move[].class);
	}
	private static void generateParticularMoveIfAvailableAddToList(PieceModel[][] board,
			PieceModel piece, int row, int col, ArrayList<Move> list) {
		if (((row <= 7) && (row >= 0)) && ((col <= 7) && (col >= 0))) {
			PieceModel p = board[row][col];
			if (p != null) {
				if (piece.getAdversary().equals(p.getAdversary())) {
					return;
				} else {
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
			} else {
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
	static Move[] queenMoves(BoardModel b, PieceModel piece) {
		if (piece.isCaptured()) {
			throw new UnsupportedOperationException();
		}
		PieceModel[][] board = b.getBoard();
		return turnNumbersIntoMoves(
			piece,
			SpecializedTriple.of(
				diagonalMoveGenerator(//northEast
 					piece,
 					board,
 					0,
 					7,
 					(brd, p, val) -> brd[p.getRow() - val][p.getCol() + val],
 					(p, row, col, val) -> p.getRow() - val > row && p.getCol() + val < col
 				),
 				(p, dir) -> p.getRow() - dir,
				(p, dir) -> p.getCol() + dir
			),
			SpecializedTriple.of(
				diagonalMoveGenerator(//northWest
 					piece,
 					board,
 					0,
 					0,
 					(brd, p, val) -> brd[p.getRow() - val][p.getCol() - val],
 					(p, row, col, val) -> p.getRow() - val > row && p.getCol() - val > col
 				),
 				(p, dir) -> p.getRow() - dir,
				(p, dir) -> p.getCol() - dir
			),
			SpecializedTriple.of(
				diagonalMoveGenerator(//southWest
 					piece,
 					board,
 					7,
 					0,
 					(brd, p, val) -> brd[p.getRow() + val][p.getCol() - val],
 					(p, row, col, val) -> p.getRow() + val < row && p.getCol() - val > col
 				),
 				(p, dir) -> p.getRow() + dir,
				(p, dir) -> p.getCol() - dir
			),
			SpecializedTriple.of(
				diagonalMoveGenerator(//southEast
 					piece,
 					board,
 					7,
 					7,
 					(brd, p, val) -> brd[p.getRow() + val][p.getCol() + val],
 					(p, row, col, val) -> p.getRow() + val < row && p.getCol() + val < col
 				),
 				(p, dir) -> p.getRow() + dir,
				(p, dir) -> p.getCol() + dir
			),
			SpecializedTriple.of(
				lineMoveGenerator(//south
 					piece, 
 					7, 
 					board,
 					PieceModel::getRow,
					e -> ++e,
					e -> --e,
					(i, j) -> i < j,
					(brd, p, val) -> brd[p.getRow() + val][p.getCol()]
				),
				(p, dir) -> p.getRow() + dir,
				(p, dir) -> p.getCol()
			),
			SpecializedTriple.of(
				lineMoveGenerator(//north
 					piece, 
 					0, 
 					board,
 					PieceModel::getRow,
					e -> --e,
					e -> ++e,
					(i, j) -> i > j,
					(brd, p, val) -> brd[p.getRow() + val][p.getCol()]
				),
				(p, dir) -> p.getRow() - dir,
				(p, dir) -> p.getCol()
			),
			SpecializedTriple.of(
				lineMoveGenerator(//east
 					piece, 
 					7, 
 					board,
 					PieceModel::getCol,
					e -> ++e,
					e -> --e,
					(i, j) -> i < j,
					(brd, p, val) -> brd[p.getRow()][p.getCol() + val]
				),
				(p, dir) -> p.getRow(),
				(p, dir) -> p.getCol() + dir
			),
			SpecializedTriple.of(
				lineMoveGenerator(//west
 					piece, 
 					0, 
 					board,
 					PieceModel::getCol,
					e -> --e,
					e -> ++e,
					(i, j) -> i > j,
					(brd, p, val) -> brd[p.getRow()][p.getCol() + val]
				),
				(p, dir) -> p.getRow(),
				(p, dir) -> p.getCol() - dir
			)
		);
	}
	private static Move[] turnNumbersIntoMoves(PieceModel piece, SpecializedTriple... triples) {
		int num = 0;
		for (SpecializedTriple t : triples) {
			num = num + t.getLeft();
		}
		Move[] moves = new Move[num];
		num = 0;
		for (SpecializedTriple t : triples) {
			byte direction = t.getLeft();
			while (direction != 0) {
				moves[num] = new Move(
					piece,
					piece.getRow(),
					piece.getCol(),
					t.getMiddle().apply(piece, direction),
					t.getRight().apply(piece, direction),
					null
				);
				direction--;
				num++;
			}
		}
		return moves;
	}
	private static byte lineMoveGenerator(PieceModel piece, int lim, PieceModel[][] board,
			MyFunction<PieceModel> pieceFunction, ByteFunction firstFunction,
			ByteFunction secondFunction, MyBiPredicate condition,
			MyTriFunction<PieceModel[][], PieceModel, PieceModel> squareMapper) {
		byte value = 0;
		if (pieceFunction.apply(piece) != lim) {
 			do {
 				value = firstFunction.apply(value);
 				PieceModel p = squareMapper.apply(board, piece, value);
 				if (p != null) {                                           
					if (!(p.getAdversary().equals(piece.getAdversary()))) {
						break;
					} else {
						value = secondFunction.apply(value);
						break;
					}
				}
 			} while (condition.test(pieceFunction.apply(piece) + value, lim));
 		}
 		return (byte) Math.abs(value);
	}
	private static byte diagonalMoveGenerator(PieceModel piece, PieceModel[][] board,
			int rowExtrema, int colExtrema,
			MyTriFunction<PieceModel[][], PieceModel, PieceModel> squareMapper,
			MyQuadPredicate<PieceModel> quadPredicate) {
		byte value = 0;
		if (piece.getRow() != rowExtrema && piece.getCol() != colExtrema) {
			do {
				value++;
				PieceModel p = squareMapper.apply(board, piece, value);
				if (p != null) {                                           
					if (!(p.getAdversary().equals(piece.getAdversary()))) {
						break;
					} else {
						value--;
						break;
					}
				}
			} while (quadPredicate.test(piece, rowExtrema, colExtrema, value));
		}
		return value;
	}
	private static class SpecializedTriple {
		private byte left;
		private  BiFunction<PieceModel> middle;
		private  BiFunction<PieceModel> right;
		private SpecializedTriple(byte left,  BiFunction<PieceModel> middle, 
				BiFunction<PieceModel> right) {
			this.left = left;
			this.middle = middle;
			this.right = right;
		}
		public static SpecializedTriple of(byte left, BiFunction<PieceModel> middle,
				BiFunction<PieceModel> right) {
			return new SpecializedTriple(left, middle, right);
		}
		public byte getLeft() {
			return this.left;
		}
		public BiFunction<PieceModel> getMiddle() {
			return this.middle;
		}
		public BiFunction<PieceModel> getRight() {
			return this.right;
		}
	}
}
