package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface MyQuadFunction<A, B> {
	B apply(A a, int i, int j, byte k);
}
