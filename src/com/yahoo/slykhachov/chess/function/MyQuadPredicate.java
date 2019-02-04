package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface MyQuadPredicate<A> {
	boolean test(A a, int rowVal, int colVal, byte val);
}
