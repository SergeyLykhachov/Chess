package com.yahoo.slykhachov.chess.function;

import com.yahoo.slykhachov.chess.model.PieceModel;

@FunctionalInterface
public interface MyTriPredicate {
	boolean test(PieceModel[][] board, int row, int col);
}
