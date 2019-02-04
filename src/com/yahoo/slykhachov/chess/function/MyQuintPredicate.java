package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface MyQuintPredicate {																									     						
	boolean test(int row, int col, int maxRow, int maxCol, byte val);            
}
