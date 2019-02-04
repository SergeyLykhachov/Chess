package com.yahoo.slykhachov.chess.function;

@FunctionalInterface
public interface TriFunction<A, R> {
	R apply(A a, int b, int c);
}
